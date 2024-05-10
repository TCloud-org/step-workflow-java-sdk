package org.tcloud.client;

import com.tcloud.Serializer;
import com.tcloud.model.InitiateWorkflowInput;
import com.tcloud.model.InitiateWorkflowOutput;
import com.tcloud.model.NotifyWorkflowInput;
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
    public static final String NOTIFY_WORKFLOW_ENDPOINT = "notify-workflow";
    public static final String TRIGGER_EMAIL_NOTIFICATION_WORKFLOW_ENDPOINT = "trigger-email-notification-workflow";

    private final HttpClient httpClient;

    private StepWorkflowClient() {
        httpClient = HttpClient.newHttpClient();
    }

    public static StepWorkflowClient create() {
        return new StepWorkflowClient();
    }

    public <T, R> R sendRequest(@NonNull final String endpoint,
                                @NonNull final T input,
                                @NonNull final Class<R> responseType) throws IOException, InterruptedException {
        final String serializedInput = Serializer.serializeAsString(input);
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(getURI(PRIVATE_ACCESS, VERSION_1, endpoint))
                .header("Content-Type", "application/json")
                .method(getMethod(endpoint), HttpRequest.BodyPublishers.ofString(serializedInput))
                .build();
        final HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        return Serializer.deserialize(response.body(), responseType);
    }

    public InitiateWorkflowOutput initiateWorkflow(@NonNull final InitiateWorkflowInput input) throws IOException, InterruptedException {
        return sendRequest(INITIATE_WORKFLOW_ENDPOINT, input, InitiateWorkflowOutput.class);
    }

    public void notifyWorkflow(@NonNull final NotifyWorkflowInput input) throws IOException, InterruptedException {
        sendRequest(NOTIFY_WORKFLOW_ENDPOINT, input, Void.class);
    }

    public void triggerEmailNotificationWorkflow(@NonNull final TriggerEmailNotificationWorkflowInput input) throws IOException, InterruptedException {
        sendRequest(TRIGGER_EMAIL_NOTIFICATION_WORKFLOW_ENDPOINT, input, Void.class);
    }
}
