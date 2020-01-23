package com.blackwhite.db;

import java.sql.Date;
import javax.persistence.*;

@Entity
@Table(name = "BOOKING")
public class Booking {
    private int id;
    private int room_id;
    private int payment_id;
    private int price;
    private Date checkin;
    private Date checkout;
    private Room roomBooked;
    private Payment paymentBooked;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "room_id")
    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    @Column(name = "payment_id")
    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    @Column(name = "price")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Column(name = "checkin_date")
    public Date getCheckin() {
        return checkin;
    }

    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    @Column(name = "checkout_date")
    public Date getCheckout() {
        return checkout;
    }

    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    public Room getRoomBooked() {
        return roomBooked;
    }

    public void setRoomBooked(Room roomBooked) {
        this.roomBooked = roomBooked;
    }

    public Payment getPaymentBooked() {
        return paymentBooked;
    }

    public void setPaymentBooked(Payment paymentBooked) {
        this.paymentBooked = paymentBooked;
    }
}
