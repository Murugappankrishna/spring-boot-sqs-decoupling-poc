package space.murugappan.userregistrationservice.awsutills;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import space.murugappan.userregistrationservice.dao.User;

@Component
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
            System.out.println("Failed in JSON Parsing" + jsonProcessingException);

        } catch (SdkServiceException sdkServiceException) {
            System.err.println("AWS service error: " + sdkServiceException.getMessage());
        } catch (SdkClientException sdkClientException) {
            System.err.println("AWS client error (network/credentials): " + sdkClientException.getMessage());
        }
    }
}
