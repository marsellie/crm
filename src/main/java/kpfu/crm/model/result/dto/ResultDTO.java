package kpfu.crm.model.result.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO {

    private UUID id;
    private String user;
    private Integer total;
    private Boolean finished;
    private Instant createdAt;
    private List<AnswerDTO> answers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDTO {

        private UUID id;
        private String text;
        private String total;
        private UUID optionId;
        private List<UUID> optionIds;
    }
}
