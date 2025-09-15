package com.example.GSVessel.Service;

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

    @Transactional
    public Map<String, Object> createTrip(Trip trip) {
        List<String> warnings = new ArrayList<>();
        List<String> stockUpdates = new ArrayList<>();

        // Iterar sobre los ítems usados en el viaje
        for (TripStockItem tripItem : trip.getUsedItems()) {
            StockItem stockItem = stockItemRepository.findById(tripItem.getStockItem().getId())
                    .orElseThrow(() -> new RuntimeException("StockItem no encontrado con id " + tripItem.getStockItem().getId()));

            int requested = tripItem.getQuantityUsed();
            int available = stockItem.getTotalQuantity() - stockItem.getUsedQuantity();

            if (available >= requested) {
                // descontar stock
                stockItem.setUsedQuantity(stockItem.getUsedQuantity() + requested);
                stockItemRepository.save(stockItem);
                stockUpdates.add("Se usaron " + requested + " de " + stockItem.getName() +
                        ". Stock restante: " + (stockItem.getTotalQuantity() - stockItem.getUsedQuantity()));
            } else {
                // no alcanza → se crea igual pero avisamos
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

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public Trip findById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip no encontrado con id " + id));
    }

    public void delete(Long id) {
        tripRepository.deleteById(id);
    }

    public Trip updateReturnDate(Long tripId, LocalDate returnDate) {
        Trip existingTrip = findById(tripId);


        if (returnDate.isBefore(existingTrip.getDepartureDate())) {
            throw new RuntimeException("La fecha de regreso no puede ser anterior a la fecha de salida");
        }

        existingTrip.setReturnDate(returnDate);
        return tripRepository.save(existingTrip);
    }
}