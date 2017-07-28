package ru.artemaa.stocks.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|name")
@Table(name = "STOCKS_CURRENCY")
@Entity(name = "stocks$Currency")
public class Currency extends StandardEntity {
    private static final long serialVersionUID = -5687224572426293983L;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 80)
    protected String name;

    @Length(min = 3, max = 3)
    @NotNull
    @Column(name = "CODE", nullable = false, unique = true, length = 3)
    protected String code;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}