package nl.totalize.croc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SqsIntegrationTest {
    private SqsClient sqsClient;
    private String queueUrl;
    private SqsSender sender;
    private SqsReceiver receiver;

    @BeforeEach
    void setUp() {
        var accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        var secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        var region = System.getenv("AWS_REGION");
        
        if (accessKey != null && secretKey != null) {
            var credentials = AwsBasicCredentials.create(accessKey, secretKey);
            sqsClient = SqsClient.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .region(region != null ? Region.of(region) : Region.US_EAST_1)
                    .build();
        } else {
            sqsClient = SqsClient.builder()
                    .region(region != null ? Region.of(region) : Region.US_EAST_1)
                    .build();
        }
        
        var queueName = "test-queue-" + UUID.randomUUID().toString().substring(0, 8);
        queueUrl = sqsClient.createQueue(CreateQueueRequest.builder()
                .queueName(queueName)
                .build()).queueUrl();
        
        sender = new SqsSender(sqsClient);
        receiver = new SqsReceiver(sqsClient);
    }

    @AfterEach
    void tearDown() {
        if (queueUrl != null) {
            sqsClient.deleteQueue(DeleteQueueRequest.builder()
                    .queueUrl(queueUrl)
                    .build());
        }
        sqsClient.close();
    }

    @Test
    void testSendAndReceiveMessage() {
        var messageBody = "Test message";
        
        var messageId = sender.sendMessage(queueUrl, messageBody);
        assertNotNull(messageId);
        
        var messages = receiver.receiveMessages(queueUrl, 1);
        assertEquals(1, messages.size());
        assertEquals(messageBody, messages.get(0).body());
        
        receiver.deleteMessage(queueUrl, messages.get(0).receiptHandle());
    }
}