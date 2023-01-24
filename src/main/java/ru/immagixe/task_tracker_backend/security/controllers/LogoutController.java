package ru.immagixe.task_tracker_backend.security.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Завершение сессии", description = "Точка входа для завершения сессии")
@RestController
@SecurityRequirement(name = "basicAuth")
public class LogoutController {

    @PostMapping("/logout")
    @Operation(summary =  "Завершение аутентифицированного сеанса пользователя")
    public void logout() {
        throw new UnsupportedOperationException();
    }
}


