package kpfu.crm.model.result;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    private Integer total;

    private String text;

    @ManyToOne(fetch = EAGER)
    private Option option;

    @ManyToMany(fetch = EAGER)
    private Collection<Option> options;
}
