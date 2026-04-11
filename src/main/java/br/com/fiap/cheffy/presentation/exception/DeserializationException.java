package br.com.fiap.cheffy.presentation.exception;

import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeserializationException extends ApiException {

    public DeserializationException(ExceptionsKeys message) {
        super(message);
    }

}
