package ru.immagixe.task_tracker_backend.tasklogic.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private int id;
    private String header;
    private String description;
    private boolean statusActive;
}
