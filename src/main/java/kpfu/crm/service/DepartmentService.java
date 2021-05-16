package kpfu.crm.service;

import kpfu.crm.model.Department;
import kpfu.crm.repo.DepartmentRepo;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends BaseParentService<Department, DepartmentRepo> {
    public DepartmentService(DepartmentRepo departmentRepo) {
        super(departmentRepo);
    }
}
