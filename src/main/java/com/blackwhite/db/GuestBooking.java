package com.blackwhite.db;

import javax.persistence.*;

@Entity
@Table(name = "GUEST_BOOKING")
public class GuestBooking {
    private int bookingID;
    private int guestID;
    private boolean isContactPerson;
    private Booking booking;
    private Guest guest;

    @Id
    @Column(name = "BOOKING_ID")
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    @Id
    @Column(name = "GUEST_ID")
    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    @Column(name = "ISCONTACTPERSON")
    public boolean isContactPerson(){
        return isContactPerson;
    }

    public void setContactPerson(boolean isContactPerson){
        this.isContactPerson = isContactPerson;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    // used to display object in a ListView
    @Override
    public String toString() {
        return "Room "+booking.getRoom_id() +" ("+guest.getFirstName()+" "+ guest.getLastName()+") - max: "+
                booking.getRoomBooked().getType().getCapacity();
    }
}
