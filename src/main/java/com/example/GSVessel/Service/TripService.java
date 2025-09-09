package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.TripDTO;
import com.example.GSVessel.Model.Trip;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Repository.TripRepository;
import com.example.GSVessel.Repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final ShipRepository shipRepository;

    @Autowired
    public TripService(TripRepository tripRepository, ShipRepository shipRepository) {
        this.tripRepository = tripRepository;
        this.shipRepository = shipRepository;
    }

    // Obtener todos los viajes
    public List<TripDTO> getAllTrips() {
        return tripRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Obtener viaje por ID
    public Optional<TripDTO> getTripById(Long id) {
        return tripRepository.findById(id).map(this::toDTO);
    }

    // Crear viaje
    public Optional<TripDTO> createTrip(TripDTO tripDTO) {
        Optional<Ship> shipOpt = shipRepository.findById(tripDTO.getShipId());
        if (shipOpt.isEmpty()) return Optional.empty();

        Trip trip = toEntity(tripDTO, shipOpt.get());
        return Optional.of(toDTO(tripRepository.save(trip)));
    }

    // Actualizar viaje
    public Optional<TripDTO> updateTrip(Long id, TripDTO tripDTO) {
        return tripRepository.findById(id).map(existing -> {
            Optional<Ship> shipOpt = shipRepository.findById(tripDTO.getShipId());
            if (shipOpt.isEmpty()) return null;

            existing.setDepartureDate(tripDTO.getDepartureDate());
            existing.setReturnDate(tripDTO.getReturnDate());
            existing.setFoodExpense(tripDTO.getFoodExpense());
            existing.setShacklesExpense(tripDTO.getShacklesExpense());
            existing.setCablesExpense(tripDTO.getCablesExpense());
            existing.setOtherExpenses(tripDTO.getOtherExpenses());
            existing.setShacklesUsed(tripDTO.getShacklesUsed());
            existing.setCargoUsed(tripDTO.getCargoUsed());
            existing.setShip(shipOpt.get());

            return toDTO(tripRepository.save(existing));
        });
    }

    // Eliminar viaje
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }

    // Helpers
    private TripDTO toDTO(Trip trip) {
        return TripDTO.builder()
                .id(trip.getId())
                .departureDate(trip.getDepartureDate())
                .returnDate(trip.getReturnDate())
                .foodExpense(trip.getFoodExpense())
                .shacklesExpense(trip.getShacklesExpense())
                .cablesExpense(trip.getCablesExpense())
                .otherExpenses(trip.getOtherExpenses())
                .shacklesUsed(trip.getShacklesUsed())
                .cargoUsed(trip.getCargoUsed())
                .shipId(trip.getShip().getId())
                .build();
    }

    private Trip toEntity(TripDTO dto, Ship ship) {
        return Trip.builder()
                .id(dto.getId())
                .departureDate(dto.getDepartureDate())
                .returnDate(dto.getReturnDate())
                .foodExpense(dto.getFoodExpense())
                .shacklesExpense(dto.getShacklesExpense())
                .cablesExpense(dto.getCablesExpense())
                .otherExpenses(dto.getOtherExpenses())
                .shacklesUsed(dto.getShacklesUsed())
                .cargoUsed(dto.getCargoUsed())
                .ship(ship)
                .build();
    }
}
