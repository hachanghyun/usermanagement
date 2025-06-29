package io.github.hachanghyun.usermanagement.message.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagePayload {
    private String name;
    private String phone;
    private String message;
}
