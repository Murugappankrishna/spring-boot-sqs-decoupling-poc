package space.murugappan.userregistrationservice.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    @Enumerated(EnumType.STRING)
    RegistrationStatus registrationStatus;
    @Column(unique = true)
    @Email(message = "Please Enter a Valid Email")
    String email;
    public User(){
        registrationStatus = RegistrationStatus.REGISTRATION_PENDING;
    }

}
