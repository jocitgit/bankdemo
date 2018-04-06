package ie.cit.comp8058.bankdemo.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Item Not Found")
public class ItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -446829042265174757L;

}
