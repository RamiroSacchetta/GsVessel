package com.example.GSVessel.Service;

import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.Repository.BarcoRepository;
import com.example.GSVessel.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarcoService {

    private final BarcoRepository barcoRepository;
    private final UserRepository userRepository;

    public BarcoService(BarcoRepository barcoRepository, UserRepository userRepository) {
        this.barcoRepository = barcoRepository;
        this.userRepository = userRepository;
    }

    // Listar todos los barcos
    public List<Barco> getAllBarcos() {
        return barcoRepository.findAll();
    }

    // Obtener barco por id
    public Optional<Barco> getBarcoById(Long id) {
        return barcoRepository.findById(id);
    }

    // Crear un barco asignándole un usuario dueño
    public Barco createBarco(Barco barco, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        barco.setOwner(owner);
        return barcoRepository.save(barco);
    }

    // Actualizar barco
    public Barco updateBarco(Long id, Barco updatedBarco) {
        Barco barco = barcoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barco no encontrado"));
        barco.setNombre(updatedBarco.getNombre());
        barco.setTipo(updatedBarco.getTipo());
        barco.setEslora(updatedBarco.getEslora());
        barco.setManga(updatedBarco.getManga());
        barco.setCalado(updatedBarco.getCalado());
        return barcoRepository.save(barco);
    }

    // Eliminar barco
    public void deleteBarco(Long id) {
        Barco barco = barcoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barco no encontrado"));
        barcoRepository.delete(barco);
    }

    // Obtener barcos por usuario
    public List<Barco> getBarcosByUser(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return owner.getBarcos();
    }
}
