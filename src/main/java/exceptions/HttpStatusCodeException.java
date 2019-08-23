package exceptions;

import java.io.IOException;

public class HttpStatusCodeException extends IOException {
    public HttpStatusCodeException(String message) {
        super(message);
    }
}
