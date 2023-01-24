package ru.immagixe.task_tracker_backend.tasklogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.immagixe.task_tracker_backend.tasklogic.model.Task;
import ru.immagixe.task_tracker_backend.tasklogic.repositories.TaskRepository;
import ru.immagixe.task_tracker_backend.security.model.User;
import ru.immagixe.task_tracker_backend.security.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task findById(int userId) {
        Optional<Task> foundTask = taskRepository.findById(userId);
        return foundTask.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task does not exist"));
    }

    public List<Task> findByOwnerId(int ownerId) {
        return taskRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public void save(Task task, User user) {
        task.setOwner(user);
        task.setModified(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Transactional
    public void update(Task updatedTask, User user) {
        updatedTask.setOwner(user);
        updatedTask.setModified(LocalDateTime.now());
        taskRepository.save(updatedTask);
    }

    @Transactional
    public void delete(int taskId) {
        taskRepository.deleteById(taskId);
    }

    private List<Task> getUserTasks(int userId) {
        Optional<User> userFromDataBase = userRepository.findById(userId);
        return userFromDataBase.isPresent() ? userFromDataBase.get().getTasks() : Collections.emptyList();
    }
}
