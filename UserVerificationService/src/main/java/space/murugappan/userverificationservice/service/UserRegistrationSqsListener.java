package space.murugappan.userverificationservice.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.BatchAcknowledgement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class UserRegistrationSqsListener {
    Executor ioExecutor =  Executors.newFixedThreadPool(10);
    @SqsListener(value = "learn_sqs.fifo", factory = "defaultSqsListenerContainerFactory")
    public void listen(List<String> messages, BatchAcknowledgement <String> batchAcknowledgement) {
        System.out.println(messages.size());
        messages.forEach(message -> CompletableFuture.runAsync(()->{

            try {
                Thread.sleep((long) (Math.random() * 100));
                System.out.println(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },ioExecutor));
        batchAcknowledgement.acknowledgeAsync();
    }

}
