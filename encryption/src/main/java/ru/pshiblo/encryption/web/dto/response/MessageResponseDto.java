package ru.pshiblo.encryption.web.dto.response;

import lombok.Data;
import ru.pshiblo.encryption.crypto.model.KeyGamal;
import ru.pshiblo.encryption.crypto.model.MessageGamalEncrypt;

/**
 * @author Maxim Pshiblo
 */
@Data
public class MessageEncryptResponseDto {
    private int a;
    private int b;
    private KeyGamalResponseDto key;

    public static MessageEncryptResponseDto fromMessageAndKeyGamal(MessageGamalEncrypt msg, KeyGamal key) {
        MessageEncryptResponseDto response = new MessageEncryptResponseDto();
        response.setA(msg.getA());
        response.setB(msg.getB());
        response.setKey(KeyGamalResponseDto.fromKeyGamal(key));
        return response;
    }
}
