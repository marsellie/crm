package kpfu.crm.repo;

import kpfu.crm.model.User;

public interface UserRepo extends BaseRepo<User> {
    User findByUsername(String username);
}
