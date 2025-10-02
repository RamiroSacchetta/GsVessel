package com.example.GSVessel.Service;

import com.example.GSVessel.Exception.BusinessException;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.StockItem;
import com.example.GSVessel.Repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockItemService {

    private final StockItemRepository stockItemRepository;

    // Listar todos los stock items
    public List<StockItem> findAll() {
        List<StockItem> items = stockItemRepository.findAll();
        if (items.isEmpty()) {
            throw new ListNoContentException("No se encontraron ítems de stock");
        }
        return items;
    }

    // Obtener por ID
    public StockItem findById(Long id) {
        return stockItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockItem no encontrado con id " + id));
    }

    // Guardar nuevo stock item
    public StockItem save(StockItem stockItem) {
        if (stockItem.getName() == null || stockItem.getName().isBlank()) {
            throw new BusinessException("El nombre del item es obligatorio");
        }
        if (stockItem.getTotalQuantity() < 0) {
            throw new BusinessException("La cantidad total debe ser mayor o igual a 0");
        }
        if (stockItem.getUnitCost() < 0) {
            throw new BusinessException("El costo unitario debe ser mayor o igual a 0");
        }
        return stockItemRepository.save(stockItem);
    }

    // Actualizar stock item
    public StockItem update(Long id, StockItem stockItem) {
        StockItem existing = findById(id);

        if (stockItem.getName() != null && !stockItem.getName().isBlank()) {
            existing.setName(stockItem.getName());
        }
        if (stockItem.getTotalQuantity() >= 0) {
            existing.setTotalQuantity(stockItem.getTotalQuantity());
        }
        if (stockItem.getUnitCost() >= 0) {
            existing.setUnitCost(stockItem.getUnitCost());
        }
        if (stockItem.getWarehouse() != null) {
            existing.setWarehouse(stockItem.getWarehouse());
        }

        return stockItemRepository.save(existing);
    }

    // Eliminar stock item
    public void delete(Long id) {
        StockItem existing = stockItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockItem no encontrado con id " + id));
        stockItemRepository.delete(existing);
    }
}
