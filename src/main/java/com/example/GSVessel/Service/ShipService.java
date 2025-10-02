package com.example.GSVessel.Service;

import com.example.GSVessel.Exception.BusinessException;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Repository.ShipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipService {

    private final ShipRepository shipRepository;

    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    // Listar todos los barcos
    public List<Ship> getAllShips() {
        List<Ship> ships = shipRepository.findAll();
        if (ships.isEmpty()) {
            throw new ListNoContentException("No se encontraron barcos");
        }
        return ships;
    }

    // Obtener barco por id
    public Ship getShipById(Long id) {
        return shipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con id: " + id));
    }

    // Crear barco
    public Ship saveShip(Ship ship) {
        if (ship.getName() == null || ship.getName().isBlank()) {
            throw new BusinessException("El nombre del barco es obligatorio");
        }
        if (ship.getRegistration() == null || ship.getRegistration().isBlank()) {
            throw new BusinessException("La matrícula del barco es obligatoria");
        }
        return shipRepository.save(ship);
    }

    // Actualizar barco
    public Ship updateShip(Long id, Ship shipDetails) {
        Ship existing = shipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con id: " + id));

        if (shipDetails.getName() != null) existing.setName(shipDetails.getName());
        if (shipDetails.getRegistration() != null) existing.setRegistration(shipDetails.getRegistration());
        existing.setHoursUsed(shipDetails.getHoursUsed());
        existing.setActive(shipDetails.isActive());
        existing.setCrewSize(shipDetails.getCrewSize());
        existing.setCargoCrates(shipDetails.getCargoCrates());
        existing.setNumberOfNets(shipDetails.getNumberOfNets());
        if (shipDetails.getOwner() != null) existing.setOwner(shipDetails.getOwner());

        return shipRepository.save(existing);
    }

    // Eliminar barco
    public void deleteShip(Long id) {
        Ship existing = shipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con id: " + id));
        shipRepository.delete(existing);
    }
}
