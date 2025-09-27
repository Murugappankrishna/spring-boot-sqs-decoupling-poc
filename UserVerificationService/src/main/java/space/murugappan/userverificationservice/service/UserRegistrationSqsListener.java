package space.murugappan.userverificationservice.service;

import io.awspring.cloud.sqs.annotation.SqsListener;


import io.awspring.cloud.sqs.listener.acknowledgement.BatchAcknowledgement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import space.murugappan.userverificationservice.dao.User;
import space.murugappan.userverificationservice.enums.RegistrationStatus;
import space.murugappan.userverificationservice.repo.UserRepo;
import space.murugappan.userverificationservice.utils.MailUtils;


@Service
@Slf4j
public class UserRegistrationSqsListener {
    private final MailUtils mailUtils;
    private final UserRepo userRepo;
    private static final SecureRandom random = new SecureRandom();


    private UserRegistrationSqsListener(MailUtils mailUtils, UserRepo userRepo) {
        this.mailUtils = mailUtils;
        this.userRepo = userRepo;
    }

    @SqsListener(value = "learn_sqs.fifo", factory = "defaultSqsListenerContainerFactory")
    public void listen(List<User> registeredUsers, BatchAcknowledgement<User> batchAcknowledgement) {
        List<CompletableFuture<Void>> futures = registeredUsers.stream().map(user -> CompletableFuture.runAsync(() -> processUser(user))).toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).whenComplete((res, err) -> {
            if (err == null) {
                batchAcknowledgement.acknowledgeAsync();
            } else {
                log.error("Batch processing failed: {}", err.getMessage(), err);
            }
        });

    }

    private void processUser(User registeredUser) {
        try {
            mailUtils.sendVerificationEmail(
                    registeredUser.getEmail(),
                    registeredUser.getName(),
                    String.format("%06d", random.nextInt(1000000))
            );

            userRepo.updateRegistrationStatus(
                    registeredUser.getId(),
                    RegistrationStatus.MAIL_SENT_PENDING
            );

        } catch (Exception e) {
            log.error("Email Functionality Failed For the Mail {} with the Exception {}",
                    registeredUser.getEmail(),
                    e.getMessage());
        }

    }

}
