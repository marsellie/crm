package kpfu.crm.model.test.dto;

import com.fasterxml.jackson.annotation.JsonView;
import kpfu.crm.controller.api.ApiView;
import kpfu.crm.model.test.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDTO {

    @JsonView(ApiView.ListView.class)
    private UUID id;

    @JsonView(ApiView.ListView.class)
    private UUID creatorId;

    @JsonView(ApiView.ListView.class)
    private Boolean scored;

    @JsonView(ApiView.ListView.class)
    private Integer total;

    @JsonView(ApiView.ListView.class)
    private String name;

    @JsonView(ApiView.FullView.class)
    private Collection<QuestionDTO> questions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDTO {

        @JsonView(ApiView.FullView.class)
        private UUID id;

        @JsonView(ApiView.FullView.class)
        private String value;

        @JsonView(ApiView.FullView.class)
        private Integer position;

        @JsonView(ApiView.FullView.class)
        private Integer score;

        @JsonView(ApiView.FullView.class)
        private Question.Type type;

        @JsonView(ApiView.FullView.class)
        private Collection<OptionDTO> options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionDTO {

        @JsonView(ApiView.FullView.class)
        private UUID id;

        @JsonView(ApiView.FullView.class)
        private Boolean valid;

        @JsonView(ApiView.FullView.class)
        private String value;
    }
}

