package kpfu.crm.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import kpfu.crm.config.security.UserDetailImpl;
import kpfu.crm.controller.api.ApiView;
import kpfu.crm.mapper.TestMapper;
import kpfu.crm.model.test.dto.CreateTestDTO;
import kpfu.crm.service.TestService;
import kpfu.crm.util.Endpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static kpfu.crm.util.ResponseFactory.ok;

@RestController
@RequestMapping(Endpoints.TEST)
@RequiredArgsConstructor
public class TestController {

    private final TestMapper mapper;
    private final TestService testService;

    @GetMapping
    @JsonView(ApiView.ListView.class)
    public ResponseEntity<?> getAll() {
        return ok(testService.findBy(Pageable.unpaged())
                .map(mapper::toDto)
                .toList()
        );
    }

    @GetMapping("{id}")
    @JsonView(ApiView.FullView.class)
    public ResponseEntity<?> getById(@PathVariable("id") UUID id) {
        return ok(testService.getViewById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserDetailImpl user,
            @RequestBody @Valid CreateTestDTO dto
    ) {
        testService.create(user, dto);
        return ok();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal UserDetailImpl user,
            @PathVariable("id") UUID id,
            @RequestBody @Valid CreateTestDTO dto
    ) {
        testService.update(id, user, dto);
        return ok();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal UserDetailImpl user,
            @PathVariable("id") UUID id
    ) {
        testService.delete(id, user);
        return ok();
    }
}
