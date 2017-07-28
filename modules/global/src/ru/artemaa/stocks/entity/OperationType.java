package ru.artemaa.stocks.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum OperationType implements EnumClass<Integer> {

    Purchase(1),
    Sale(2),
    Dividends(3);

    private Integer id;

    OperationType(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static OperationType fromId(Integer id) {
        for (OperationType at : OperationType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}