package kpfu.crm.repo;

import kpfu.crm.model.result.Result;
import kpfu.crm.model.test.Test;

import java.util.List;

public interface ResultRepo extends BaseRepo<Result> {

    List<Result> findByTest(Test test);
}
