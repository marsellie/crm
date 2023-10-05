package kpfu.crm.model.result;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import kpfu.crm.model.BaseEntity;
import kpfu.crm.model.test.Option;
import kpfu.crm.model.test.Question;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Answer extends BaseEntity {

    @ManyToOne(fetch = EAGER)
    private Result result;

    @ManyToOne(fetch = EAGER)
    private Question question;

    @Min(value = 0, message = "Допустимы значения от 0")
    @Max(value = 100, message = "Допустимы значения до 100")
    private Integer total;

    private String text;

    @ManyToOne(fetch = EAGER)
    private Option option;

    @ManyToMany(fetch = EAGER)
    private Collection<Option> options;
}
