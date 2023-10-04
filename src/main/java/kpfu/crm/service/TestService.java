package kpfu.crm.service;

import jakarta.transaction.Transactional;
import kpfu.crm.config.security.UserDetailImpl;
import kpfu.crm.exception.RestException;
import kpfu.crm.mapper.TestMapper;
import kpfu.crm.model.test.Option;
import kpfu.crm.model.test.Question;
import kpfu.crm.model.test.Test;
import kpfu.crm.model.test.dto.CreateTestDTO;
import kpfu.crm.model.test.dto.TestDTO;
import kpfu.crm.repo.TestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static kpfu.crm.model.test.Question.Type.TEXT;
import static kpfu.crm.model.user.Role.ADMIN;
import static kpfu.crm.util.ValidationUtils.isTrue;
import static kpfu.crm.util.ValidationUtils.notNull;

@Service
@RequiredArgsConstructor
public class TestService extends CrudService<Test> {

    private final TestRepo repo;
    private final TestMapper mapper;
    private final UserService userService;

    public TestDTO getViewById(UUID id) {
        return mapper.toDto(getById(id));
    }

    @Transactional
    public void create(UserDetailImpl user, CreateTestDTO dto) {
        Test test = new Test();

        mapper.toEntity(test, dto);
        linkEntities(test);

        test.setCreator(userService.getById(user.getId()));
        test.setCreatedAt(Instant.now());

        if (test.getScored()) {
            test.getQuestions()
                    .forEach(q -> notNull(q.getScore(), "Не указано количество баллов за вопрос"));

            test.getQuestions().stream()
                    .filter(q -> q.getType() != TEXT)
                    .forEach(q -> isTrue(q.getOptions().stream()
                            .anyMatch(Option::getValid), "Необходимо чтобы был хотя бы один правильный ответ")
                    );

            test.setTotal(test.getQuestions().stream()
                    .mapToInt(Question::getScore)
                    .sum()
            );
        } else {
            test.getQuestions().forEach(q -> q.setScore(null));
            test.getQuestions().forEach(q -> q.getOptions().forEach(o -> o.setValid(null)));
        }

        save(test);
    }

    @Transactional
    public void update(UUID id, UserDetailImpl user, CreateTestDTO dto) {
        Test test = getById(id);
        validateUserOwnsTest(user, test);

        mapper.toEntity(test, dto);
        linkEntities(test);

        save(test);
    }

    public void delete(UUID id, UserDetailImpl user) {
        Test test = getById(id);

        validateUserOwnsTest(user, test);

        repo.delete(test);
    }

    public void validateUserOwnsTest(UserDetailImpl user, Test test) {
        if (!test.getCreator().getId().equals(user.getId())
                && user.getAuthorities()
                .stream().noneMatch(a -> a.getAuthority().equals(ADMIN.name()))) {
            throw new RestException("Нет доступа для редактирования данного теста");
        }
    }

    private void linkEntities(Test test) {
        test.getQuestions()
                .forEach(q -> q.setTest(test));
        test.getQuestions()
                .forEach(q -> {
                    if (q.getType() == TEXT) {
                        q.setOptions(emptyList());
                    } else {
                        q.getOptions().forEach(o -> o.setQuestion(q));
                    }
                });
    }
}
