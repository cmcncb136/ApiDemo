package com.example.apidemo.lamda;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class LambdaService {
    private final LambdaClient lambdaClient;

    public String invokeLambda(String url, String payload) {
        InvokeRequest request = InvokeRequest.builder()
                .functionName(url)
                .payload(SdkBytes.fromString(payload, StandardCharsets.UTF_8))
                .build();

        InvokeResponse response = lambdaClient.invoke(request);

        return response.payload().asUtf8String();
    }
}
