package ru.immagixe.task_tracker_backend.emailgreeting.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.immagixe.task_tracker_backend.emailgreeting.model.dto.EmailDTO;
import ru.immagixe.task_tracker_backend.emailgreeting.services.rabbitmq.RabbitProducer;
import ru.immagixe.task_tracker_backend.security.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final EmailCreatorService emailCreatorService;
    private final RabbitProducer rabbitProducer;

    public void sendEmail(User user) {
        List<User> users = new ArrayList<>(List.of(user));
        List<EmailDTO> emailMessages = emailCreatorService.getEmailMessages(users);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(emailMessages);
            rabbitProducer.sendMessage(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
