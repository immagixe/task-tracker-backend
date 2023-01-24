package ru.immagixe.task_tracker_backend.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import ru.immagixe.task_tracker_backend.security.model.User;
import ru.immagixe.task_tracker_backend.security.repositories.UserRepository;
import ru.immagixe.task_tracker_backend.security.securityDetails.TaskTrackerUserDetails;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(int userId) {
        Optional<User> user = userRepository.findById(userId);
        return user;
    }

    public User findByUserDetails (TaskTrackerUserDetails taskTrackerUserDetails) {
        int userId = taskTrackerUserDetails.getUser().getId();
        return findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }
}
