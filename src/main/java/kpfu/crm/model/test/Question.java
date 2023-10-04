package kpfu.crm.model.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kpfu.crm.exception.RestException;
import kpfu.crm.model.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.EAGER;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;

@Entity
@Getter
@Setter
public class Question extends BaseEntity {

    @NotBlank
    private String value;

    private Integer score;

    @NotNull
    private Integer position;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @NotNull
    @ManyToOne
    private Test test;

    @OneToMany(mappedBy = "question", fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Option> options;

    @Getter
    @RequiredArgsConstructor
    public enum Type {

        ONE("Одиночный выбор"),
        MULTI("Множественный выбор"),
        TEXT("Текстовый вопрос");

        private static final Map<String, Type> VALUES_MAP = Arrays.stream(values())
                .collect(Collectors.toMap(Type::getValue, identity()));

        private final String value;

        @JsonCreator
        public static Type toType(String value) {
            return ofNullable(VALUES_MAP.get(value))
                    .orElseThrow(() -> {
                        String errorMessage = "Не найден тип вопроса [%s]".formatted(value);
                        return new RestException(errorMessage);
                    });
        }

        @JsonValue
        public String toValue() {
            return value;
        }
    }
}
