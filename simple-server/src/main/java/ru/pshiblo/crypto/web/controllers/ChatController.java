package ru.pshiblo.crypto.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.pshiblo.crypto.web.dto.MessageChatDto;

/**
 * @author Maxim Pshiblo
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(@Payload MessageChatDto msg) {
        log.info("ws /chat: {}", msg);
        messagingTemplate.convertAndSendToUser(msg.getUsername(),"/messages", msg);
    }

}
