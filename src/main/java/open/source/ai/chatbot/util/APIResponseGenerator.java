package open.source.ai.chatbot.util;

import lombok.extern.slf4j.Slf4j;
import open.source.ai.chatbot.dto.ChatBotResponseAO;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class APIResponseGenerator {

    public ChatBotResponseAO generateResponse(String message, String status, String... statusCode) {
        log.info("Execution started in {}.", getClass());
        log.debug("Message: {}, Status: {}, StatusCode: {}", message, status, statusCode);
        return ChatBotResponseAO.builder()
                .status(status)
                .statusCode(statusCode.length > 0 ? statusCode[0] : null)
                .statusMessage(message)
                .build();
    }
}
