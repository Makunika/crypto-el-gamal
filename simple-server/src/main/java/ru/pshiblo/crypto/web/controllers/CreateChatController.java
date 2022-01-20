package ru.pshiblo.crypto.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pshiblo.crypto.web.dto.CreateChatDto;
import ru.pshiblo.crypto.web.dto.OpenKeyDto;

import java.math.BigInteger;

/**
 * @author Maxim Pshiblo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/create-chat")
@Slf4j
public class CreateChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public void createChat(@RequestBody CreateChatDto createChatDto) {
        log.info("post createChat: {}", createChatDto);
        new BigInteger().modPow()
        messagingTemplate.convertAndSendToUser(createChatDto.getUsernameTo(),"/create", createChatDto);
    }

    @PostMapping("key")
    public void sendNewOpenKey(@RequestBody OpenKeyDto openKeyDto) {
        log.info("post key: {}", openKeyDto);
        messagingTemplate.convertAndSendToUser(openKeyDto.getUsernameFrom(),"/key", openKeyDto);
    }
}
