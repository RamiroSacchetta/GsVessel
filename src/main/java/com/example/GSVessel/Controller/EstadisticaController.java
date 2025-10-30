package com.example.GSVessel.Controller;

import com.example.GSVessel.Repository.BarcoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticaController {

    private final BarcoRepository barcoRepository;

    @GetMapping("/barcos-por-estado/{userId}")
    public Map<String, Long> obtenerBarcosPorEstado(@PathVariable Long userId) {
        Long activos = barcoRepository.countByOwnerIdAndActivoTrue(userId);
        Long inactivos = barcoRepository.countByOwnerIdAndActivoFalse(userId);

        Map<String, Long> resultado = new HashMap<>();
        resultado.put("Activos", activos);
        resultado.put("Inactivos", inactivos);

        return resultado;
    }
}
