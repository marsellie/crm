package kpfu.crm.repo;

import kpfu.crm.model.user.AppUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends BaseRepo<AppUser> {

    Optional<AppUser> findByUsername(String username);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "delete from usr where id = :id"
    )
    void unsafeDelete(UUID id);
}
