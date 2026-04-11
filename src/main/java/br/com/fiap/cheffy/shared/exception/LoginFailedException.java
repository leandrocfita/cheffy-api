package br.com.fiap.cheffy.shared.exception;

import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginFailedException extends BusinessException {

    private final String originalMessage;

    public LoginFailedException(ExceptionsKeys message, String originalMessage) {
        super(message);
        this.originalMessage = originalMessage;
    }
}
