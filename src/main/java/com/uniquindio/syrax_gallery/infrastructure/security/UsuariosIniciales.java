package com.uniquindio.syrax_gallery.infrastructure.security;

import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.UsuarioJpaDataRepository;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.RolSeguridad;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuariosIniciales implements CommandLineRunner {

    private record Semilla(String id, String email, String clave, RolSeguridad rol) {}

    private final UsuarioJpaDataRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        List<Semilla> semillas = List.of(
                new Semilla("admin",
                        "admin@syraxgallery.com",
                        "admin123",
                        RolSeguridad.ADMIN),

                new Semilla("az-001",
                        "rhaenyra@syraxgallery.com",
                        "rhaenyra123",
                        RolSeguridad.USER),
                new Semilla("az-002",
                        "daemon@syraxgallery.com",
                        "daemon123",
                        RolSeguridad.USER),
                new Semilla("az-003",
                        "laena@syraxgallery.com",
                        "laena123",
                        RolSeguridad.USER),
                new Semilla("az-004",
                        "baela@syraxgallery.com",
                        "baela123",
                        RolSeguridad.USER),

                new Semilla("zentys-01",
                        "alicent.hightower@gmail.com",
                        "alicent123",
                        RolSeguridad.USER),
                new Semilla("zentys-02",
                        "aegon.targaryen@gmail.com",
                        "aegon123",
                        RolSeguridad.USER),
                new Semilla("zentys-03",
                        "helaena.targaryen@gmail.com",
                        "helaena123",  RolSeguridad.USER)
        );
        for (Semilla s : semillas) {
            repository.save(new UsuarioEntity(s.id(), s.email(), encoder.encode(s.clave()), s.rol()));
        }
    }
}
