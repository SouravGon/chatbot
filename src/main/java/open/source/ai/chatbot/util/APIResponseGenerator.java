package open.source.ai.chatbot.util;

import open.source.ai.chatbot.dto.ChatBotResponseAO;
import org.springframework.stereotype.Component;

@Component
public class APIResponseGenerator {

    public ChatBotResponseAO generateResponse(String message, String status, String... statusCode) {
        return ChatBotResponseAO.builder()
                .status(status)
                .statusCode(statusCode.length > 0 ? statusCode[0] : null)
                .statusMessage(message)
                .build();
    }
}
