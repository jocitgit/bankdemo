package ie.cit.comp8058.bankdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Bad Request")
public class AuthRequestException extends RuntimeException {

	private static final long serialVersionUID = -9000798804457057848L;

}
