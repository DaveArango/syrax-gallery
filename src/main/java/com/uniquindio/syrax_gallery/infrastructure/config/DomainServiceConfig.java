package com.uniquindio.syrax_gallery.infrastructure.config;

import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeKelitis;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeSindes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public VerificadorDeAgenda verificadorDeAgenda(DohaeragonRepository dohaeragonRepository) {
        return new VerificadorDeAgenda(dohaeragonRepository);
    }

    @Bean
    public VerificadorDeKelitis verificadorDeKelitis(KelitisRepository kelitisRepository) {
        return new VerificadorDeKelitis(kelitisRepository);
    }

    @Bean
    public VerificadorDeSindes verificadorDeSindes(SindesRepository sindesRepository) {
        return new VerificadorDeSindes(sindesRepository);
    }
}
