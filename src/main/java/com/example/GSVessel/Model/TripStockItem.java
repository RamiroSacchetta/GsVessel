package com.example.GSVessel.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripStockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación al viaje
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;


    // Relación al stock
    @ManyToOne
    @JoinColumn(name = "stock_item_id", nullable = false)
    private StockItem stockItem;

    private int quantityUsed;   // cuántos se usaron
    private Double totalCost;   // opcional: cantidad * unitCost
}