package open.source.ai.chatbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.source.ai.chatbot.config.APIConfig;
import open.source.ai.chatbot.dto.ChatBotRequestDTO;
import open.source.ai.chatbot.dto.ChatBotResponseAO;
import open.source.ai.chatbot.dto.ChatBotResponseDTO;
import open.source.ai.chatbot.dto.Choice;
import open.source.ai.chatbot.dto.Messages;
import open.source.ai.chatbot.exceptions.ChatBotException;
import open.source.ai.chatbot.interceptors.TogetherAIClientInterceptor;
import open.source.ai.chatbot.util.APIResponseGenerator;
import open.source.ai.chatbot.util.Key;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final APIConfig apiConfig;
    private final RestTemplate restTemplate;
    private final APIResponseGenerator responseGenerator;
    private final TogetherAIClientInterceptor togetherAIClientInterceptor;

    @Async
    public CompletableFuture<ChatBotResponseAO> generateText(ChatBotRequestDTO chatBotRequestDTO) {
        log.info("Execution started in {}.", getClass());

        String response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ChatBotRequestDTO> request = new HttpEntity<>(chatBotRequestDTO, headers);
            restTemplate.setInterceptors(List.of(togetherAIClientInterceptor));

            log.debug("Before calling the RestTemplate. Request: {}", request.getBody().getMessages().get(0).getContent());

            response = getResponse(request);

            log.debug("After calling the RestTemplate. Response: {}", response);
        } catch (HttpClientErrorException ex) {
            log.error("HttpClientErrorException: {}", ex.getMessage());
            throw new HttpClientErrorException(ex.getStatusCode(), Key.MESSAGE);
        } catch (ResourceAccessException ex) {
            log.error("ResourceAccessException: {}", ex.getMessage());
            throw new ResourceAccessException(Key.MESSAGE);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            throw new ChatBotException(Key.MESSAGE);
        }

        if (null == response || ObjectUtils.isEmpty(response)) {
            log.debug("Response is null.");
            throw new ChatBotException(Key.MESSAGE);
        }

        log.debug("Response: {}", response);
        return CompletableFuture.completedFuture(responseGenerator.generateResponse(response.replaceAll("(?s)<think>.*?</think>\n\n", ""),
                Key.SUCCESS, String.valueOf(HttpStatus.OK.value())));
    }

    private String getResponse(HttpEntity<ChatBotRequestDTO> request) {
        ResponseEntity<ChatBotResponseDTO> response = restTemplate.exchange(
                apiConfig.getClientUrl(Key.TOGETHER_AI),
                HttpMethod.POST,
                request,
                ChatBotResponseDTO.class
        );
        return Optional.ofNullable(response.getBody())
                .map(ChatBotResponseDTO::getChoices)
                .filter(choices -> !ObjectUtils.isEmpty(choices))
                .stream().findFirst().orElseThrow(() -> new ChatBotException("No choices available"))
                .stream().map(Choice::getMessage)
                .map(Messages::getContent).findFirst()
                .orElse(null);
    }
}
