package space.murugappan.userverificationservice.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.murugappan.userverificationservice.dao.User;
import space.murugappan.userverificationservice.enums.RegistrationStatus;



import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.registrationStatus=:registrationStatus WHERE u.id =:id")
    void  updateRegistrationStatus(UUID id , RegistrationStatus registrationStatus);


}
