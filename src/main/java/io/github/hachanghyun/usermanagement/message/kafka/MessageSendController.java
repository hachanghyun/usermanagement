package io.github.hachanghyun.usermanagement.message.kafka;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class MessageSendController {

    private final KafkaProducerService kafkaProducerService;
    private final UserRepository userRepository;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessageToUsers(@RequestBody MessageRequest request) {
        userRepository.findAll().stream()
                .filter(user -> user.getAgeRange().equals(request.getAgeRange()))
                .forEach(user -> {
                    String payload = user.getPhoneNumber() + "," + request.getMessage() + "," + user.getName();
                    kafkaProducerService.sendMessage("message-topic", payload);
                });
        return ResponseEntity.ok().build();
    }
}
