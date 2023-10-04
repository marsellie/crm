package kpfu.crm.model.test.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kpfu.crm.model.test.Question.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

import static kpfu.crm.model.test.Question.Type.TEXT;
import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTestDTO {

    @NotBlank(message = "Название теста не должно быть пустым")
    private String name;

    private Boolean scored;

    @Valid
    @NotEmpty(message = "Должен быть хотя бы один вопрос")
    private Collection<CreateQuestionDTO> questions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateQuestionDTO {

        private Integer score;

        @NotBlank(message = "Вопрос не должен быть пустым")
        private String value;

        @NotNull(message = "Порядок вопроса не может быть пустым")
        private Integer position;

        @NotNull(message = "Тип не может быть пустым")
        private Type type;

        private Collection<CreateOptionDTO> options;

        @AssertTrue(message = "Варианты ответов не соответствуют типу")
        private boolean isOptionsValid() {
            if (type == TEXT) {
                return true;
            }

            return !isEmpty(options) && options.stream()
                    .map(CreateOptionDTO::getValue)
                    .noneMatch(ObjectUtils::isEmpty);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOptionDTO {

        private Boolean valid;

        private String value;
    }
}

