package space.murugappan.userregistrationservice.dao;

import jakarta.persistence.*;
import lombok.*;
import space.murugappan.userregistrationservice.enums.RegistrationStatus;

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
