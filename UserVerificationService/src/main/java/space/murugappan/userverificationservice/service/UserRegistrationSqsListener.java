package space.murugappan.userverificationservice.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;

import org.springframework.stereotype.Service;
import space.murugappan.userverificationservice.dao.User;



@Service
public class UserRegistrationSqsListener {
    @SqsListener(value = "learn_sqs.fifo", factory = "defaultSqsListenerContainerFactory")
    public void listen(User registeredUser, Acknowledgement acknowledgement) {
        System.out.println(registeredUser);
        acknowledgement.acknowledgeAsync();
    }

}
