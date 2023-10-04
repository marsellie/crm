package kpfu.crm.controller;

import jakarta.validation.Valid;
import kpfu.crm.config.security.UserDetailImpl;
import kpfu.crm.model.test.dto.CreateResultDTO;
import kpfu.crm.service.ResultService;
import kpfu.crm.util.Endpoints;
import kpfu.crm.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static kpfu.crm.util.ResponseFactory.ok;


@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.RESULT)
public class ResultController {

    private final ResultService resultService;

    @GetMapping("{id}")
    public ResponseEntity<?> getResults(
            @AuthenticationPrincipal UserDetailImpl user,
            @PathVariable("id") UUID testId
    ) {
        return ok(resultService.findAll(testId, user));
    }

    @PostMapping("{id}")
    public ResponseEntity<?> postResult(
            @AuthenticationPrincipal UserDetailImpl user,
            @PathVariable("id") UUID testId,
            @RequestBody @Valid CreateResultDTO dto
    ) {
        resultService.createResult(user, testId, dto);
        return ok();
    }
}
