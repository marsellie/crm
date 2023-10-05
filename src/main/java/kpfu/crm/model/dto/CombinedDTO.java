package kpfu.crm.model.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CombinedDTO {

    @JsonValue
    private final Map<String, Object> map = new HashMap<>();

    public CombinedDTO with(String name, Object value) {
        map.put(name, value);

        return this;
    }
}
