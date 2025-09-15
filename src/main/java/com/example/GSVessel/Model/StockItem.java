package com.example.GSVessel.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "stock_item")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Nombre del item (ej: Combustible, Filtros, Aceite)

    private int totalQuantity;  // Cantidad total registrada
    private int usedQuantity;   // Cantidad ya utilizada
    private Double unitCost;    // Costo unitario

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "stockItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripStockItem> tripUsages = new ArrayList<>();

    // Cantidad disponible calculada
    public int getAvailableQuantity() {
        return totalQuantity - usedQuantity;
    }
}