package space.murugappan.userregistrationservice.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SQSClientConfig {
    @Bean
    SqsClient createSqsClient() {
        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
