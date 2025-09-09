package com.example.GSVessel.Service;

import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Repository.ShipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipService {

    private final ShipRepository shipRepository;

    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    public Optional<Ship> getShipById(Long id) {
        return shipRepository.findById(id);
    }

    public Ship saveShip(Ship ship) {
        return shipRepository.save(ship);
    }

    public Ship updateShip(Long id, Ship shipDetails) {
        return shipRepository.findById(id).map(ship -> {
            ship.setName(shipDetails.getName());
            ship.setRegistration(shipDetails.getRegistration());
            ship.setHoursUsed(shipDetails.getHoursUsed());
            ship.setActive(shipDetails.isActive());
            ship.setCrewSize(shipDetails.getCrewSize());
            ship.setCargoCrates(shipDetails.getCargoCrates());
            ship.setNumberOfNets(shipDetails.getNumberOfNets());
            ship.setOwner(shipDetails.getOwner());
            return shipRepository.save(ship);
        }).orElseThrow(() -> new RuntimeException("Ship not found with id " + id));
    }

    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }
}

