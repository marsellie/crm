//package kpfu.crm;
//
//import kpfu.crm.exceptions.RestException;
//import kpfu.crm.model.BaseEntity;
//import kpfu.crm.service.BaseParentService;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class ServiceTests {
//
//    @Autowired
//    private DepartmentService departmentService;
//    @Autowired
//    private TeacherService teacherService;
//    @Autowired
//    private ActivityRecordService activityRecordService;
//
//    private static Department department;
//    private static Teacher teacher;
//    private static ActivityRecord activityRecord;
//
//    private void parentCreation(BaseEntity baseEntity, BaseParentService service) {
//    }
//
//    @Test
//    @Order(0)
//    void departmentCreation() {
//        var name = "test";
//
//        department = new Department();
//        department.setName(name);
//
//        var before = departmentService.findAll().size();
//        department = departmentService.save(department);
//        var after = departmentService.findAll().size();
//
//        assertTrue(after > before);
//        assertEquals(department.getName(), name);
//    }
//
//    @Test
//    @Order(1)
//    void departmentEdit() {
//        var newName = "new test";
//        department.setName(newName);
//        department = departmentService.update(department);
//
//        assertEquals(department.getName(), newName);
//    }
//
//    @Test
//    @Order(2)
//    void departmentDeletion() {
//        var before = departmentService.findAll().size();
//        assertDoesNotThrow(() -> departmentService.delete(department.getId()));
//        var after = departmentService.findAll().size();
//
//        assertThrows(RestException.class, () -> departmentService.getById(department.getId()));
//        assertTrue(before > after);
//    }
//
//    @Test
//    @Order(3)
//    void teacherCreation() {
//        var name = "test";
//
//        department = new Department();
//        department.setName(name);
//
//        var before = departmentService.findAll().size();
//        department = departmentService.save(department);
//        var after = departmentService.findAll().size();
//
//        assertTrue(after > before);
//        assertEquals(department.getName(), name);
//    }
//
//    @Test
//    @Order(4)
//    void teacherEdit() {
//        var newName = "new test";
//        department.setName(newName);
//        department = departmentService.update(department);
//
//        assertEquals(department.getName(), newName);
//    }
//
//    @Test
//    @Order(5)
//    void teacherDeletion() {
//        var before = departmentService.findAll().size();
//        assertDoesNotThrow(() -> departmentService.delete(department.getId()));
//        var after = departmentService.findAll().size();
//
//        assertThrows(RestException.class, () -> departmentService.getById(department.getId()));
//        assertTrue(before > after);
//    }
//
//    @Test
//    @Order(6)
//    void activityRecordCreation() {
//        var name = "test";
//
//        department = new Department();
//        department.setName(name);
//
//        var before = departmentService.findAll().size();
//        department = departmentService.save(department);
//        var after = departmentService.findAll().size();
//
//        assertTrue(after > before);
//        assertEquals(department.getName(), name);
//    }
//
//    @Test
//    @Order(7)
//    void activityRecordEdit() {
//        var newName = "new test";
//        department.setName(newName);
//        department = departmentService.update(department);
//
//        assertEquals(department.getName(), newName);
//    }
//
//    @Test
//    @Order(8)
//    void activityRecordDeletion() {
//        var before = departmentService.findAll().size();
//        assertDoesNotThrow(() -> departmentService.delete(department.getId()));
//        var after = departmentService.findAll().size();
//
//        assertThrows(RestException.class, () -> departmentService.getById(department.getId()));
//        assertTrue(before > after);
//    }
//}
