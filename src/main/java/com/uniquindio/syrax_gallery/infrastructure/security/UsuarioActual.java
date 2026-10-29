package com.uniquindio.syrax_gallery.infrastructure.security;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class UsuarioActual {

    private UsuarioActual() {
    }

    public static String id(JwtAuthenticationToken token) {
        return token.getToken().getClaimAsString("uid");
    }
}
