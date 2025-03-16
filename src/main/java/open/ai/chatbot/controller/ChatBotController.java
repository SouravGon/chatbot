package open.ai.chatbot.controller;

import lombok.RequiredArgsConstructor;
import open.ai.chatbot.dto.ChatBotRequestDTO;
import open.ai.chatbot.service.ChatBotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;

    @PostMapping("/chat")
    public String generateText(@RequestBody ChatBotRequestDTO chatBotRequestDTO) {
        return chatBotService.generateText(chatBotRequestDTO);
    }
}
