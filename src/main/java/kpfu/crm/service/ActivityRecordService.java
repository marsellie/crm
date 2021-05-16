package kpfu.crm.service;

import kpfu.crm.model.ActivityRecord;
import kpfu.crm.model.Teacher;
import kpfu.crm.repo.ActivityRecordRepo;
import org.springframework.stereotype.Service;

@Service
public class ActivityRecordService
        extends BaseChildService<
        Teacher,
        TeacherService,
        ActivityRecord,
        ActivityRecordRepo
        > {
    public ActivityRecordService(TeacherService parentService, ActivityRecordRepo repo) {
        super("activityRecords", parentService, repo);
    }
}
