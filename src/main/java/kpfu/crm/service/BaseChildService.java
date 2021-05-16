package kpfu.crm.service;

import kpfu.crm.exceptions.RestException;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.repo.BaseRepo;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

public abstract class BaseChildService<
        Parent extends BaseEntity,
        ParentService extends BaseParentService<Parent, ? extends BaseRepo<Parent>>,
        T extends BaseEntity,
        TRepo extends BaseRepo<T>
        > extends BaseParentService<T, TRepo> {
    protected final String parentFieldName;
    protected final ParentService parentService;

    public BaseChildService(String parentFieldName, ParentService parentService, TRepo repo) {
        super(repo);
        this.parentService = parentService;
        this.parentFieldName = parentFieldName;
    }

    public T save(long parentId, T t) throws RestException {
        Parent parent = parentService.get(parentId);

        getByParent(parent).add(t);
        parentService.save(parent);
        return t;
    }

    private Collection<T> getByParent(Parent parent) throws RestException {
        Field field = ReflectionUtils.findField(parent.getClass(), parentFieldName);
        if (field == null)
            throw new RestException("Не удалось найти поле <" + parentFieldName + ">");

        field.setAccessible(true);
        if (ReflectionUtils.getField(field, parent) instanceof Collection tCollection)
            return (Collection<T>) tCollection;
        else
            throw new RestException("Неверный тип поля <" + parentFieldName + "> (нужен Collection)");
    }

    public Collection<T> getByParentId(long parentId) throws RestException {
        return getByParent(parentService.get(parentId));
    }

    public T delete(long parentId, long id) throws RestException {
        Parent parent = parentService.get(parentId);
        Collection<T> ts = getByParent(parent);
        Iterator<T> it = ts.iterator();
        T deleted = null;

        while (it.hasNext()){
            T next = it.next();
            if (next.getId() == id) {
                deleted = next;
                ts.remove(next);
                break;
            }
        }

        parentService.save(parent);
        return deleted;
    }
}
