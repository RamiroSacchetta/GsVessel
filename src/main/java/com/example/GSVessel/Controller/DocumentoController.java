package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.DocumentoDTO;
import com.example.GSVessel.Service.DocumentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    // Subir documento
    @PostMapping
    public ResponseEntity<DocumentoDTO> uploadDocumento(@ModelAttribute DocumentoDTO dto) {
        DocumentoDTO saved = documentoService.saveDocumento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Listar todos ordenados por fecha de expiración (primero a vencer)
    @GetMapping
    public ResponseEntity<List<DocumentoDTO>> getAllOrderByFechaExpiracion() {
        List<DocumentoDTO> docs = documentoService.getAllOrderByFechaExpiracionAsc();
        return ResponseEntity.ok(docs);
    }

    // Listar por barco
    @GetMapping("/barco/{barcoId}")
    public ResponseEntity<List<DocumentoDTO>> getDocumentosByBarco(@PathVariable Long barcoId) {
        List<DocumentoDTO> docs = documentoService.getDocumentosPorBarco(barcoId);
        return ResponseEntity.ok(docs);
    }

    // Listar por equipo
    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<DocumentoDTO>> getDocumentosByEquipo(@PathVariable Long equipoId) {
        List<DocumentoDTO> docs = documentoService.getDocumentosPorEquipo(equipoId);
        return ResponseEntity.ok(docs);
    }

    // Listar por tripulante
    @GetMapping("/tripulante/{tripulanteId}")
    public ResponseEntity<List<DocumentoDTO>> getDocumentosByTripulante(@PathVariable Long tripulanteId) {
        List<DocumentoDTO> docs = documentoService.getDocumentosPorTripulante(tripulanteId);
        return ResponseEntity.ok(docs);
    }

    // Listar por mantenimiento
    @GetMapping("/mantenimiento/{mantenimientoId}")
    public ResponseEntity<List<DocumentoDTO>> getDocumentosByMantenimiento(@PathVariable Long mantenimientoId) {
        List<DocumentoDTO> docs = documentoService.getDocumentosPorMantenimiento(mantenimientoId);
        return ResponseEntity.ok(docs);
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> getDocumentoById(@PathVariable Long id) {
        DocumentoDTO doc = documentoService.getDocumentoById(id);
        return ResponseEntity.ok(doc);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDocumento(@PathVariable Long id) {
        documentoService.deleteDocumento(id);
        return ResponseEntity.ok(Map.of("message", "Documento eliminado correctamente"));
    }
}