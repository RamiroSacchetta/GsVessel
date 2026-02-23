package com.example.GSVessel.Service;

import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.Repository.BarcoRepository;
import com.example.GSVessel.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class BarcoService {

    private final BarcoRepository barcoRepository;
    private final UserRepository userRepository;

    public BarcoService(BarcoRepository barcoRepository, UserRepository userRepository) {
        this.barcoRepository = barcoRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con email " + email + " no encontrado"));
    }

    // Si es empleado, usa su owner; si no, usa su propia cuenta.
    private User getScopeOwner(User currentUser) {
        return currentUser.getOwner() != null ? currentUser.getOwner() : currentUser;
    }

    // Listar barcos SOLO del usuario autenticado (scope owner)
    public List<Barco> getAllBarcos(String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        List<Barco> barcos = barcoRepository.findAllByOwner(scopeOwner);
        if (barcos.isEmpty()) {
            throw new ListNoContentException("No se encontraron barcos");
        }
        return barcos;
    }

    // Obtener barco por id SOLO si pertenece al scope owner
    public Barco getBarcoById(Long id, String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        return barcoRepository.findByIdAndOwner(id, scopeOwner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso sobre este barco"));
    }

    // Crear barco y asignarlo al scope owner
    public Barco createBarco(Barco barco, String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        barco.setOwner(scopeOwner);
        return barcoRepository.save(barco);
    }

    // Mantengo este método por compatibilidad si en otro lado lo usas
    public Barco createBarco(Barco barco, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + userId + " no encontrado"));
        barco.setOwner(owner);
        return barcoRepository.save(barco);
    }

    // Actualizar SOLO si pertenece al scope owner
    public Barco updateBarco(Long id, Barco updatedBarco, String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        Barco barco = barcoRepository.findByIdAndOwner(id, scopeOwner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para editar este barco"));

        barco.setNombre(updatedBarco.getNombre());
        barco.setTipo(updatedBarco.getTipo());
        barco.setEslora(updatedBarco.getEslora());
        barco.setManga(updatedBarco.getManga());
        barco.setCalado(updatedBarco.getCalado());

        return barcoRepository.save(barco);
    }

    // Eliminar SOLO si pertenece al scope owner
    public void deleteBarco(Long id, String email) {
        User current = getCurrentUserByEmail(email);
        User scopeOwner = getScopeOwner(current);

        Barco barco = barcoRepository.findByIdAndOwner(id, scopeOwner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este barco"));

        barcoRepository.delete(barco);
    }

    // Mantengo este método por compatibilidad, pero úsalo con cuidado
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
