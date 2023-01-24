package ru.immagixe.task_tracker_backend.emailgreeting.services;

import org.springframework.stereotype.Service;
import ru.immagixe.task_tracker_backend.emailgreeting.interfaces.EmailCreator;
import ru.immagixe.task_tracker_backend.security.model.User;

@Service
public class GreetingEmailCreator implements EmailCreator {

    @Override
    public String createEmailAddress(User user) {
        return user.getEmail();
    }

    @Override
    public String createEmailTitle(User user) {
        return "Спасибо за регистрацию в сервисе <TaskTracker>!";
    }

    @Override
    public String createEmailText(User user) {
        return "Добро пожаловать в наш сервис по отслеживанию задач! Желаем приятного использования!\n" +
                "Ваш логин для авторизации: " + user.getEmail();
    }
}
