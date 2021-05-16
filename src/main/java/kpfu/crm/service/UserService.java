package kpfu.crm.service;

import kpfu.crm.model.User;
import kpfu.crm.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseParentService<User, UserRepo> implements UserDetailsService {
    public UserService(UserRepo userRepo) {
        super(userRepo);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Не найден пользователь " + username);
        return user;
    }
}
