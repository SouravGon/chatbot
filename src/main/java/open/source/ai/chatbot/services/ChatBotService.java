package open.source.ai.chatbot.services;

import lombok.RequiredArgsConstructor;
import open.source.ai.chatbot.config.APIConfig;
import open.source.ai.chatbot.dto.ChatBotRequestDTO;
import open.source.ai.chatbot.dto.ChatBotResponseAO;
import open.source.ai.chatbot.dto.ChatBotResponseDTO;
import open.source.ai.chatbot.exceptions.ChatBotException;
import open.source.ai.chatbot.interceptors.TogetherAIClientInterceptor;
import open.source.ai.chatbot.util.APIResponseGenerator;
import open.source.ai.chatbot.util.Key;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    public static final String MESSAGE = "Sorry, Try again later.";
    private final APIConfig apiConfig;
    private final RestTemplate restTemplate;
    private final APIResponseGenerator responseGenerator;
    private final TogetherAIClientInterceptor togetherAIClientInterceptor;

    public ChatBotResponseAO generateText(ChatBotRequestDTO chatBotRequestDTO) {
        chatBotRequestDTO.setModel(apiConfig.getModel());
        chatBotRequestDTO.getMessages().get(0).setRole(Key.USER);

        ResponseEntity<ChatBotResponseDTO> response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ChatBotRequestDTO> request = new HttpEntity<>(chatBotRequestDTO, headers);
            restTemplate.setInterceptors(List.of(togetherAIClientInterceptor));
            response = restTemplate.exchange(
                    apiConfig.getClientUrl(Key.TOGETHER_AI),
                    HttpMethod.POST,
                    request,
                    ChatBotResponseDTO.class
            );
        } catch (HttpClientErrorException ex) {
            throw new HttpClientErrorException(ex.getStatusCode(), ex.getMessage());
        } catch (ResourceAccessException ex) {
            throw new ResourceAccessException(ex.getMessage());
        } catch (Exception ex) {
            throw new ChatBotException(ex.getMessage());
        }

        if (ObjectUtils.isEmpty(response.getBody()) || response.getBody().getChoices().isEmpty() || !response.getStatusCode().is2xxSuccessful()) {
            return responseGenerator.generateResponse(MESSAGE, Key.ERROR, String.valueOf(response.getStatusCode().value()));
        }

        return responseGenerator.generateResponse(response.getBody().getChoices().get(0).getMessage().getContent(),
                Key.SUCCESS, String.valueOf(response.getStatusCode().value()));
    }
}
