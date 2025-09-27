package space.murugappan.userverificationservice.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

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


    private UserRegistrationSqsListener(MailUtils mailUtils,UserRepo userRepo) {
        this.mailUtils = mailUtils;
        this.userRepo = userRepo;
    }

    @SqsListener(value = "learn_sqs.fifo", factory = "defaultSqsListenerContainerFactory")
    public void listen(User registeredUser, Acknowledgement acknowledgement) {
        try {
            mailUtils.sendVerificationEmail(registeredUser.getEmail(),
                    registeredUser.getName(),
                    String.format("%06d", random.nextInt(1000000))

            );
            userRepo.updateRegistrationStatus(registeredUser.getId(), RegistrationStatus.MAIL_SENT_PENDING);
            acknowledgement.acknowledgeAsync();
        } catch (Exception e) {
            log.error("Email Functionality Failed For the Mail {} with the Exception {}", registeredUser.getEmail(), e.getMessage());
        }

    }

}
