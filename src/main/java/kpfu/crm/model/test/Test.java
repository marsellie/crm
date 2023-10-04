package kpfu.crm.model.test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.model.result.Result;
import kpfu.crm.model.user.AppUser;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Collection;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Test extends BaseEntity {

    @NotBlank
    private String name;

    @NotNull
    private Boolean scored;

    private Integer total;

    @NotNull
    private Instant createdAt;

    @NotNull
    @ManyToOne(fetch = EAGER)
    private AppUser creator;

    @NotEmpty
    @OneToMany(mappedBy = "test", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Question> questions;

    @OneToMany(mappedBy = "test", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Result> results;
}
