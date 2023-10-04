package kpfu.crm.model.test;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kpfu.crm.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Option extends BaseEntity {

    @NotBlank
    private String value;

    private Boolean valid;

    @NotNull
    @ManyToOne
    private Question question;
}
