package kpfu.crm.service;

import kpfu.crm.model.user.AppUser;
import kpfu.crm.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService extends CrudService<AppUser> {

    private final UserRepo userRepo;

    public Optional<AppUser> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
