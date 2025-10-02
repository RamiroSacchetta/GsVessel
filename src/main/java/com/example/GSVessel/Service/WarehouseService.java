package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.WarehouseDTO;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Warehouse;
import com.example.GSVessel.Repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    // Crear depósito
    public WarehouseDTO create(WarehouseDTO dto) {
        Warehouse warehouse = Warehouse.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .build();
        return toDTO(warehouseRepository.save(warehouse));
    }

    // Actualizar depósito
    public WarehouseDTO update(Long id, WarehouseDTO dto) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con ID: " + id));

        warehouse.setName(dto.getName());
        warehouse.setLocation(dto.getLocation());

        return toDTO(warehouseRepository.save(warehouse));
    }

    // Eliminar depósito
    public void delete(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con ID: " + id));
        warehouseRepository.delete(warehouse);
    }

    // Obtener depósito por ID
    public WarehouseDTO getById(Long id) {
        return warehouseRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con ID: " + id));
    }

    // Listar todos los depósitos
    public List<WarehouseDTO> getAll() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        if (warehouses.isEmpty()) {
            throw new ListNoContentException("No se encontraron depósitos");
        }
        return warehouses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Conversión a DTO
    private WarehouseDTO toDTO(Warehouse warehouse) {
        return WarehouseDTO.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .build();
    }
}