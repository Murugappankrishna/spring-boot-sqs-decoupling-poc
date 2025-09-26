package space.murugappan.userverificationservice.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.ListenerMode;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.Duration;


@Configuration
public class SqsConfig {

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder().build();
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(options->
                        options.acknowledgementMode(AcknowledgementMode.MANUAL)
                                .maxMessagesPerPoll(10)
                                .maxDelayBetweenPolls(Duration.ofSeconds(10))
                                .pollTimeout(Duration.ofSeconds(10))
                                .listenerMode(ListenerMode.BATCH)
                )
                .build();
    }

}
