package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.MantenimientoPreventivoDTO;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Model.MantenimientoPreventivo;
import com.example.GSVessel.Repository.EquipmentRepository;
import com.example.GSVessel.Repository.MantenimientoPreventivoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MantenimientoPreventivoService {

    private final MantenimientoPreventivoRepository mantenimientoPreventivoRepository;
    private final EquipmentRepository equipmentRepository;

    public MantenimientoPreventivoService(
            MantenimientoPreventivoRepository mantenimientoPreventivoRepository,
            EquipmentRepository equipmentRepository) {
        this.mantenimientoPreventivoRepository = mantenimientoPreventivoRepository;
        this.equipmentRepository = equipmentRepository;
    }

    // Convertir entidad a DTO (calculando la alerta)
    private MantenimientoPreventivoDTO convertToDTO(MantenimientoPreventivo mp) {
        Long equipmentId = mp.getEquipment() != null ? mp.getEquipment().getId() : null;
        boolean alerta = mp.isAlerta(); // Calcula la alerta
        return new MantenimientoPreventivoDTO(
                mp.getId(),
                mp.getHorasCambio(),
                mp.getHorasAviso(),
                mp.getFechaUltimoCambio(),
                equipmentId,
                alerta
        );
    }

    // Guardar nuevo mantenimiento preventivo
    public MantenimientoPreventivoDTO saveMantenimientoPreventivo(MantenimientoPreventivoDTO dto) {
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + dto.getEquipmentId()));

        MantenimientoPreventivo mp = new MantenimientoPreventivo();
        mp.setHorasCambio(dto.getHorasCambio());
        mp.setHorasAviso(dto.getHorasAviso());
        mp.setFechaUltimoCambio(dto.getFechaUltimoCambio() != null ? dto.getFechaUltimoCambio() : LocalDate.now());
        mp.setEquipment(equipment);

        MantenimientoPreventivo saved = mantenimientoPreventivoRepository.save(mp);
        return convertToDTO(saved);
    }

    // Actualizar mantenimiento preventivo
    public MantenimientoPreventivoDTO updateMantenimientoPreventivo(Long id, MantenimientoPreventivoDTO dto) {
        MantenimientoPreventivo mp = mantenimientoPreventivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento Preventivo no encontrado con id: " + id));

        mp.setHorasCambio(dto.getHorasCambio());
        mp.setHorasAviso(dto.getHorasAviso());

        // Si se llama a actualizar, posiblemente sea para reiniciar o cambiar fechas
        if (dto.getFechaUltimoCambio() != null) {
            mp.setFechaUltimoCambio(dto.getFechaUltimoCambio());
        }

        // Opcional: Actualizar equipo si cambia
        if (dto.getEquipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + dto.getEquipmentId()));
            mp.setEquipment(equipment);
        }

        MantenimientoPreventivo updated = mantenimientoPreventivoRepository.save(mp);
        return convertToDTO(updated);
    }

    // Obtener por ID
    public MantenimientoPreventivoDTO getMantenimientoPreventivoById(Long id) {
        MantenimientoPreventivo mp = mantenimientoPreventivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento Preventivo no encontrado con id: " + id));
        return convertToDTO(mp);
    }

    // Listar todos
    public List<MantenimientoPreventivoDTO> getAllMantenimientosPreventivos() {
        return mantenimientoPreventivoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Listar por ID del equipo
    public List<MantenimientoPreventivoDTO> getByEquipmentId(Long equipmentId) {
        return mantenimientoPreventivoRepository.findByEquipmentId(equipmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Listar por ID del barco
    public List<MantenimientoPreventivoDTO> getByShipId(Long shipId) {
        return mantenimientoPreventivoRepository.findByShipId(shipId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Eliminar
    public void deleteMantenimientoPreventivo(Long id) {
        MantenimientoPreventivo mp = mantenimientoPreventivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento Preventivo no encontrado con id: " + id));
        mantenimientoPreventivoRepository.delete(mp);
    }

    // Método para reiniciar horas de uso y actualizar fecha
    public MantenimientoPreventivoDTO reiniciarHoras(Long id) {
        MantenimientoPreventivo mp = mantenimientoPreventivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento Preventivo no encontrado con id: " + id));

        // Reiniciar las horas usadas del equipo asociado a 0
        Equipment equipment = mp.getEquipment();
        equipment.setHoursUsed(0);
        equipmentRepository.save(equipment); // Actualiza el equipo

        // Actualizar la fecha del último cambio a hoy
        mp.setFechaUltimoCambio(LocalDate.now());
        MantenimientoPreventivo updated = mantenimientoPreventivoRepository.save(mp);

        return convertToDTO(updated);
    }

    // Método para sumar horas al equipo asociado
    public Equipment sumarHorasAEquipo(Long equipmentId, int horasASumar) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + equipmentId));

        int nuevasHoras = equipment.getHoursUsed() + horasASumar;
        equipment.setHoursUsed(nuevasHoras);
        return equipmentRepository.save(equipment);
    }
}