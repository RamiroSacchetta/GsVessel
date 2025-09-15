package com.example.GSVessel.Service;

import com.example.GSVessel.Model.StockItem;
import com.example.GSVessel.Repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockItemService {

    private final StockItemRepository stockItemRepository;

    public List<StockItem> findAll() {
        return stockItemRepository.findAll();
    }

    public StockItem findById(Long id) {
        return stockItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockItem no encontrado con id " + id));
    }

    public StockItem save(StockItem stockItem) {
        return stockItemRepository.save(stockItem);
    }

    public StockItem update(Long id, StockItem stockItem) {
        StockItem existing = findById(id);
        existing.setName(stockItem.getName());
        existing.setTotalQuantity(stockItem.getTotalQuantity());
        existing.setUnitCost(stockItem.getUnitCost());
        existing.setWarehouse(stockItem.getWarehouse());
        return stockItemRepository.save(existing);
    }

    public void delete(Long id) {
        stockItemRepository.deleteById(id);
    }
}