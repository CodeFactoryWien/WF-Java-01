package com.blackwhite.db;

import javax.persistence.*;

@Entity
@Table(name = "PAYMENT_STATUS")
public class PaymentStatus {
    private int id;
    private String name;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NAME")
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
