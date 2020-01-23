package com.blackwhite.db;

import javax.persistence.*;

@Entity
@Table(name = "TYPE")
public class RoomType {
    private int typeID;
    private int capacity;
    private int price;
    private String equipment;
    private String description;

    @Id
    @Column(name = "ID")
    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    @Column(name = "CAPACITY")
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Column(name = "PRICE")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Column(name = "EQUIPMENT")
    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // used to display object in ChoiceBox
    @Override
    public String toString() {
        return description;
    }
}
