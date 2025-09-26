package space.murugappan.userregistrationservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.murugappan.userregistrationservice.dao.User;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
}
