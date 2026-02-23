package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.ShipDTO;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.Repository.BarcoRepository;
import com.example.GSVessel.Repository.ShipRepository;
import com.example.GSVessel.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    private User getCurrentUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    private User getScopeOwner(User currentUser) {
        return currentUser.getOwner() != null ? currentUser.getOwner() : currentUser;
    }

    public List<Ship> getAllShips(String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        List<Ship> ships = shipRepository.findAllByBarcoOwner(scopeOwner);
        if (ships.isEmpty()) {
            throw new ListNoContentException("No se encontraron ships");
        }
        return ships;
    }

    public Ship getShipById(Long id, String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        return shipRepository.findByIdAndBarcoOwner(id, scopeOwner)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "No tienes permiso para ver este ship"));
    }

    // Mejor opción: crear por barcoId (no por nombre)
    public Ship createShip(ShipDTO shipDTO, String userEmail) {
        User current = getCurrentUserByEmail(userEmail);
        User scopeOwner = getScopeOwner(current);

        if (shipDTO.getBarcoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "barcoId es obligatorio");
        }

        Barco barco = barcoRepository.findByIdAndOwner(shipDTO.getBarcoId(), scopeOwner)
                .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado o no pertenece al usuario"));

        Ship ship = new Ship();
        ship.setName(shipDTO.getName());
        ship.setRegistration(shipDTO.getRegistration());
        ship.setActive(shipDTO.isActive());
        ship.setCrewSize(shipDTO.getCrewSize());
        ship.setBarco(barco);

        return shipRepository.save(ship);
    }

    public Ship updateShip(Long id, Ship shipDetails, String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        Ship existing = shipRepository.findByIdAndBarcoOwner(id, scopeOwner)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "No tienes permiso para editar este ship"));

        if (shipDetails.getName() != null) existing.setName(shipDetails.getName());
        if (shipDetails.getRegistration() != null) existing.setRegistration(shipDetails.getRegistration());
        existing.setActive(shipDetails.isActive());
        existing.setCrewSize(shipDetails.getCrewSize());

        if (shipDetails.getBarco() != null && shipDetails.getBarco().getId() != null) {
            Barco newBarco = barcoRepository.findByIdAndOwner(shipDetails.getBarco().getId(), scopeOwner)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.FORBIDDEN, "No puedes asignar este ship a un barco de otro usuario"));
            existing.setBarco(newBarco);
        }

        return shipRepository.save(existing);
    }

    public void deleteShip(Long id, String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        Ship existing = shipRepository.findByIdAndBarcoOwner(id, scopeOwner)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este ship"));

        shipRepository.delete(existing);
    }
}
