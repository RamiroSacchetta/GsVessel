package com.example.GSVessel.Mapper;

import com.example.GSVessel.DTO.TripDTO;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Model.Trip;

public class TripMapper {

    // Convertir entidad a DTO
    public static TripDTO toDTO(Trip trip) {
        if (trip == null) return null;

        return TripDTO.builder()
                .id(trip.getId())
                .departureDate(trip.getDepartureDate())
                .returnDate(trip.getReturnDate())
                .otherExpenses(trip.getOtherExpenses())
                .shipId(trip.getShip() != null ? trip.getShip().getId() : null)
                // Los demás campos del DTO no existen en la entidad, quedan nulos/default
                .foodExpense(null)
                .shacklesExpense(null)
                .cablesExpense(null)
                .combustible(null)
                .aceite(null)
                .filtros(null)
                .combustibleUsed(0)
                .aceiteUsed(0)
                .filtrosUsed(0)
                .shacklesUsed(0)
                .cargoUsed(0)
                .build();
    }

    // Convertir DTO a entidad
    public static Trip toEntity(TripDTO dto, Ship ship) {
        if (dto == null) return null;

        Trip trip = new Trip();
        trip.setId(dto.getId());
        trip.setDepartureDate(dto.getDepartureDate());
        trip.setReturnDate(dto.getReturnDate());
        trip.setOtherExpenses(dto.getOtherExpenses());
        trip.setShip(ship);
        // Los demás campos del DTO no existen en la entidad, se omiten
        return trip;
    }
}