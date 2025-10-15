package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.ShipDTO;
import com.example.GSVessel.Exception.BusinessException;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.Repository.BarcoRepository;
import com.example.GSVessel.Repository.ShipRepository;
import com.example.GSVessel.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipService {

    private final ShipRepository shipRepository;
    private final BarcoRepository barcoRepository;
    private final UserRepository userRepository;

    public ShipService(ShipRepository shipRepository,
                       BarcoRepository barcoRepository,
                       UserRepository userRepository) {
        this.shipRepository = shipRepository;
        this.barcoRepository = barcoRepository;
        this.userRepository = userRepository;
    }

    // Listar todos los ships
    public List<Ship> getAllShips() {
        List<Ship> ships = shipRepository.findAll();
        if (ships.isEmpty()) {
            throw new ListNoContentException("No se encontraron ships");
        }
        return ships;
    }

    // Obtener ship por id
    public Ship getShipById(Long id) {
        return shipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ship no encontrado con id: " + id));
    }

    // Crear ship y asignarlo a un barco existente por nombre
    public Ship createShip(ShipDTO shipDTO, String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Barco barco = barcoRepository.findByOwnerAndNombre(user, shipDTO.getBarcoNombre())
                .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con ese nombre"));

        Ship ship = new Ship();
        ship.setName(shipDTO.getName());
        ship.setRegistration(shipDTO.getRegistration());
        ship.setActive(shipDTO.isActive());
        ship.setCrewSize(shipDTO.getCrewSize());
        ship.setBarco(barco);

        return shipRepository.save(ship);
    }

    // Actualizar ship
    public Ship updateShip(Long id, Ship shipDetails) {
        Ship existing = shipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ship no encontrado con id: " + id));

        if (shipDetails.getName() != null) existing.setName(shipDetails.getName());
        if (shipDetails.getRegistration() != null) existing.setRegistration(shipDetails.getRegistration());
        existing.setActive(shipDetails.isActive());
        existing.setCrewSize(shipDetails.getCrewSize());

        if (shipDetails.getBarco() != null) existing.setBarco(shipDetails.getBarco());

        return shipRepository.save(existing);
    }

    // Eliminar ship
    public void deleteShip(Long id) {
        Ship existing = shipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ship no encontrado con id: " + id));
        shipRepository.delete(existing);
    }
}
