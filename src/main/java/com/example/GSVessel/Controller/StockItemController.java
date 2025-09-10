package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.StockItemDTO;
import com.example.GSVessel.Service.StockItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-items")
@RequiredArgsConstructor
public class StockItemController {

    private final StockItemService stockItemService;

    @PostMapping
    public ResponseEntity<StockItemDTO> create(@RequestBody StockItemDTO dto) {
        return ResponseEntity.ok(stockItemService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockItemDTO> update(@PathVariable Long id, @RequestBody StockItemDTO dto) {
        return ResponseEntity.ok(stockItemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockItemDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockItemService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockItemDTO>> getAll() {
        return ResponseEntity.ok(stockItemService.getAll());
    }
}