package ru.immagixe.task_tracker_backend.tasklogic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.immagixe.task_tracker_backend.tasklogic.TaskService;
import ru.immagixe.task_tracker_backend.tasklogic.model.dto.TaskDTO;
import ru.immagixe.task_tracker_backend.tasklogic.model.Task;
import ru.immagixe.task_tracker_backend.security.model.User;
import ru.immagixe.task_tracker_backend.security.securityDetails.TaskTrackerUserDetails;
import ru.immagixe.task_tracker_backend.security.services.UserService;

@RequiredArgsConstructor
public class TaskControllerValidate {

    protected final TaskService taskService;
    protected final UserService userService;

    protected void validateAccess(TaskDTO taskDTO, User user) {
        Task foundedTask = taskService.findById(taskDTO.getId());

        if (foundedTask.getOwner().getId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    protected void checkAuthorization(TaskTrackerUserDetails taskTrackerUserDetails) {
        if (taskTrackerUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}

