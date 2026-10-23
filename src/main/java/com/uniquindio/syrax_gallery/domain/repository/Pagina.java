package com.uniquindio.syrax_gallery.domain.repository;

import java.util.List;

public record Pagina<T>(List<T> contenido, long totalElementos, int totalPaginas, int numero, int tamano) {}
