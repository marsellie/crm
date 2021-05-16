package kpfu.crm.service;

import kpfu.crm.model.Department;
import kpfu.crm.model.Teacher;
import kpfu.crm.repo.TeacherRepo;
import org.springframework.stereotype.Service;

@Service
public class TeacherService extends BaseChildService<Department, DepartmentService, Teacher, TeacherRepo> {

    public TeacherService(DepartmentService departmentService, TeacherRepo repo) {
        super("teachers", departmentService, repo);
    }
}
