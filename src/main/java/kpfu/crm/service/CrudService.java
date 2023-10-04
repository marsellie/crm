package kpfu.crm.service;

import kpfu.crm.exception.RestException;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.repo.BaseRepo;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public abstract class CrudService<T extends BaseEntity> {

    @Setter(onMethod_ = @Autowired)
    private BaseRepo<T> repo;

    public Page<T> findBy(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public List<T> findAll() {
        return repo.findAll();
    }

    public T getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> {
                    String formatted = "Не удалось найти сущность c ID <%s>".formatted(id);
                    return new RestException(formatted);
                });
    }

    public T save(T t) {
        return repo.save(t);
    }
}
