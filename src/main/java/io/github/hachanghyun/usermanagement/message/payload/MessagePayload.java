package io.github.hachanghyun.usermanagement.message.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload {
    private String phoneNumber;
    private String message;
    private String name;

    @JsonCreator
    public MessagePayload(
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("message") String message,
            @JsonProperty("name") String name) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.name = name;
    }
}

