package ru.pshiblo.encryption.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pshiblo.encryption.crypto.model.KeyGamal;

/**
 * @author Maxim Pshiblo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyGamalResponseDto {
    private int p;
    private int g;
    private int x;
    private int y;

    public static KeyGamalResponseDto fromKeyGamal(KeyGamal keyGamal) {
        return new KeyGamalResponseDto(
                keyGamal.getP(),
                keyGamal.getG(),
                keyGamal.getX(),
                keyGamal.getY()
        );
    }
}
