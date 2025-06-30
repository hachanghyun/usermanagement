package io.github.hachanghyun.usermanagement.message.controller;

import io.github.hachanghyun.usermanagement.message.dto.SendMessageRequest;
import io.github.hachanghyun.usermanagement.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody SendMessageRequest request) {
        messageService.sendMessagesByAgeGroup(request.getAgeGroup(), request.getMessage());
        return ResponseEntity.ok("메시지 전송 요청 완료");
    }
}
