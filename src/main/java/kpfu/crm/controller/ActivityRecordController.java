package kpfu.crm.controller;

import kpfu.crm.model.ActivityRecord;
import kpfu.crm.service.ActivityRecordService;
import kpfu.crm.util.Endpoints;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.ACTIVITY_RECORD)
public class ActivityRecordController
        extends BaseChildController<
        ActivityRecord,
        ActivityRecordService
        > {
    public ActivityRecordController(ActivityRecordService service) {
        super(service);
    }
}
