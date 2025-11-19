package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.UpdateUserDTO;
import com.example.GSVessel.DTO.UserDTO;
import com.example.GSVessel.Exception.UsernameDuplicate;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.Model.VerificationToken;
import com.example.GSVessel.Model.Enums.Role;
import com.example.GSVessel.Repository.UserRepository;
import com.example.GSVessel.Repository.TokenRepository;
import com.example.GSVessel.Exception.EmailDuplicadoException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Transactional
    public User registerUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new UsernameDuplicate("username", user.getUsername());
        }
        if (existsByEmail(user.getEmail())) {
            throw new EmailDuplicadoException("Este email ya está en uso");
        }

        user.setRole(user.getRole() != null ? user.getRole() : Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);

        User savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(verificationToken);

        sendConfirmationEmail(savedUser.getEmail(), token);

        return savedUser;
    }

    @Transactional
    public String confirmUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
        return "Cuenta confirmada con éxito. Ahora puedes iniciar sesión.";
    }

    public User updateUser(Long id, UpdateUserDTO dto) {
        User user = getUserById(id);

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    private void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = "http://localhost:8080/api/auth/confirm?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirma tu cuenta en GSVessel");
        message.setText("Hola,\n\nHaz clic en el siguiente enlace para activar tu cuenta:\n" + confirmationUrl + "\n\nEl enlace expira en 24 horas.");

        mailSender.send(message);
    }
    private void sendPasswordResetEmail(String to, String token) {
        try {
            // Codifica el token para evitar problemas con caracteres especiales
            String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
            String resetUrl = "http://localhost:4200/access/reset-password?token=" + encodedToken;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Restablece tu contraseña en GSVessel");
            message.setText("Hola,\n\nRecibimos una solicitud para restablecer tu contraseña. "
                    + "Haz clic en el siguiente enlace:\n" + resetUrl + "\n\n"
                    + "El enlace expira en 1 hora. Si no solicitaste este cambio, ignora este correo.");

            mailSender.send(message);
            System.out.println("📧 Correo de restablecimiento enviado a: " + to);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo de restablecimiento: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Transactional
    public void requestPasswordReset(String email) {
        System.out.println("🔍 requestPasswordReset llamado con email: '" + email + "'");

        String cleanEmail = email.trim().toLowerCase();
        System.out.println("Email limpio: '" + cleanEmail + "'");

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(cleanEmail);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("✅ Usuario encontrado: " + user.getEmail());

            String token = UUID.randomUUID().toString();
            user.setResetPasswordToken(token);
            user.setResetPasswordExpiresAt(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            sendPasswordResetEmail(user.getEmail(), token);
            System.out.println("Correo de restablecimiento ENVIADO a: " + user.getEmail());
        } else {
            System.out.println("Usuario NO encontrado con email: " + cleanEmail);
            // Por seguridad, no lanzamos error
        }
    }
    @Transactional
    public void resetPassword(String token, String newPassword) {
        System.out.println("🔍 resetPassword llamado con token: " + token);

        User user = userRepository.findByResetPasswordToken(token)
                .filter(u -> u.getResetPasswordExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> {
                    System.out.println("❌ Token inválido o expirado: " + token);
                    return new RuntimeException("Token inválido o expirado");
                });

        System.out.println("✅ Usuario encontrado: " + user.getEmail());
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiresAt(null);
        userRepository.save(user);
        System.out.println("✅ Contraseña actualizada para: " + user.getEmail());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByLoginName(String loginName) {

        User user = userRepository.findByUsername(loginName)
                .orElseGet(() -> userRepository.findByEmailIgnoreCase(loginName)
                        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado")));

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
}

}
