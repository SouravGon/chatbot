package open.source.ai.chatbot.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import open.source.ai.chatbot.util.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RefreshScope
@Component
@Getter
@Setter
@NoArgsConstructor
public class APIConfig {

    @Value("${chatbot.api.key}")
    private String apiKey;
    @Value("${chatbot.api.url}")
    private String apiUrl;
    private final Map<String, String> clientUrls = new HashMap<>();
    private String togetherAIBasicAuthKey;

    @PostConstruct
    public void generateTogetherAIBasicAuthKey() {
        togetherAIBasicAuthKey = Key.BEARER + apiKey;
    }

    @PostConstruct
    public void initializeClientUrls() {
        clientUrls.put(Key.TOGETHER_AI, apiUrl);
    }

    public String getClientUrl(String clientName) {
        return clientUrls.get(clientName);
    }
}
