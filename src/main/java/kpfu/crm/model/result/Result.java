package kpfu.crm.model.result;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kpfu.crm.model.user.AppUser;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.model.test.Test;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Result extends BaseEntity {

    @NotNull
    @ManyToOne
    private Test test;

    @NotNull
    @ManyToOne
    private AppUser appUser;

    private Integer total;

    @NotNull
    private Instant createdAt;

    @NotEmpty
    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
}
