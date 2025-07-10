package nl.totalize.croc;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

public class SqsReceiver {
    private final SqsClient sqsClient;

    public SqsReceiver(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public List<Message> receiveMessages(String queueUrl, int maxMessages) {
        var request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxMessages)
                .build();

        var response = sqsClient.receiveMessage(request);
        return response.messages();
    }

    public void deleteMessage(String queueUrl, String receiptHandle) {
        var request = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();

        sqsClient.deleteMessage(request);
    }
}