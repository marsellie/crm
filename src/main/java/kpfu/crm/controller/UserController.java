package kpfu.crm.controller;

import jakarta.validation.Valid;
import kpfu.crm.mapper.UserMapper;
import kpfu.crm.model.user.AppUser;
import kpfu.crm.model.user.dto.CreateUserDTO;
import kpfu.crm.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static kpfu.crm.util.Endpoints.USER;
import static kpfu.crm.util.ResponseFactory.ok;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ok(userRepo.findAll().stream()
                .map(userMapper::toDto)
                .toList()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserDTO dto) {
        AppUser user = new AppUser();

        userMapper.toEntity(user, dto);
        Optional.ofNullable(dto.getPassword())
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);

        userRepo.save(user);

        return ok();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") UUID userId,
            @RequestBody @Valid CreateUserDTO dto
    ) {
        AppUser user = userRepo.getReferenceById(userId);

        userMapper.toEntity(user, dto);
        Optional.ofNullable(dto.getPassword())
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);

        userRepo.save(user);

        return ok();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID userId) {
        userRepo.deleteById(userId);
        return ok();
    }
}

