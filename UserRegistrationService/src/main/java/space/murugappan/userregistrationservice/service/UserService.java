package space.murugappan.userregistrationservice.service;

import org.springframework.stereotype.Service;
import space.murugappan.userregistrationservice.awsutills.SQSUtills;
import space.murugappan.userregistrationservice.dao.User;
import space.murugappan.userregistrationservice.repo.UserRepo;

import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final SQSUtills sqsUtills;
    private static final String sqsUserMailQueue = "learn_sqs.fifo";

    UserService(UserRepo userRepo, SQSUtills sqsUtills) {
        this.sqsUtills = sqsUtills;
        this.userRepo = userRepo;
    }

    public User registerUser(User user) {
        User savedUser = userRepo.save(user);
        CompletableFuture.runAsync(() -> sqsUtills.putMessage(user, sqsUserMailQueue));
        return savedUser;
    }
}
