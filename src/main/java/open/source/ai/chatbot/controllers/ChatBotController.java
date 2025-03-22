package open.source.ai.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import open.source.ai.chatbot.controllers.rest.BaseRestController;
import open.source.ai.chatbot.dto.ChatBotRequestDTO;
import open.source.ai.chatbot.dto.ChatBotResponseAO;
import open.source.ai.chatbot.services.ChatBotService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ChatBotController implements BaseRestController {

    private final ChatBotService chatBotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatBotResponseAO> generateText(@RequestBody ChatBotRequestDTO message) {
        return new ResponseEntity<>(chatBotService.generateText(message), HttpStatusCode.valueOf(200));
    }
}
