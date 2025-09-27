package space.murugappan.userregistrationservice.awsutills;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import space.murugappan.userregistrationservice.dao.User;

@Component
@Slf4j
public class SQSUtills {
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    SQSUtills(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.objectMapper = new ObjectMapper();
    }

    public void putMessage(User user, String queueName) {
        try {
            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
            String userJsonString = objectMapper.writeValueAsString(user);
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageGroupId("user-registration-group")
                    .messageBody(userJsonString)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Failed in JSON Parsing{}", String.valueOf(jsonProcessingException));

        } catch (SdkServiceException sdkServiceException) {
            log.error("AWS service error: {}", sdkServiceException.getMessage());
        } catch (SdkClientException sdkClientException) {
            log.error("AWS client error (network/credentials): {}", sdkClientException.getMessage());
        }
    }
}
