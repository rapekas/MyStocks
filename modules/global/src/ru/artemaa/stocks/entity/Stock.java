package ru.artemaa.stocks.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import org.hibernate.validator.constraints.Length;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s (%s)|name,code")
@Table(name = "STOCKS_STOCK")
@Entity(name = "stocks$Stock")
public class Stock extends StandardEntity {
    private static final long serialVersionUID = 47389431230851613L;

    @Length(min = 3, max = 80)
    @NotNull
    @Column(name = "NAME", nullable = false, unique = true, length = 80)
    protected String name;

    @Length(min = 1, max = 20)
    @NotNull
    @Column(name = "CODE", nullable = false, unique = true, length = 20)
    protected String code;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup"})
    @NotNull
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURRENCY_ID")
    protected Currency currency;

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }


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