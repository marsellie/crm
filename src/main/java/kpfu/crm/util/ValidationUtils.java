package kpfu.crm.util;

import kpfu.crm.exception.RestException;
import lombok.experimental.UtilityClass;

import java.util.Objects;

import static org.springframework.util.ObjectUtils.isEmpty;

@UtilityClass
public class ValidationUtils {

    public static void notNull(Object o, String message) {
        if (o == null) {
            throw new RestException(message);
        }
    }

    public static void notEmpty(Object o, String message) {
        if (isEmpty(o)) {
            throw new RestException(message);
        }
    }

    public static void areEqual(Object o1, Object o2, String message) {
        if (!Objects.equals(o1, o2)) {
            throw new RestException(message);
        }
    }

    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new RestException(message);
        }
    }
}
