package kpfu.crm.repo;

import kpfu.crm.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepo<T extends BaseEntity> extends JpaRepository<T, Long> {
}
