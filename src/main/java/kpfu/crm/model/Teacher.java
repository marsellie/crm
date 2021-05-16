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
public class Teacher extends BaseEntity {
    private String name;
    private String position;
    private float workingRate;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ActivityRecord> activityRecords;

    @Override
    public String[] ignoreProperties() {
        return new String[]{
                "activityRecords"
        };
    }
}
