package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Payment payment = new Payment();
            if(  PaymentMode.valueOf(mode.toUpperCase()) == null){
                    throw new Exception("Payment mode not detected");
            }
             Reservation reservation = reservationRepository2.findById(reservationId).get();
            int totalAmount = reservation.getSpot().getPricePerHour() * reservation.getNumberOfHours();
            if(totalAmount>amountSent){
                throw new Exception("Insufficient Amount");
            }
            payment.setPaymentMode(PaymentMode.valueOf(mode.toUpperCase()));
            payment.setReservation(reservation);
            payment.setPaymentCompleted(true);
            paymentRepository2.save(payment);
            return  payment;
    }
}
