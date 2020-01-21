package com.blackwhite.db;

import javax.persistence.Column;
import javax.persistence.Id;

public class Payment {
    private int id;
    private int paymentMethod;
    private int paymentStatus;
    private int amount;
    private String systemId;
    private PaymentStatus payStatus;
    private PaymentMethod payMethod;

    @Id
    @Column(name="id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="payment_method_id")
    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Column(name="payment_status_id")
    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Column(name="amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Column(name="payment_system_id")
    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public PaymentStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PaymentStatus payStatus) {
        this.payStatus = payStatus;
    }

    public PaymentMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PaymentMethod payMethod) {
        this.payMethod = payMethod;
    }

    @Override
    public String toString() {
        return payStatus.getName() + ": " + systemId;
    }
}
