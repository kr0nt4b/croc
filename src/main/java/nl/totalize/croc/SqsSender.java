package nl.totalize.croc;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class SqsSender {
    private final SqsClient sqsClient;

    public SqsSender(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public String sendMessage(String queueUrl, String messageBody) {
        var request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        var response = sqsClient.sendMessage(request);
        return response.messageId();
    }
}