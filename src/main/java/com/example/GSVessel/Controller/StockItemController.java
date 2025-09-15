package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.StockItem;
import com.example.GSVessel.Service.StockItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockItemController {

    private final StockItemService stockItemService;

    @GetMapping
    public List<StockItem> getAll() {
        return stockItemService.findAll();
    }

    @GetMapping("/{id}")
    public StockItem getById(@PathVariable Long id) {
        return stockItemService.findById(id);
    }

    @PostMapping
    public StockItem create(@RequestBody StockItem stockItem) {
        return stockItemService.save(stockItem);
    }

    @PutMapping("/{id}")
    public StockItem update(@PathVariable Long id, @RequestBody StockItem stockItem) {
        return stockItemService.update(id, stockItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}