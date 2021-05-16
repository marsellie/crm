package kpfu.crm.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Getter
@Setter
public class ActivityRecord extends BaseEntity {
    private String type;
    private String name;
    private long value;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    @Override
    public String[] ignoreProperties() {
        return new String[0];
    }
}
