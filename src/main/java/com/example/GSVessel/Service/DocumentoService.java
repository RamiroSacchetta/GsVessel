package com.example.GSVessel.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.GSVessel.DTO.DocumentoDTO;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Model.*;
import com.example.GSVessel.Model.Enums.TipoDocumento;
import com.example.GSVessel.Repository.DocumentoRepository;
import com.example.GSVessel.Repository.EquipmentRepository;
import com.example.GSVessel.Repository.MaintenanceRepository;
import com.example.GSVessel.Repository.ShipRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final ShipRepository shipRepository;
    private final EquipmentRepository equipmentRepository;
    private final TripulanteRepository tripulanteRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final Cloudinary cloudinary;

    public DocumentoService(
            DocumentoRepository documentoRepository,
            ShipRepository shipRepository,
            EquipmentRepository equipmentRepository,
            TripulanteRepository tripulanteRepository,
            MaintenanceRepository maintenanceRepository,
            Cloudinary cloudinary) {
        this.documentoRepository = documentoRepository;
        this.shipRepository = shipRepository;
        this.equipmentRepository = equipmentRepository;
        this.tripulanteRepository = tripulanteRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.cloudinary = cloudinary;
    }

    private String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a Cloudinary", e);
        }
    }

    public DocumentoDTO convertToDTO(Documento documento) {
        Long barcoId = documento.getBarco() != null ? documento.getBarco().getId() : null;
        Long equipmentId = documento.getEquipment() != null ? documento.getEquipment().getId() : null;
        Long tripulanteId = documento.getTripulante() != null ? documento.getTripulante().getId() : null;
        Long mantenimientoId = documento.getMantenimiento() != null ? documento.getMantenimiento().getId() : null;

        DocumentoDTO dto = new DocumentoDTO();
        dto.setId(documento.getId());
        dto.setNombre(documento.getNombre());
        dto.setTipo(documento.getTipo());
        dto.setDescripcion(documento.getDescripcion());
        dto.setUrl(documento.getUrl());
        dto.setFechaExpiracion(documento.getFechaExpiracion());
        dto.setFechaSubida(documento.getFechaSubida());
        dto.setBarcoId(barcoId);
        dto.setEquipmentId(equipmentId);
        dto.setTripulanteId(tripulanteId);
        dto.setMantenimientoId(mantenimientoId);
        return dto;
    }

    public DocumentoDTO saveDocumento(DocumentoDTO dto) {
        Documento documento = new Documento();
        documento.setNombre(dto.getNombre());
        documento.setTipo(dto.getTipo());
        documento.setDescripcion(dto.getDescripcion());
        documento.setFechaExpiracion(dto.getFechaExpiracion());
        documento.setFechaSubida(LocalDate.now());

        // Subir archivo
        if (dto.getArchivo() != null && !dto.getArchivo().isEmpty()) {
            String url = uploadFile(dto.getArchivo());
            documento.setUrl(url);
        }

        // Asociar a entidad
        if (dto.getBarcoId() != null) {
            Ship barco = shipRepository.findById(dto.getBarcoId())
                    .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado"));
            documento.setBarco(barco);
        }
        if (dto.getEquipmentId() != null) {
            Equipment equipo = equipmentRepository.findById(dto.getEquipmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));
            documento.setEquipment(equipo);
        }
        if (dto.getTripulanteId() != null) {
            Tripulante tripulante = tripulanteRepository.findById(dto.getTripulanteId())
                    .orElseThrow(() -> new EntityNotFoundException("Tripulante no encontrado"));
            documento.setTripulante(tripulante);
        }
        if (dto.getMantenimientoId() != null) {
            Maintenance mantenimiento = maintenanceRepository.findById(dto.getMantenimientoId())
                    .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado"));
            documento.setMantenimiento(mantenimiento);
        }

        Documento saved = documentoRepository.save(documento);
        return convertToDTO(saved);
    }

    public List<DocumentoDTO> getAllOrderByFechaExpiracionAsc() {
        return documentoRepository.findAllOrderByFechaExpiracionAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentoDTO> getDocumentosPorBarco(Long barcoId) {
        return documentoRepository.findByBarcoIdOrderByFechaExpiracionAsc(barcoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentoDTO> getDocumentosPorEquipo(Long equipmentId) {
        return documentoRepository.findByEquipmentIdOrderByFechaExpiracionAsc(equipmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentoDTO> getDocumentosPorTripulante(Long tripulanteId) {
        return documentoRepository.findByTripulanteIdOrderByFechaExpiracionAsc(tripulanteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentoDTO> getDocumentosPorMantenimiento(Long mantenimientoId) {
        return documentoRepository.findByMantenimientoIdOrderByFechaExpiracionAsc(mantenimientoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DocumentoDTO getDocumentoById(Long id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado"));
        return convertToDTO(documento);
    }

    public void deleteDocumento(Long id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado"));
        documentoRepository.delete(documento);
    }
}