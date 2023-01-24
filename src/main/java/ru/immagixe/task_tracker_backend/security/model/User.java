package ru.immagixe.task_tracker_backend.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.immagixe.task_tracker_backend.tasklogic.model.Task;

import javax.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Must be in email format")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Task> tasks;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
