package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot=new ParkingLot(name,address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);
        if(numberOfWheels<=2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }else if(numberOfWheels<=4){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            spot.setSpotType(SpotType.OTHERS);
        }
        spot.setOccupied(false);
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();

        spotList.add(spot);
        spot.setParkingLot(parkingLot);
        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);

        return  spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot = null;
        if(spotRepository1.findById(spotId).get() != null) {
           spot = spotRepository1.findById(spotId).get();
            spot.setPricePerHour(pricePerHour);
            ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
            List<Spot> spotList = parkingLot.getSpotList();
            if (spotList == null) {
                spotList = new ArrayList<>();
                spotList.add(spot);
            }
            for (Spot spots : spotList) {
                if (spots.getId() == spotId) {
                    spots.setPricePerHour(pricePerHour);
                }
            }
            spot.setParkingLot(parkingLot);
            parkingLotRepository1.save(parkingLot);
            spotRepository1.save(spot);

        }
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

            if(parkingLotRepository1.findById(parkingLotId).get()!= null) {
                ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
                List<Spot> spotList = parkingLot.getSpotList();
                for (Spot spot : spotList) {
                    deleteSpot(spot.getId());
                }
                parkingLotRepository1.deleteById(parkingLotId);
            }
    }
}
