package org.tcloud.client;

import com.tcloud.Serializer;
import com.tcloud.model.InitiateWorkflowInput;
import com.tcloud.model.InitiateWorkflowOutput;
import com.tcloud.model.TriggerEmailNotificationWorkflowInput;
import lombok.NonNull;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.tcloud.config.ApiConfig.getMethod;
import static org.tcloud.config.ApiConfig.getURI;

public class StepWorkflowClient {
    private static final String PRIVATE_ACCESS = "private";
    private static final String VERSION_1 = "v1";
    public static final String INITIATE_WORKFLOW_ENDPOINT = "initiate-workflow";
    public static final String TRIGGER_EMAIL_NOTIFICATION_WORKFLOW_ENDPOINT = "trigger-email-notification-workflow";

    private final HttpClient httpClient;

    private StepWorkflowClient() {
        httpClient = HttpClient.newHttpClient();
    }

    public static StepWorkflowClient create() {
        return new StepWorkflowClient();
    }

    public InitiateWorkflowOutput initiateWorkflow(@NonNull final InitiateWorkflowInput input) throws IOException, InterruptedException {
        final String serializedInput = Serializer.serializeAsString(input);
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(getURI(PRIVATE_ACCESS, VERSION_1, INITIATE_WORKFLOW_ENDPOINT))
                .header("Content-Type", "application/json")
                .method(getMethod(INITIATE_WORKFLOW_ENDPOINT), HttpRequest.BodyPublishers.ofString(serializedInput))
                .build();
        final HttpResponse<byte[]> output = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        return Serializer.deserialize(output.body(), InitiateWorkflowOutput.class);
    }

    public void triggerEmailNotificationWorkflow(@NonNull final TriggerEmailNotificationWorkflowInput input) throws IOException, InterruptedException {
        final String serializedInput = Serializer.serializeAsString(input);
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(getURI(PRIVATE_ACCESS, VERSION_1, TRIGGER_EMAIL_NOTIFICATION_WORKFLOW_ENDPOINT))
                .header("Content-Type", "application/json")
                .method(getMethod(TRIGGER_EMAIL_NOTIFICATION_WORKFLOW_ENDPOINT), HttpRequest.BodyPublishers.ofString(serializedInput))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }
}
