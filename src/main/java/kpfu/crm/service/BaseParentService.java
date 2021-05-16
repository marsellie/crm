package kpfu.crm.service;

import kpfu.crm.exceptions.RestException;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.repo.BaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseParentService<T extends BaseEntity, TRepo extends BaseRepo<T>> {
    protected final TRepo repo;

    public List<T> get() {
        return repo.findAll();
    }

    public T get(long id) throws RestException {
        T result = repo.findById(id).orElseThrow(() -> new RestException("Не удалось найти сущность c ID <%d>".formatted(id)));
        return result;
    }

    public T save(T t) {
        return repo.save(t);
    }

    public T update(T newT) throws RestException {
        T tFromDb = get(newT.getId());
        BeanUtils.copyProperties(newT, tFromDb, tFromDb.ignoreProperties());
        return repo.save(tFromDb);
    }

    public T delete(long id) throws RestException {
        T tFromDb = get(id);
        repo.delete(tFromDb);
        return tFromDb;
    }
}
