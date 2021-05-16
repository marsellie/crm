package kpfu.crm.controller;

import kpfu.crm.model.Department;
import kpfu.crm.service.DepartmentService;
import kpfu.crm.util.Endpoints;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.DEPARTMENT)
public class DepartmentController extends BaseParentController<Department, DepartmentService> {
    public DepartmentController(DepartmentService service) {
        super(service);
    }
}