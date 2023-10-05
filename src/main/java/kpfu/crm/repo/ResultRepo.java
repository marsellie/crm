package kpfu.crm.repo;

import jakarta.validation.constraints.NotNull;
import kpfu.crm.model.result.Result;
import kpfu.crm.model.test.Test;
import kpfu.crm.model.user.AppUser;

import java.util.List;

public interface ResultRepo extends BaseRepo<Result> {

    List<Result> findByTest(Test test);
    List<Result> findByAppUser(AppUser appUser);
}
