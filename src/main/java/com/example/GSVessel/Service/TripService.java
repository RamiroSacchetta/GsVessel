package com.example.GSVessel.Service;

import com.example.GSVessel.Exception.BusinessException;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.StockItem;
import com.example.GSVessel.Model.Trip;
import com.example.GSVessel.Model.TripStockItem;
import com.example.GSVessel.Repository.StockItemRepository;
import com.example.GSVessel.Repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final StockItemRepository stockItemRepository;

    // Crear viaje y actualizar stock
    @Transactional
    public Map<String, Object> createTrip(Trip trip) {
        List<String> warnings = new ArrayList<>();
        List<String> stockUpdates = new ArrayList<>();

        for (TripStockItem tripItem : trip.getUsedItems()) {
            StockItem stockItem = stockItemRepository.findById(tripItem.getStockItem().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "StockItem no encontrado con id: " + tripItem.getStockItem().getId()));

            int requested = tripItem.getQuantityUsed();
            int available = stockItem.getTotalQuantity() - stockItem.getUsedQuantity();

            if (available >= requested) {
                stockItem.setUsedQuantity(stockItem.getUsedQuantity() + requested);
                stockItemRepository.save(stockItem);
                stockUpdates.add("Se usaron " + requested + " de " + stockItem.getName() +
                        ". Stock restante: " + (stockItem.getTotalQuantity() - stockItem.getUsedQuantity()));
            } else {
                warnings.add("No hay stock suficiente de: " + stockItem.getName() +
                        " (pedido " + requested + ", disponible " + available + ")");
            }
        }

        Trip savedTrip = tripRepository.save(trip);

        Map<String, Object> response = new HashMap<>();
        response.put("tripId", savedTrip.getId());
        response.put("stockUpdates", stockUpdates);
        response.put("warnings", warnings);

        return response;
    }

    // Listar todos los viajes
    public List<Trip> findAll() {
        List<Trip> trips = tripRepository.findAll();
        if (trips.isEmpty()) {
            throw new ListNoContentException("No se encontraron viajes");
        }
        return trips;
    }

    // Buscar viaje por id
    public Trip findById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip no encontrado con id: " + id));
    }

    // Eliminar viaje
    public void delete(Long id) {
        Trip existing = findById(id); // <-- lanza EntityNotFoundException si no existe
        tripRepository.delete(existing);
    }

    // Actualizar fecha de regreso
    public Trip updateReturnDate(Long tripId, LocalDate returnDate) {
        Trip existingTrip = findById(tripId);

        if (returnDate.isBefore(existingTrip.getDepartureDate())) {
            throw new BusinessException("La fecha de regreso no puede ser anterior a la fecha de salida");
        }

        existingTrip.setReturnDate(returnDate);
        return tripRepository.save(existingTrip);
    }
}
