package open.source.ai.chatbot.exceptions.advice;

import open.source.ai.chatbot.controllers.ChatBotController;
import open.source.ai.chatbot.dto.ChatBotResponseAO;
import open.source.ai.chatbot.exceptions.ChatBotException;
import open.source.ai.chatbot.util.Key;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@ControllerAdvice(assignableTypes = {ChatBotController.class})
@ResponseBody
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ChatBotCustomExceptionHandler {

    @ExceptionHandler(value = {HttpClientErrorException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ChatBotResponseAO handleHttpClientErrorException(HttpClientErrorException ex) {
        return new ChatBotResponseAO().builder()
                .status(Key.ERROR)
                .statusCode(String.valueOf(ex.getRawStatusCode()))
                .statusMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = {ResourceAccessException.class})
    @ResponseStatus(value = HttpStatus.BAD_GATEWAY)
    public ChatBotResponseAO handleResourceAccessException(ResourceAccessException ex) {
        return new ChatBotResponseAO().builder()
                .status(Key.ERROR)
                .statusCode(String.valueOf(HttpStatus.BAD_GATEWAY.value()))
                .statusMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = {ChatBotException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ChatBotResponseAO handleChatBotException(ChatBotException ex) {
        return new ChatBotResponseAO().builder()
                .status(Key.ERROR)
                .statusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .statusMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ChatBotResponseAO handleException(Exception ex) {
        return new ChatBotResponseAO().builder()
                .status(Key.ERROR)
                .statusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .statusMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ChatBotResponseAO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        AtomicReference<String> errorMessage = new AtomicReference<>("");
        errors.forEach((key, value) -> errorMessage.set(errorMessage + key + ": " + value + ", "));
        return new ChatBotResponseAO().builder()
                .status(Key.ERROR)
                .statusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .statusMessage(errorMessage.toString())
                .build();
    }
}
