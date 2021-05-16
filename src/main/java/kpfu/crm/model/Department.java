package kpfu.crm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Department extends BaseEntity {
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Teacher> teachers;

    @Override
    public String[] ignoreProperties() {
        return new String[]{"teachers"};
    }
}
