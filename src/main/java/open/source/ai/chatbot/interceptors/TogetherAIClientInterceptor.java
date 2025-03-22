package open.source.ai.chatbot.interceptors;

import lombok.AllArgsConstructor;
import open.source.ai.chatbot.config.APIConfig;
import open.source.ai.chatbot.util.Key;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class TogetherAIClientInterceptor implements ClientHttpRequestInterceptor {

    private final APIConfig apiConfig;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set(Key.AUTHORIZATION, apiConfig.getTogetherAIBasicAuthKey());
        return execution.execute(request, body);
    }
}
