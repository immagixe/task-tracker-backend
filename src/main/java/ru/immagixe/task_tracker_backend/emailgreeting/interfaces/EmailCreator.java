package ru.immagixe.task_tracker_backend.emailgreeting.interfaces;

import ru.immagixe.task_tracker_backend.security.model.User;

public interface EmailCreator {

    String createEmailAddress(User user);

    String createEmailTitle(User user);

    String createEmailText(User user);
}
