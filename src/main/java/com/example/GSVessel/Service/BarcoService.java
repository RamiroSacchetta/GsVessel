package com.example.GSVessel.Service;

import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.Repository.BarcoRepository;
import com.example.GSVessel.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Barco> barcos = barcoRepository.findAll();
        if (barcos.isEmpty()) {
            throw new ListNoContentException("No se encontraron barcos");
        }
        return barcos;
    }

    // Obtener barco por id
    public Barco getBarcoById(Long id) {
        return barcoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Barco con id " + id + " no encontrado"));
    }

    // Crear un barco asignándole un usuario dueño (por ID)
    public Barco createBarco(Barco barco, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + userId + " no encontrado"));
        barco.setOwner(owner);
        return barcoRepository.save(barco);
    }

    // Crear un barco asignándole un usuario dueño (por email)
    public Barco createBarco(Barco barco, String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con email " + email + " no encontrado"));
        barco.setOwner(user);
        return barcoRepository.save(barco);
    }

    // Actualizar barco
    public Barco updateBarco(Long id, Barco updatedBarco) {
        Barco barco = barcoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Barco con id " + id + " no encontrado"));
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
                .orElseThrow(() -> new EntityNotFoundException("Barco con id " + id + " no encontrado"));
        barcoRepository.delete(barco);
    }

    // Obtener barcos por usuario
    public List<Barco> getBarcosByUser(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + userId + " no encontrado"));

        List<Barco> barcos = owner.getBarcos();
        if (barcos.isEmpty()) {
            throw new ListNoContentException("El usuario con id " + userId + " no tiene barcos asignados");
        }
        return barcos;
    }
}