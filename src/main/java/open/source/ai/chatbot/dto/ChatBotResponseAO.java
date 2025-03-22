package open.source.ai.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotResponseAO {

    @JsonProperty("STATUS")
    private String status;
    @JsonProperty("STATUS_CODE")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusCode;
    @JsonProperty("STATUS_MESSAGE")
    private String statusMessage;
}
