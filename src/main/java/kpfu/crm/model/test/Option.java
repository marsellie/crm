package kpfu.crm.model.test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kpfu.crm.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "question_option")
public class Option extends BaseEntity {

    @NotBlank
    @Column(name = "option_value")
    private String value;

    private Boolean valid;

    @NotNull
    @ManyToOne
    private Question question;
}
