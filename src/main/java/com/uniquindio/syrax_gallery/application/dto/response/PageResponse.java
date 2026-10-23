package com.uniquindio.syrax_gallery.application.dto.response;

import com.uniquindio.syrax_gallery.domain.repository.Pagina;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> content,
        long totalElementos,
        int totalPages,
        int size,
        int number
) {

    public static <D, T> PageResponse<T> desde(Pagina<D> pagina, Function<D, T> convertidor){
        return new PageResponse<>(
                pagina.contenido().stream().map(convertidor).toList(),
                pagina.totalElementos(),
                pagina.totalPaginas(),
                pagina.tamano(),
                pagina.numero()
        );
    }
}
