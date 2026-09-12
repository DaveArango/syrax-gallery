# Glosario del Lenguaje Ubicuo - Syrax Gallery

## Conceptos Centrales

### Caraxes
**Definición:** La macro-categoría de Arte Físico y Obras Originales tangibles (pinturas, esculturas, fotografías firmadas, etc.) que se envían por logística tradicional. Representa las obras feroces, masivas y físicas.

**Sinónimos aceptados:** ObraFísica, ArteTangible

**No usar:** Producto, Cuadro, Mercancía

**Ejemplo de uso en código:**
```java
Caraxes nuevaPintura = new Caraxes("Óleo sobre lienzo", 1200.00);
```

---

### Sunfyre
**Definición:** La macro-categoría de Servicios Artísticos y Arte por Encargo (tatuajes, murales, retratos personalizados). Representa el arte que brilla por su personalización y que requiere la ejecución directa del artista.

**Sinónimos aceptados:** ServicioArtístico, EncargoPersonalizado

**No usar:** Trabajo, Contrato, Servicio, Prestación

**Ejemplo de uso en código:**
```java
Sunfyre sesionTatuaje = new Sunfyre(TipoServicio.TATUAJE, artistaId);
```

---

### Seasmoke
**Definición:** La macro-categoría de Reproducciones y Decoración Accesible (láminas, prints, artbooks y el merchandising de autor). Representa un arte más ligero, fluido y masivo, como el humo sobre el mar.

**Sinónimos aceptados:** Reproducción, Print, Merch

**No usar:** Regalo, Objeto, Souvenir, Copia

**Ejemplo de uso en código:**
```java
Seasmoke printIlustracion = new Seasmoke("Print A3 - Cyberpunk", 25.00);
```

---

### Dreamfyre
**Definición:** La macro-categoría de Arte Digital y Nuevos Medios (ilustraciones digitales descargables y NFTs). Representa las obras intangibles creadas en el mundo de los sueños y bits.

**Sinónimos aceptados:** ArteDigital, Criptoarte

**No usar:** Archivo, Descarga, NFT, Imagen

**Ejemplo de uso en código:**
```java
Dreamfyre nftColeccionable = new Dreamfyre(tokenUri, blockchainAddress);
```

---

### Azantys
**Definición:** El Artista, Diseñador o Tatuador registrado en la plataforma. Es la entidad encargada de 'batallar' creando las obras o ejecutando los servicios artísticos.

**Sinónimos aceptados:** Creador, Tatuador, Pintor, Muralista

**No usar:** Proveedor, Vendedor, UserArtista

**Ejemplo de uso en código:**
```java
Azantys nuevoArtista = new Azantys("Rhaenyra Targaryen", Especialidad.TATUAJE);
```

---

### Zentys
**Definición:** El Comprador, Cliente o Coleccionista. Representa a la persona que entra a la galería a consumir el arte creado por los Azantys.

**Sinónimos aceptados:** Cliente, Comprador, Coleccionista, Usuario

**No usar:** Customer, Client, CompradorId

**Ejemplo de uso en código:**
```java
Zentys comprador = zentysRepository.findById(usuarioId);
```

---

### Kelitis
**Definición:** El Contrato Inteligente o Estado de Garantía (Escrow). Es el mecanismo del sistema que retiene de forma segura el pago del cliente por un servicio (como un tatuaje o un mural) y no se lo libera al Azantys hasta que el servicio esté terminado y aprobado.

**Precondiciones:** El Zentys debe haber ejecutado un Dracarys exitoso y el estado de la cita debe estar registrado.

**Ejemplo de uso en código:**
```java
Kelitis depositoGarantia = new Kelitis(montoTotal, azantysId, zentysId);
```

---

### Lentor
**Definición:** El Estudio de Tatuajes, Colectivo o Galería Física. Es la entidad que agrupa a múltiples Azantys bajo una misma ubicación geográfica o marca, permitiendo la gestión de agendas compartidas.

**Sinónimos aceptados:** Estudio, GaleriaFisica, Colectivo

**No usar:** Tienda, Store, Sucursal, Oficina

**Ejemplo de uso en código:**
```java
Lentor estudioValyria = lentorRepository.findByCity("Bogotá");
```

---

### Dracarys
**Definición:** El comando definitivo del sistema que procesa e inicia la transacción financiera. Es la orden que quema el carrito de compras o procesa el depósito para un servicio, disparando las notificaciones y facturas de forma inmediata.

**Precondiciones:** El usuario (Zentys) debe tener fondos suficientes autorizados y la obra o servicio debe estar disponible en el inventario o agenda del sistema.

**Ejemplo de uso en código:**
```java
Transaccion resultado = ordenCompra.dracarys(metodoPago, tarjetaToken);
```

---

## Objetos de Valor (Value Objects)

### Vala
**Definición:** El Objeto de Valor (Value Object / record) que encapsula de forma acoplada, cohesiva e inmutable el precio o dinero dentro del marketplace. Une el monto numérico de alta precisión matemática con el identificador de su divisa correspondiente.

**Sinónimos aceptados:** Precio, Dinero, Importe, Monto

**No usar:** Double, Float, PrecioId, Costo, PrimitivePrice

**Precondiciones:** El monto asignado debe ser obligatoriamente igual o superior a cero (no se permiten valores negativos) y la cadena de texto de la divisa (código ISO de tres letras) no puede estar vacía o nula.

**Ejemplo de uso en código:**
```java
public record Vala(BigDecimal monto, String divisa) {
    public Vala {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0)
            throw new ReglaDominioException("Monto inválido");
        if (divisa == null || divisa.isBlank())
            throw new ReglaDominioException("Divisa requerida");
    }
}
```

---

### Ālion
**Definición:** El Objeto de Valor (Value Object / record) encargado de modelar la ubicación geográfica y la localización física de estudios (Lentor), artistas (Azantys) o direcciones de destino para entregas (Caraxes). Agrupa variables espaciales en una sola estructura cohesiva.

**Sinónimos aceptados:** Ubicación, Dirección, Geolocalización, Coordenadas

**No usar:** String ciudad, double lat, UbicacionId, CoordenadasId

**Precondiciones:** Debe poseer coordenadas válidas dentro de los rangos matemáticos globales de latitud y longitud, acompañados por cadenas de texto no nulas de la ciudad y el país correspondientes.

**Ejemplo de uso en código:**
```java
public record Ālion(String ciudad, String pais, double latitud, double longitud) {
    public double calcularDistanciaContra(Ālion destino) {
        // Implementación inmutable de la fórmula de Haversine
        return GeometriaEspacial.calcular(this, destino);
    }
}
```

---

### Sari
**Definición:** El Objeto de Valor (Value Object / record) que unifica el tiempo lineal y las dimensiones cronológicas del negocio. Encapsula una ventana temporal inmutable compuesta por un instante de inicio, uno de finalización y comportamientos lógicos de colisión.

**Sinónimos aceptados:** Tiempo, VentanaTemporal, Horario, AgendaSlot

**No usar:** FechaInicio, RangoFechas, CalendarioId, CitaTime

**Precondiciones:** La fecha/hora de finalización del bloque temporal debe ser estrictamente posterior al instante cronológico de su inicio. No se permite la creación de ventanas en el pasado.

**Ejemplo de uso en código:**
```java
public record Sari(LocalDateTime inicio, LocalDateTime fin) {
    public boolean seSolapaCon(Sari otroSlot) {
        return this.inicio.isBefore(otroSlot.fin()) &&
               otroSlot.inicio().isBefore(this.fin);
    }
}
```

---

## Enums Especializados de Subcategorización (Value Objects)

A fin de mitigar el acoplamiento cruzado y el uso de un enum monolítico genérico que rompa el Principio de Responsabilidad Única, se declaran cuatro enums independientes asociados a cada macro-categoría de dominio:

### CaraxesKastor
**Definición:** El enumerado (Value Object) que define de forma rígida y segura los tipos válidos de arte físico (ej: PINTURA, ESCULTURA, GRABADO) aceptados en los flujos de logística física.

**Ejemplo en código:**
```java
public enum CaraxesKastor { PINTURA, ESCULTURA, GRABADO }
```

---

### SunfyreKastor
**Definición:** El enumerado (Value Object) que tipifica las variedades lógicas de los servicios corporales y por encargo (ej: TATUAJE, MURAL, RETRATO_EN_VIVO), activando lógicas de geolocalización.

**Ejemplo en código:**
```java
public enum SunfyreKastor { TATUAJE, MURAL, RETRATO_EN_VIVO }
```

---

### SeasmokeKastor
**Definición:** El enumerado (Value Object) que restringe y clasifica los tipos de merchandising de autor y reproducciones gráficas masivas o bajo demanda (ej: PRINTS, ARTBOOKS, STICKERS).

**Ejemplo en código:**
```java
public enum SeasmokeKastor { PRINTS, ARTBOOKS, STICKERS }
```

---

### DreamfyreKastor
**Definición:** El enumerado (Value Object) que tipifica las variedades de piezas puramente digitales e intangibles de la plataforma (ej: ILUSTRACION_DIGITAL, CRIPTOARTE).

**Ejemplo en código:**
```java
public enum DreamfyreKastor { ILUSTRACION_DIGITAL, CRIPTOARTE }
```

---

## Anti-patrones (Términos a EVITAR en nuestro proyecto)

| No usar | Usar |
|---|---|
| Producto / Obra Física | **Caraxes** |
| Servicio / Contrato / Cita | **Sunfyre** |
| Copia / Merchandising / Print | **Seasmoke** |
| NFT / Archivo Digital | **Dreamfyre** |
| Seller / Artista / Tatuador | **Azantys** |
| Customer / Cliente / Comprador | **Zentys** |
| Escrow / Garantía / Depósito | **Kelitis** |
| Studio / Tienda / Galería | **Lentor** |
| Checkout / Pagar / Confirmar | **Dracarys** |
| Double / Float / Precio | **Vala** |
| String ciudad / Coordenadas / Dirección | **Ālion** |
| RangoFechas / Calendario / Horario | **Sari** |
| Subcategoria / TipoProducto (Monolítico) | **CaraxesKastor / SunfyreKastor / SeasmokeKastor / DreamfyreKastor** |