package com.example.GSVessel.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockItemDTO {
    private Long id;
    private String name;
    private int totalQuantity;
    private int usedQuantity;
    private Double unitCost;
    private Long warehouseId; // relación con depósito
}