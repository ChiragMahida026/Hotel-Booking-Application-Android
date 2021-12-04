package com.example.hotelbookingsystem;

public class getsetmethodpayment {

    String Payment;
    String PaymentId;
    String emails;
    String name;

    public getsetmethodpayment() {
    }

    public getsetmethodpayment(String payment, String paymentId, String emails, String name) {
        Payment = payment;
        PaymentId = paymentId;
        this.emails = emails;
        this.name = name;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(String paymentId) {
        PaymentId = paymentId;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
