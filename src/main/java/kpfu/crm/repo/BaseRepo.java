package kpfu.crm.repo;

import kpfu.crm.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface BaseRepo<T extends BaseEntity> extends JpaRepository<T, UUID> {
}
