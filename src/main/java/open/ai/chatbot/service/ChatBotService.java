package open.ai.chatbot.service;

import lombok.RequiredArgsConstructor;
import open.ai.chatbot.dto.ChatBotRequestDTO;
import open.ai.chatbot.dto.ChatBotResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String url;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.max-tokens}")
    private String maxTokens;

    private final RestTemplate restTemplate;

    public String generateText(ChatBotRequestDTO chatBotRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        chatBotRequestDTO.setModel(model);
        chatBotRequestDTO.setMax_tokens(Integer.parseInt(maxTokens));
        HttpEntity<ChatBotRequestDTO> request = new HttpEntity<>(chatBotRequestDTO, headers);

        ResponseEntity<ChatBotResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                ChatBotResponseDTO.class
        );

        return response.getBody().getChoices().get(0).getText();
    }
}
