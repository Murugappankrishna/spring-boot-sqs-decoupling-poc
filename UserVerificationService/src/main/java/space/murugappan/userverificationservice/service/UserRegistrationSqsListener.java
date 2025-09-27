package space.murugappan.userverificationservice.service;

import io.awspring.cloud.sqs.annotation.SqsListener;


import io.awspring.cloud.sqs.listener.acknowledgement.BatchAcknowledgement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

import java.security.SecureRandom;
import java.util.Collections;
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


    UserRegistrationSqsListener(MailUtils mailUtils, UserRepo userRepo) {
        this.mailUtils = mailUtils;
        this.userRepo = userRepo;
    }

    @SqsListener(value = "learn_sqs.fifo", factory = "defaultSqsListenerContainerFactory")
    public void listen(List<Message<User>> registeredUsers, BatchAcknowledgement<User> batchAcknowledgement) {
        registeredUsers.forEach(user -> CompletableFuture.runAsync(() -> processUser(user, batchAcknowledgement))

        );


    }

    private void processUser(Message<User> message, BatchAcknowledgement<User> batchAcknowledgement) {
        User registeredUser = message.getPayload();
        try {
            mailUtils.sendVerificationEmail(registeredUser.getEmail(), registeredUser.getName(), String.format("%06d", random.nextInt(1000000)));

            userRepo.updateRegistrationStatus(registeredUser.getId(), RegistrationStatus.MAIL_SENT_PENDING);
            batchAcknowledgement.acknowledgeAsync(Collections.singleton(message));

        } catch (Exception e) {
            log.error("Email Functionality Failed For the Mail {} with the Exception {}", registeredUser.getEmail(), e.getMessage());
        }

    }

}
