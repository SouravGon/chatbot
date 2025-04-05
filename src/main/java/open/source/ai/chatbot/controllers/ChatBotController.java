package open.source.ai.chatbot.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import open.source.ai.chatbot.controllers.rest.BaseRestController;
import open.source.ai.chatbot.dto.ChatBotRequestDTO;
import open.source.ai.chatbot.dto.ChatBotResponseAO;
import open.source.ai.chatbot.services.ChatBotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ChatBotController implements BaseRestController {

    private final ChatBotService chatBotService;

    @PostMapping("/chat")
    public CompletableFuture<ResponseEntity<ChatBotResponseAO>> generateText(@Valid @RequestBody ChatBotRequestDTO message) {
        return chatBotService.generateText(message)
                .thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }
}
