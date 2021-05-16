package kpfu.crm.util;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class ResponseFactory {

    public static RestResponse ok() {
        return ok("");
    }

    public static RestResponse ok(Object data) {
        return build("", data, HttpStatus.OK);
    }

    public static RestResponse fail(String message) {
        return build(message, "", HttpStatus.BAD_REQUEST);
    }

    private static RestResponse build(String errorMessage, Object data, HttpStatus status) {
        HashMap<String, Object> body = new HashMap<>() {{
            put("errorMessage", errorMessage);
            put("data", data);
        }};

        return new RestResponse(body, status);
    }
}
