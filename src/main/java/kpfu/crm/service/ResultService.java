package kpfu.crm.service;

import kpfu.crm.config.security.UserDetailImpl;
import kpfu.crm.exception.RestException;
import kpfu.crm.mapper.ResultMapper;
import kpfu.crm.mapper.TestMapper;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.model.dto.CombinedDTO;
import kpfu.crm.model.result.Answer;
import kpfu.crm.model.result.Result;
import kpfu.crm.model.result.dto.ResultDTO;
import kpfu.crm.model.result.dto.UpdateResultDTO;
import kpfu.crm.model.result.dto.UpdateResultDTO.UpdateAnswerDTO;
import kpfu.crm.model.test.Option;
import kpfu.crm.model.test.Question;
import kpfu.crm.model.test.Test;
import kpfu.crm.model.test.dto.CreateResultDTO;
import kpfu.crm.model.test.dto.CreateResultDTO.CreateAnswerDTO;
import kpfu.crm.model.user.AppUser;
import kpfu.crm.repo.ResultRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static kpfu.crm.model.test.Question.Type.ONE;
import static kpfu.crm.model.test.Question.Type.TEXT;
import static kpfu.crm.util.ValidationUtils.*;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepo repo;
    private final ResultMapper mapper;
    private final TestMapper testMapper;
    private final UserService userService;
    private final TestService testService;

    public List<ResultDTO> findAll(UUID testId, UserDetailImpl user) {
        Test test = testService.getById(testId);
        testService.validateUserOwnsTest(user, test);

        return test.getResults().stream()
                .map(this::toDto)
                .toList();
    }

    public List<CombinedDTO> findAll(UserDetailImpl user) {
        List<Result> results = repo.findByAppUser(userService.getById(user.getId()));

        return results.stream()
                .map(r -> new CombinedDTO()
                        .with("test", testMapper.toDto(r.getTest()))
                        .with("result", toDto(r))
                )
                .toList();
    }

    private ResultDTO toDto(Result result) {
        result.getAnswers().sort(comparing(a -> a.getQuestion().getPosition()));

        return mapper.toDto(result);
    }

    public void createResult(UserDetailImpl user, UUID testId, CreateResultDTO dto) {
        Test test = testService.getById(testId);
        AppUser userDb = userService.getById(user.getId());
        List<Answer> answers = getAnswers(test, dto);

        Result result = new Result()
                .setAppUser(userDb)
                .setTest(test)
                .setCreatedAt(Instant.now())
                .setAnswers(answers);

        linkEntities(result);

        if (test.getScored()) {
            setTotal(answers);

            int totalResult = answers.stream()
                    .map(Answer::getTotal)
                    .filter(Objects::nonNull)
                    .mapToInt(a -> a)
                    .sum();

            result.setTotal(totalResult);
        }
        repo.save(result);
    }

    private void setTotal(List<Answer> answers) {
        answers.stream()
                .filter(a -> a.getQuestion().getType() != TEXT)
                .forEach(a -> {
                    Question question = a.getQuestion();

                    if (question.getType() == ONE) {
                        if (a.getOption().getValid()) {
                            a.setTotal(question.getScore());
                        }
                    } else {
                        long selectedValidOptions = a.getOptions().stream()
                                .map(Option::getValid)
                                .filter(b -> b)
                                .count();
                        long allValidOptions = question.getOptions().stream()
                                .filter(Option::getValid)
                                .count();

                        if (allValidOptions == selectedValidOptions) {
                            a.setTotal(question.getScore());
                        }
                    }
                });
    }

    private static void linkEntities(Result result) {
        result.getAnswers()
                .forEach(a -> a.setResult(result));
    }

    private static List<Answer> getAnswers(Test test, CreateResultDTO dto) {
        return dto.getAnswers().stream()
                .map(q -> mapAnswer(test, q))
                .toList();
    }

    private static Answer mapAnswer(Test test, CreateAnswerDTO answerDto) {
        Question question = test.getQuestions().stream()
                .filter(q -> q.getId().equals(answerDto.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = "Не найден ответ с ID = %s".formatted(answerDto.getQuestionId());
                    return new RestException(errorMessage);
                });

        Answer answer = new Answer();

        setAnswer(answer, answerDto, question);
        answer.setQuestion(question);

        return answer;
    }

    private static void setAnswer(Answer answer, CreateAnswerDTO answerDto, Question question) {
        switch (question.getType()) {
            case ONE -> {
                notNull(answerDto.getOptionId(), "Для одиночного выбора необходимо определить хотя бы один выбор");
                answer.setOption(getOptionByAnswer(answerDto.getOptionId(), question.getOptions()));
            }
            case MULTI -> {
                notEmpty(answerDto.getOptionIds(), "Для множественного выбора необходимо определить хотя бы один выбор");
                answer.setOptions(getOptionsByAnswers(answerDto.getOptionIds(), question.getOptions()));
            }
            case TEXT -> {
                notEmpty(answerDto.getText(), "Для текстового вопроса необходимо заполнить ответ");
                answer.setText(answerDto.getText());
            }
        }
    }

    private static List<Option> getOptionsByAnswers(
            Collection<UUID> answerIds,
            Collection<Option> options
    ) {
        List<Option> answers = options.stream()
                .filter(o -> answerIds.contains(o.getId()))
                .toList();

        areEqual(answerIds.size(), answers.size(), "Не удалось определить выбранные значения");
        notEmpty(answers, "Для множественного выбора необходимо определить хотя бы один выбор");

        return answers;
    }

    private static Option getOptionByAnswer(
            UUID answerId,
            Collection<Option> options
    ) {
        return options.stream()
                .filter(o -> answerId.equals(o.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = "Не найден ответ с ID = %s".formatted(answerId);
                    return new RestException(errorMessage);
                });
    }

    public void updateResult(UserDetailImpl user, UUID resultId, UpdateResultDTO dto) {
        Result result = repo.getReferenceById(resultId);
        testService.validateUserOwnsTest(user, result.getTest());

        List<UUID> dtoAnswerIds = dto.getAnswers().stream()
                .map(UpdateAnswerDTO::getId)
                .sorted()
                .toList();

        List<UUID> answerIds = result.getAnswers().stream()
                .map(BaseEntity::getId)
                .sorted()
                .toList();

        areEqual(dtoAnswerIds.size(), answerIds.size(), "Размерность ответов не совпадает");

        for (int i = 0; i < dtoAnswerIds.size(); i++) {
            UUID dtoId = dtoAnswerIds.get(i);
            UUID answerId = answerIds.get(i);

            areEqual(dtoId, answerId, "Идентификаторы ответов не совпадают");
        }

        for (Answer answer : result.getAnswers()) {
            Integer updated = dto.getAnswers().stream()
                    .filter(da -> da.getId().equals(answer.getId()))
                    .findFirst()
                    .orElseThrow()
                    .getScore();

            answer.setTotal(updated);
        }

        result.setTotal(
                result.getAnswers().stream()
                        .filter(a -> a.getTotal() != null)
                        .mapToInt(Answer::getTotal)
                        .sum()
        );

        repo.save(result);
    }
}
