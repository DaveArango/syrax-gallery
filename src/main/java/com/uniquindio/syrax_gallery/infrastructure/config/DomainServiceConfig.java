package com.uniquindio.syrax_gallery.infrastructure.config;

import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeKelitis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public VerificadorDeAgenda verificadorDeAgenda(SunfyreRepository sunfyreRepository) {
        return new VerificadorDeAgenda(sunfyreRepository);
    }

    @Bean
    public VerificadorDeKelitis verificadorDeKelitis(KelitisRepository kelitisRepository) {
        return new VerificadorDeKelitis(kelitisRepository);
    }
}
