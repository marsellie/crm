package kpfu.crm.exceptions;

import kpfu.crm.util.ResponseFactory;
import kpfu.crm.util.RestResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class AppExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RestResponse handleAll(Exception e) {
        return ResponseFactory.fail(e.getMessage());
    }

    @ExceptionHandler(RestException.class)
    public RestResponse handleServiceException(RestException e) {
        return ResponseFactory.fail(e.getMessage());
    }
}
