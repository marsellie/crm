package kpfu.crm.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class RestResponse extends ResponseEntity<HashMap<String, ?>> {

    public RestResponse(HashMap<String, ?> body, HttpStatus status) {
        super(body, status);
    }
}
