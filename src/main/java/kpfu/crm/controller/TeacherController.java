package kpfu.crm.controller;

import kpfu.crm.model.Teacher;
import kpfu.crm.service.TeacherService;
import kpfu.crm.util.Endpoints;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.TEACHER)
public class TeacherController extends BaseChildController<Teacher, TeacherService> {
    public TeacherController(TeacherService service) {
        super(service);
    }
}
