package space.murugappan.userverificationservice.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import space.murugappan.userverificationservice.enums.RegistrationStatus;

import java.util.UUID;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    String password;
    RegistrationStatus registrationStatus;
    public User(){
        registrationStatus = RegistrationStatus.REGISTRATION_PENDING;
    }

}
