package kpfu.crm.controller;

import kpfu.crm.exceptions.RestException;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.repo.BaseRepo;
import kpfu.crm.service.BaseParentService;
import kpfu.crm.util.ResponseFactory;
import kpfu.crm.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
public abstract class BaseParentController<
        T extends BaseEntity,
        TService extends BaseParentService<T, ? extends BaseRepo<T>>
        > {
    protected final TService service;

    @GetMapping
    public RestResponse get() {
        return ResponseFactory.ok(service.get());
    }

    @GetMapping("{id}")
    public RestResponse get(
            @PathVariable long id
    ) throws RestException {
        return ResponseFactory.ok(service.get(id));
    }

    @PostMapping
    public RestResponse create(
            @RequestBody T t
    ) throws RestException {
        return ResponseFactory.ok(service.save(t));
    }

    @PutMapping
    public RestResponse update(
            @RequestBody T t
    ) throws RestException {
        return ResponseFactory.ok(service.update(t));
    }

    @DeleteMapping("{id}")
    public RestResponse delete(
            @PathVariable("id") long id
    ) throws RestException {
        return ResponseFactory.ok(service.delete(id));
    }
}
