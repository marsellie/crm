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

@Mapper(uses = ResultMapper.AnswerMapper.class)
public interface ResultMapper {

    @Mapping(target = "user", source = "appUser.name")
    ResultDTO toDto(Result result);

    @Mapper
    interface AnswerMapper {

        @Mapping(target = "option", source = "option.value")
        @Mapping(target = "options", expression = "java(mapOptionsToValues(answer.getOptions()))")
        AnswerDTO toDto(Answer answer);

        default List<String> mapOptionsToValues(Collection<Option> options) {
            return options.stream()
                    .map(Option::getValue)
                    .toList();
        }
    }
}
