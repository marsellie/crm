package kpfu.crm.mapper;

import kpfu.crm.model.test.Option;
import kpfu.crm.model.test.Question;
import kpfu.crm.model.test.Test;
import kpfu.crm.model.test.dto.CreateTestDTO;
import kpfu.crm.model.test.dto.CreateTestDTO.CreateOptionDTO;
import kpfu.crm.model.test.dto.CreateTestDTO.CreateQuestionDTO;
import kpfu.crm.model.test.dto.TestDTO;
import kpfu.crm.model.test.dto.TestDTO.OptionDTO;
import kpfu.crm.model.test.dto.TestDTO.QuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = TestMapper.QuestionMapper.class)
public interface TestMapper {

    @Mapping(target = "creatorId", source = "creator.id")
    TestDTO toDto(Test test);

    void toEntity(@MappingTarget Test test, CreateTestDTO dto);

    @Mapper(uses = QuestionMapper.VariantMapper.class)
    interface QuestionMapper {
        QuestionDTO toDto(Question question);

        Question toEntity(CreateQuestionDTO dto);

        @Mapper
        interface VariantMapper {
            OptionDTO toDto(Option option);

            Option toEntity(CreateOptionDTO dto);
        }
    }
}
