package ru.immagixe.task_tracker_backend.tasklogic.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.immagixe.task_tracker_backend.tasklogic.TaskService;
import ru.immagixe.task_tracker_backend.tasklogic.model.dto.TaskDTO;
import ru.immagixe.task_tracker_backend.tasklogic.model.dto.UserDTO;
import ru.immagixe.task_tracker_backend.tasklogic.utils.MapperUtil;
import ru.immagixe.task_tracker_backend.security.securityDetails.TaskTrackerUserDetails;
import ru.immagixe.task_tracker_backend.security.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Планировщик задач", description = "Точка входа для работы с задачами пользователя")
@CrossOrigin("http://localhost:63342/")
@RestController
@SecurityRequirement(name = "basicAuth")
public class TaskController extends TaskControllerValidate {

    private final MapperUtil mapperUtil;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, MapperUtil mapperUtil) {
        super(taskService, userService);
        this.mapperUtil = mapperUtil;
    }

    @Operation(summary = "Получение пользователя")
    @GetMapping("/user")
    public UserDTO getUser(@AuthenticationPrincipal TaskTrackerUserDetails taskTrackerUserDetails) {
        this.checkAuthorization(taskTrackerUserDetails);

        return mapperUtil.convertToUserDTO(taskTrackerUserDetails.getUser());
    }

    @Operation(summary = "Получение списка задач пользователя")
    @GetMapping("/tasks")
    public List<TaskDTO> getTasks(@AuthenticationPrincipal TaskTrackerUserDetails taskTrackerUserDetails) {
        this.checkAuthorization(taskTrackerUserDetails);

        return taskService.findByOwnerId(taskTrackerUserDetails.getUser().getId()).stream()
                .map(mapperUtil::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Создание задачи")
    @PostMapping(path = "/tasks", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> addTask(@AuthenticationPrincipal TaskTrackerUserDetails taskTrackerUserDetails,
                                              @ModelAttribute TaskDTO taskDTO) {
        this.checkAuthorization(taskTrackerUserDetails);

        taskService.save(mapperUtil.convertToTask(taskDTO), userService.findByUserDetails(taskTrackerUserDetails));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Изменение задачи")
    @PatchMapping(path = "/tasks", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> updateTask(@AuthenticationPrincipal TaskTrackerUserDetails taskTrackerUserDetails,
                                                 @ModelAttribute TaskDTO taskDTO) {
        this.checkAuthorization(taskTrackerUserDetails);
        this.validateAccess(taskDTO, taskTrackerUserDetails.getUser());

        taskService.update(mapperUtil.convertToTask(taskDTO), userService.findByUserDetails(taskTrackerUserDetails));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Удаление задачи")
    @DeleteMapping(path = "/tasks", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> deleteTask(@AuthenticationPrincipal TaskTrackerUserDetails taskTrackerUserDetails,
                                                 @ModelAttribute TaskDTO taskDTO) {
        this.checkAuthorization(taskTrackerUserDetails);
        this.validateAccess(taskDTO, taskTrackerUserDetails.getUser());

        taskService.delete(taskDTO.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
