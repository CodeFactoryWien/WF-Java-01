package com.blackwhite.db;

import javax.persistence.Column;
import javax.persistence.Id;

public class PaymentMethod {
    private int id;
    private String name;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // used to display object in ChoiceBox
    @Override
    public String toString() {
        return name;
    }
}
