package kpfu.crm.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseFactory {

    public static ResponseEntity<Map<String, Object>> ok() {
        return ok(null);
    }

    public static ResponseEntity<Map<String, Object>> ok(Object data) {
        return build(null, data, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> fail(String message) {
        return build(message, null, HttpStatus.BAD_REQUEST);
    }

    private static ResponseEntity<Map<String, Object>> build(String errorMessage, Object data, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();

        body.put("errorMessage", errorMessage);
        body.put("data", data);

        return ResponseEntity.status(status)
                .body(body);
    }
}
