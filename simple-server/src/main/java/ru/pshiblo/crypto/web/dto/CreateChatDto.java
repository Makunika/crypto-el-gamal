package ru.pshiblo.crypto.web.dto;

import lombok.Data;

/**
 * @author Maxim Pshiblo
 */
@Data
public class CreateChatDto {
    private String usernameTo;
    private String usernameFrom;
    private int p;
    private int g;
    private int y;
}
