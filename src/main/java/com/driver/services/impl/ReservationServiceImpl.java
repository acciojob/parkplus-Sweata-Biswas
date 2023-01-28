package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Reservation reservation = new Reservation();
           reservation.setNumberOfHours(timeInHours);
        if(userRepository3.findById(userId).get()==null || parkingLotRepository3.findById(parkingLotId).get()==null){
            throw  new Exception("Cannot make reservation");
        }
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        int minimumPrice =Integer.MAX_VALUE;
        Spot getSpot = null;
       List<Spot> spotList = parkingLot.getSpotList();
        for (Spot spot:spotList) {
            boolean availableSpot = false;
            if(numberOfWheels >4 && spot.getSpotType().equals(SpotType.OTHERS)){
               availableSpot = true;
            }else if(numberOfWheels>2 && numberOfWheels<=4 && (spot.getSpotType().equals(SpotType.OTHERS) || spot.getSpotType().equals(SpotType.FOUR_WHEELER))){
                availableSpot = true;
            } else if (numberOfWheels<=2 ){
                availableSpot = true;
            }


            if(!spot.getOccupied() && availableSpot){
                minimumPrice = Math.min(minimumPrice, spot.getPricePerHour());
                getSpot = spot;
            }
        }
        if(getSpot == null){
            throw  new Exception("Cannot make reservation");
        }
        List<Reservation> reservationList =  getSpot.getReservationList();
        if(reservationList== null){
            reservationList= new ArrayList<>();
        }
        reservationList.add(reservation);
        reservation.setSpot(getSpot);
        User user = userRepository3.findById(userId).get();
        reservation.setUser(user);
        List<Reservation> reservationList1 = user.getReservationList();
        if(reservationList1== null){
            reservationList1= new ArrayList<>();
        }
        reservationList1.add(reservation);
        getSpot.setOccupied(true);
        getSpot.setReservationList(reservationList);
        user.setReservationList(reservationList1);
        spotRepository3.save(getSpot);
        userRepository3.save(user);
        reservationRepository3.save(reservation);
        return reservation;
    }
}
