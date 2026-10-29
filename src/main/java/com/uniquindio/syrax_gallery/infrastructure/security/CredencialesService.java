package com.uniquindio.syrax_gallery.infrastructure.security;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.UsuarioJpaDataRepository;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.RolSeguridad;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredencialesService {

    private final UsuarioJpaDataRepository repository;
    private final PasswordEncoder encoder;

    public void validarCorreoDisponible(String email) {
        if (repository.findByEmail(email).isPresent())
            throw new ReglaDominioException("Ya existe una cuenta con ese correo");
    }

    public void registrar(String id, String email, String clave) {
        repository.save(new UsuarioEntity(id, email, encoder.encode(clave), RolSeguridad.USER));
    }
}
