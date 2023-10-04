package kpfu.crm.model.test.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateResultDTO {

    @Valid
    @NotEmpty(message = "В результате должен быть хотя бы один ответ")
    private List<CreateAnswerDTO> answers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAnswerDTO {

        @NotNull(message = "Идентификатор вопроса не должен быть пустым")
        private UUID questionId;

        private String text;
        private UUID optionId;
        private Collection<UUID> optionIds;
    }
}
