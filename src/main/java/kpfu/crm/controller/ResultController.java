package kpfu.crm.controller;

import jakarta.validation.Valid;
import kpfu.crm.config.security.UserDetailImpl;
import kpfu.crm.model.result.dto.UpdateResultDTO;
import kpfu.crm.model.test.dto.CreateResultDTO;
import kpfu.crm.service.ResultService;
import kpfu.crm.util.Endpoints;
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

    @GetMapping
    public ResponseEntity<?> getResults(
            @AuthenticationPrincipal UserDetailImpl user
    ) {
        return ok(resultService.findAll(user));
    }

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

    @PutMapping("{id}")
    public ResponseEntity<?> postResult(
            @AuthenticationPrincipal UserDetailImpl user,
            @PathVariable("id") UUID resultId,
            @RequestBody @Valid UpdateResultDTO dto
    ) {
        resultService.updateResult(user, resultId, dto);
        return ok();
    }
}
