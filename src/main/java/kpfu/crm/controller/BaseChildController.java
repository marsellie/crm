package kpfu.crm.controller;

import kpfu.crm.exceptions.RestException;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.repo.BaseRepo;
import kpfu.crm.service.BaseChildService;
import kpfu.crm.util.ResponseFactory;
import kpfu.crm.util.RestResponse;
import org.springframework.web.bind.annotation.*;

public abstract class BaseChildController<
        T extends BaseEntity,
        TService extends BaseChildService<?, ?, T, ? extends BaseRepo<T>>
        >
        extends BaseParentController<T, TService> {
    public BaseChildController(TService service) {
        super(service);
    }

    @PostMapping("{id}")
    public RestResponse create(
            @PathVariable("id") long parentId,
            @RequestBody T t
    ) throws RestException {
        return ResponseFactory.ok(service.save(parentId, t));
    }

    @GetMapping("parent/{id}")
    public RestResponse getByParent(
            @PathVariable("id") long parentId
    ) throws RestException {
        return ResponseFactory.ok(service.getByParentId(parentId));
    }

    @DeleteMapping("{parentId}/{id}")
    public RestResponse delete(
            @PathVariable("id") long id,
            @PathVariable("parentId") long parentId
    ) throws RestException {
        return ResponseFactory.ok(service.delete(parentId, id));
    }
}
