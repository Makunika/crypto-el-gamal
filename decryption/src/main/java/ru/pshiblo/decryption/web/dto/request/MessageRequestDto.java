package ru.pshiblo.encryption.web.dto.request;

import lombok.Data;
import ru.pshiblo.encryption.crypto.model.MessageGamalOrigin;

/**
 * @author Maxim Pshiblo
 */
@Data
public class MessageRequestDto {
    private int data;
    private KeyGamalRequestDto key;

    public static MessageGamalOrigin toMessageGamalOrigin(MessageRequestDto request) {
        MessageGamalOrigin origin = new MessageGamalOrigin();
        origin.setData(request.getData());
        return origin;
    }
}
