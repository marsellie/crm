package kpfu.crm.mapper;

import kpfu.crm.model.result.Answer;
import kpfu.crm.model.result.Result;
import kpfu.crm.model.result.dto.ResultDTO;
import kpfu.crm.model.result.dto.ResultDTO.AnswerDTO;
import kpfu.crm.model.test.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper(uses = ResultMapper.AnswerMapper.class)
public interface ResultMapper {

    @Mapping(target = "user", source = "appUser.name")
    @Mapping(target = "finished", expression = "java(mapFinished(result))")
    ResultDTO toDto(Result result);

    default Boolean mapFinished(Result result) {
        return result.getAnswers().stream()
                .map(Answer::getTotal)
                .noneMatch(Objects::isNull);
    }

    @Mapper
    interface AnswerMapper {

        @Mapping(target = "optionId", source = "option.id")
        @Mapping(target = "optionIds", expression = "java(mapOptionsToValues(answer.getOptions()))")
        AnswerDTO toDto(Answer answer);

        default List<UUID> mapOptionsToValues(Collection<Option> options) {
            return options.stream()
                    .map(Option::getId)
                    .toList();
        }
    }
}
