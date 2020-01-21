package com.blackwhite.db;

import javax.persistence.*;

@Entity
@Table(name = "ROOM")
public class Room {
    private int roomNumber;
    private int typeId;
    private int size;
    private boolean isAvailable;
    private RoomType type;

    @Id
    @Column(name = "ROOM_NUMBER")
    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Column(name = "TYPE_ID")
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Column(name = "SIZE")
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Column(name = "isAVAILABLE")
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public RoomType getType (){
        return type;
    }

    public void setType (RoomType type) {
        this.type = type;
    }

    // used to display object in a ListView
    @Override
    public String toString() {
        return type.getDescription() +" ("+size+"m²)";
    }
}
