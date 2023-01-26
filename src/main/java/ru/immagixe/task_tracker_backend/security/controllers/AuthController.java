package ru.immagixe.task_tracker_backend.security.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.immagixe.task_tracker_backend.emailgreeting.services.EmailSenderService;
import ru.immagixe.task_tracker_backend.security.model.User;
import ru.immagixe.task_tracker_backend.security.services.RegistrationService;
import ru.immagixe.task_tracker_backend.security.util.EmailAlreadyExistsException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@OpenAPIDefinition(info = @Info(
        title = "Планировщик задач",
        description = "<H4>Проект с микросервисной архитектурой</H4>"),
        servers = {@Server(url = "http://localhost:8080", description = "Local server")})
@Tag(name = "Регистрация", description = "Точка входа для регистрации пользователя")
@RestController
//@CrossOrigin("http://localhost:63342/")
@CrossOrigin("http://195.133.145.60:8080/")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final EmailSenderService emailSenderService;

    @PostMapping(path = "/user", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> performRegistration(@ModelAttribute("account") @Valid User user) {
        try {
            registrationService.checkEmailExists(user);
        } catch (EmailAlreadyExistsException exc) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already taken", exc);
        }

        registrationService.register(user);
        emailSenderService.sendEmail(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Map<String, String> handleValidationExceptions(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
