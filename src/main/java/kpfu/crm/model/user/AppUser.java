package kpfu.crm.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.model.result.Result;
import kpfu.crm.model.test.Test;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "app_user")
@Getter
@Setter
public class AppUser extends BaseEntity {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Enumerated(value = STRING)
    @ElementCollection(targetClass = Role.class, fetch = EAGER)
    private Set<Role> roles;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true, mappedBy = "creator")
    private Collection<Test> tests;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true, mappedBy = "appUser")
    private Collection<Result> results;
}
