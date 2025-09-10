package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.StockItemDTO;
import com.example.GSVessel.Model.StockItem;
import com.example.GSVessel.Model.Warehouse;
import com.example.GSVessel.Repository.StockItemRepository;
import com.example.GSVessel.Repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockItemService {

    private final StockItemRepository stockItemRepository;
    private final WarehouseRepository warehouseRepository;

    public StockItemDTO create(StockItemDTO dto) {
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con ID: " + dto.getWarehouseId()));

        StockItem item = StockItem.builder()
                .name(dto.getName())
                .totalQuantity(dto.getTotalQuantity())
                .usedQuantity(dto.getUsedQuantity())
                .unitCost(dto.getUnitCost())
                .warehouse(warehouse)
                .build();

        return toDTO(stockItemRepository.save(item));
    }

    public StockItemDTO update(Long id, StockItemDTO dto) {
        StockItem item = stockItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockItem no encontrado con ID: " + id));

        item.setName(dto.getName());
        item.setTotalQuantity(dto.getTotalQuantity());
        item.setUsedQuantity(dto.getUsedQuantity());
        item.setUnitCost(dto.getUnitCost());

        if (!item.getWarehouse().getId().equals(dto.getWarehouseId())) {
            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con ID: " + dto.getWarehouseId()));
            item.setWarehouse(warehouse);
        }

        return toDTO(stockItemRepository.save(item));
    }

    public void delete(Long id) {
        StockItem item = stockItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockItem no encontrado con ID: " + id));
        stockItemRepository.delete(item);
    }

    public StockItemDTO getById(Long id) {
        return stockItemRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("StockItem no encontrado con ID: " + id));
    }

    public List<StockItemDTO> getAll() {
        return stockItemRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private StockItemDTO toDTO(StockItem item) {
        return StockItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .totalQuantity(item.getTotalQuantity())
                .usedQuantity(item.getUsedQuantity())
                .unitCost(item.getUnitCost())
                .warehouseId(item.getWarehouse().getId())
                .build();
    }
}