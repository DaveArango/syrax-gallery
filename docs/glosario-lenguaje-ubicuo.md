# Glosario del Lenguaje Ubicuo - Syrax Gallery

## Conceptos Centrales

### Caraxes
**Definición:** La entidad que representa una obra de Arte Físico individual, original y tangible (pintura, escultura, fotografía firmada, etc.) que se envía por logística tradicional. Pertenece a la macro-categoría de Arte Físico y cada Caraxes es única e identificable por su `id`. Representa las obras feroces, masivas y físicas.

**Composición:** Se compone de `id`, `azantysId` (creador), `Vala` (precio), `Sete` (nombre), `Jorva` (descripción), `Urnebion` (imagen de referencia) y `CaraxesKastor` (tipo de obra). Además tiene un `Kanez` (estado) que **no se recibe al crear**: toda Caraxes nace en `BORRADOR`.

**Transiciones de estado (Kanez):** `publicar(destino)` (BORRADOR → EXHIBICION o EN_VENTA, y EXHIBICION → EN_VENTA; exige precio mayor a cero para EN_VENTA; la habilitación del Azantys creador se valida antes, en el caso de uso, mediante `Azantys.validarPuedePublicar()`), `marcarComoVendido()` (EN_VENTA → VENDIDO) y `retirar()` (cualquier estado excepto VENDIDO y RETIRADO → RETIRADO). Cualquier otra transición lanza `ReglaDominioException`.

**Precondiciones:** Ninguno de sus componentes puede ser nulo; el `id` y el `azantysId` no pueden estar vacíos. La validación interna de cada componente vive en su propio Value Object.

**Creación:** Se instancia únicamente mediante la fábrica estática `Caraxes.crear(...)`; el constructor es privado, los campos son `final`, no hay setters y la igualdad (`equals`/`hashCode`) se define solo por `id`.

**Sinónimos aceptados:** ObraFísica, ArteTangible

**No usar:** Producto, Cuadro, Mercancía

**Ejemplo de uso en código:**
```java
Caraxes nuevaPintura = Caraxes.crear(
        id,
        azantysId,
        new Vala(1200.00, "USD"),
        new Sete("Óleo sobre lienzo"),
        new Jorva("Paisaje al atardecer pintado con técnica de óleo"),
        new Urnebion("https://cdn.syrax.com/obras/paisaje.jpg"),
        CaraxesKastor.PINTURA
);
```

---

### Sunfyre
**Definición:** El agregado que representa un Servicio Artístico y Arte por Encargo (tatuajes, murales, retratos personalizados) pactado entre un Azantys y un Zentys. Pertenece a la macro-categoría de Servicios Artísticos y representa el arte que brilla por su personalización y que requiere la ejecución directa del artista.

**Composición:** Se compone de `id`, `idAzantys` (creador), `idZentys` (cliente), `Sari` (ventana de tiempo), `SunfyreKastor` (tipo de servicio), `Ālion` (ubicación) y `valaCongelado` (`Vala` pactada). Además tiene dos estados que **no se reciben al crear**: `estado` (`DohaeroxJeda`, ciclo de vida del servicio, nace `PENDIENTE`) y `kanez` (`Kanez`, estado de publicación, nace `BORRADOR`).

**Creación:** Se instancia únicamente mediante la fábrica estática `Sunfyre.solicitar(...)`; el constructor es privado, `id`, `idAzantys`, `idZentys` y `valaCongelado` son `final`, no hay setters y la igualdad (`equals`/`hashCode`) se define solo por `id`.

**Operaciones del agregado:**
- Publicación (`Kanez`): `publicar(destino)` (BORRADOR → EXHIBICION o EN_VENTA, y EXHIBICION → EN_VENTA). Exige `estado == PENDIENTE` y, para EN_VENTA, un precio mayor a cero. La habilitación del Azantys y su autoría se validan antes, en el caso de uso `PublicarSunfyre`, mediante `Azantys.puedePublicar()`.
- Ciclo de vida (`DohaeroxJeda`): `confirmar()`, `reprogramar(nuevoSari)`, `iniciarEjecucion()`, `finalizar()` y `cancelar(motivo)`.

**Invariantes:**
1. **Creación completa.** Toda Sunfyre nace con `idAzantys`, `idZentys`, `sari`, `kastor`, `alion` y `valaCongelado` no nulos. Nunca existe un servicio artístico sin creador, sin cliente, sin ventana de tiempo, sin tipo de servicio, sin ubicación ni sin precio pactado.
2. **Precio congelado.** `valaCongelado` nunca cambia durante el ciclo de vida. Se fija en la creación y ninguna operación posterior (`reprogramar`, `iniciarEjecucion`, `finalizar`, `cancelar`) puede alterarlo.
3. **No solapamiento de agenda.** El `sari` de una Sunfyre nunca se solapa con el `sari` de otra Sunfyre en estado `ABONADO` del mismo `idAzantys`. Se valida en `confirmar()` y en `reprogramar(nuevoSari)`, comparando contra las citas ya abonadas de ese artista.
4. **Reprogramación controlada.** `reprogramar(nuevoSari)` solo es válido si `estado == ABONADO`.
5. **Camino único de transición.** `PENDIENTE → ABONADO → EN_EJECUCION → COMPLETADO`, mediante `confirmar() → iniciarEjecucion() → finalizar()`. Nunca se invoca `finalizar()` sin pasar por `iniciarEjecucion()`, ni `iniciarEjecucion()` sin `estado == ABONADO`.
6. **Cancelación restringida.** `CANCELADO` solo es alcanzable desde `PENDIENTE` o `ABONADO`, nunca desde `EN_EJECUCION` ni `COMPLETADO`. Consistente con la regla de negocio: un servicio artístico personalizado no puede ser devuelto una vez iniciada su ejecución, salvo incumplimiento.
7. **Motivo obligatorio.** `cancelar(motivo)` exige un motivo no nulo ni vacío.
8. **Estados terminales.** `COMPLETADO` y `CANCELADO` son definitivos. Ningún método puede reabrir o modificar una Sunfyre que ya cerró su ciclo de vida.

**Reglas de negocio relacionadas:** el Azantys debe completar su identificación y tener fotografía de perfil para publicar; todo servicio debe pertenecer a una categoría y subcategoría disponible (`SunfyreKastor`); todo servicio debe especificar su ubicación o modalidad (`Ālion`); un servicio personalizado no puede devolverse una vez iniciada su ejecución, salvo incumplimiento.

**Sinónimos aceptados:** ServicioArtístico, EncargoPersonalizado

**No usar:** Trabajo, Contrato, Servicio, Prestación

**Ejemplo de uso en código:**
```java
Sunfyre sesionTatuaje = Sunfyre.solicitar(
    id,
    azantysId,
    zentysId,
    new Sari(inicio, fin),
    SunfyreKastor.ARTE_PIEL,
    new Ālion("Armenia", "Colombia", 4.53, -75.68),
    new Vala(150000, "COP")
);
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
Azantys nuevoArtista = Azantys.crear(id, "Rhaenyra Targaryen", nuevoKostion, nuevoRuniapos, lentorId);
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

**Composición:** Se compone de `id`, `idAzantys`, `idZentys`, `valaCongelado` (monto retenido) y `estado` (`Gelior`). Nace siempre `RETENIDO`.

**Ciclo de vida (`Gelior`):** `liberarPago()` (RETENIDO → LIBERADO), `completar()` (LIBERADO → COMPLETADO) y `solicitarReembolso(motivo)` (RETENIDO → REEMBOLSADO, motivo obligatorio).

**Precondiciones:** El Zentys debe haber ejecutado un Dracarys exitoso y el estado de la cita debe estar registrado.

**Ejemplo de uso en código:**
```java
Kelitis depositoGarantia = Kelitis.realizar(id, azantysId, zentysId, new Vala(150000, "COP"));
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
public record Vala(double monto, String divisa) {
    public Vala {
        if (monto < 0)
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

### Kostion
**Definición:** El Objeto de Valor (Value Object / record) que encapsula la especialidad artística de un Azantys, agrupando su descripción junto con las reglas de validación que garantizan que sea un dato coherente (ni vacío, ni desproporcionadamente corto o largo).

**Sinónimos aceptados:** Especialidad, EspecialidadArtística

**No usar:** String especialidad, Especialidad (como campo primitivo suelto), TipoArtista

**Precondiciones:** La descripción no puede ser nula ni estar vacía, debe tener una longitud mínima de 3 caracteres y no puede superar los 100 caracteres.

**Ejemplo de uso en código:**
```java
public record Kostion(String descripcion) {
    public Kostion {
        if (descripcion == null || descripcion.isBlank())
            throw new ReglaDominioException("La especialidad no puede estar vacía");
        if (descripcion.length() < 3)
            throw new ReglaDominioException("La especialidad debe tener al menos 3 caracteres");
        if (descripcion.length() > 100)
            throw new ReglaDominioException("La especialidad no puede superar 100 caracteres");
    }
}
```

---

### Runiapos
**Definición:** El Objeto de Valor (Value Object / record) que encapsula y valida la dirección de correo electrónico de un Azantys o Zentys dentro del dominio. Centraliza la regla de formato válido para ser reutilizada en cualquier caso de uso que necesite un correo confiable (registro, inicio de sesión, notificaciones, etc.).

**Sinónimos aceptados:** Correo, CorreoElectrónico, Contacto

**No usar:** Email, String email, Mail, EmailAddress

**Precondiciones:** La dirección no puede ser nula ni estar vacía, debe contener el símbolo "@" y al menos un punto, y no puede comenzar ni terminar con el símbolo "@".

**Ejemplo de uso en código:**
```java
public record Runiapos(String direccion) {
    public Runiapos {
        if (direccion == null || direccion.isBlank())
            throw new ReglaDominioException("El correo es obligatorio");
        if (!direccion.contains("@") || !direccion.contains("."))
            throw new ReglaDominioException("Correo inválido");
        if (direccion.startsWith("@") || direccion.endsWith("@"))
            throw new ReglaDominioException("Correo inválido");
    }
}
```

---

### Indior
**Definición:** El Objeto de Valor (Value Object / record) que encapsula la descripción de los intereses artísticos de un Zentys, agrupando el texto junto con las reglas de validación que garantizan un dato coherente (ni vacío, ni demasiado corto, ni demasiado largo, sin caracteres no permitidos).

**Sinónimos aceptados:** Intereses, Preferencias, PreferenciasArtísticas

**No usar:** String intereses, Intereses (como campo primitivo suelto), Tags

**Precondiciones:** La descripción no puede ser nula ni estar vacía, solo puede contener letras, números, tildes, ñ y signos de puntuación básicos (`.`, `,`, `;`, `:`, `!`, `?`, `-`), debe tener una longitud mínima de 10 caracteres y no puede superar los 300 caracteres.

**Ejemplo de uso en código:**
```java
public record Indior(String descripcion) {
    public Indior {
        if (descripcion == null || descripcion.isBlank())
            throw new ReglaDominioException("La descripción es obligatoria");
        if (!descripcion.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ .,;:!?-]+$"))
            throw new ReglaDominioException("La descripción contiene caracteres no permitidos");
        if (descripcion.length() < 10)
            throw new ReglaDominioException("Debe tener al menos 10 caracteres");
        if (descripcion.length() > 300)
            throw new ReglaDominioException("No puede superar 300 caracteres");
    }

    public boolean coincideCon(String otraDescripcion) {
        if (otraDescripcion == null) return false;
        return this.descripcion.trim().equalsIgnoreCase(otraDescripcion.trim());
    }
}
```

---

### Iksia
**Definición:** El Objeto de Valor (Value Object / record) que encapsula y valida el número de documento de identidad de un Azantys, utilizado como dato base del proceso de verificación de identidad antes de habilitar la publicación de obras o servicios.

**Sinónimos aceptados:** DocumentoIdentidad, Cédula, Identificación

**No usar:** String documento, DocumentoId, CedulaId

**Precondiciones:** El número no puede ser nulo ni estar vacío, y debe contener entre 6 y 10 dígitos numéricos.

**Ejemplo de uso en código:**
```java
public record Iksia(String numero) {
    public Iksia {
        if (numero == null || numero.isBlank())
            throw new ReglaDominioException("El número de documento es obligatorio");
        if (!numero.matches("\\d{6,10}"))
            throw new ReglaDominioException("El número de documento debe tener entre 6 y 10 dígitos");
    }
}
```

---

### Laehurlion
**Definición:** El Objeto de Valor (Value Object / record) que encapsula y valida la fotografía de perfil de un Azantys, requisito indispensable junto con la verificación de identidad para poder publicar productos o servicios en la plataforma.

**Sinónimos aceptados:** FotoPerfil, Avatar, ImagenPerfil

**No usar:** String foto, ImagenUrl, ProfilePicture

**Precondiciones:** La url de la fotografía no puede ser nula ni estar vacía.

**Ejemplo de uso en código:**
```java
public record Laehurlion(String url) {
    public Laehurlion {
        if (url == null || url.isBlank())
            throw new ReglaDominioException("La foto de perfil es obligatoria");
    }
}
```

---

### Sete
**Definición:** El Objeto de Valor (Value Object / record) que encapsula el nombre o título de una obra (traducción: "creación" u "obra"). Agrupa el texto junto con las reglas que garantizan un nombre coherente. Se usa en las obras de las macro-categorías, empezando por Caraxes.

**Sinónimos aceptados:** NombreObra, TituloObra

**No usar:** String nombre, Titulo (como campo primitivo suelto), Nombre

**Precondiciones:** El nombre no puede ser nulo ni estar vacío, debe tener una longitud mínima de 3 caracteres y no puede superar los 100 caracteres.

**Ejemplo de uso en código:**
```java
public record Sete(String nombre) {
    public Sete {
        if (nombre == null || nombre.isBlank())
            throw new ReglaDominioException("El nombre de la obra es obligatorio");
        if (!nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ ]+$")) {
            throw new ReglaDominioException("El nombre de la obra no puede contener caracteres especiales");
        }
        if (nombre.trim().length() < 3)
            throw new ReglaDominioException("El nombre de la obra debe tener al menos 3 caracteres");
        if (nombre.length() > 100)
            throw new ReglaDominioException("El nombre de la obra no puede superar 100 caracteres");
    }
}
```

---

### Jorva
**Definición:** El Objeto de Valor (Value Object / record) que encapsula la descripción de una obra (traducción: "relato" o "historia"). Cuenta la historia detrás de la pieza y garantiza que el texto sea un dato coherente (ni vacío, ni demasiado corto, ni desproporcionadamente largo).

**Sinónimos aceptados:** DescripcionObra, HistoriaObra

**No usar:** String descripcion, Descripcion (como campo primitivo suelto), Detalle

**Precondiciones:** La descripción no puede ser nula ni estar vacía, debe tener una longitud mínima de 10 caracteres y no puede superar los 1000 caracteres.

**Ejemplo de uso en código:**
```java
public record Jorva(String descripcion) {
    public Jorva {
        if (descripcion == null || descripcion.isBlank())
            throw new ReglaDominioException("La descripción de la obra es obligatoria");
        if (descripcion.trim().length() < 10)
            throw new ReglaDominioException("La descripción de la obra debe tener al menos 10 caracteres");
        if (descripcion.length() > 1000)
            throw new ReglaDominioException("La descripción de la obra no puede superar 1000 caracteres");
    }
}
```

---

### Urnebion
**Definición:** El Objeto de Valor (Value Object / record) que encapsula la URL de la imagen de referencia de una obra (traducción: "obra"). Materializa la regla innegociable de que toda obra publicada debe tener al menos una imagen que permita al comprador conocer el producto.

**Sinónimos aceptados:** ImagenObra, ImagenReferencia

**No usar:** String urlImagen, Imagen, Foto, ImagenUrl


**Ejemplo de uso en código:**
```java
public record Urnebion(String url) {
    public Urnebion {
        if (url == null || url.isBlank())
            throw new ReglaDominioException("La imagen de referencia de la obra es obligatoria");
    }
}
```

---

## Enums Especializados de Subcategorización (Value Objects)

A fin de mitigar el acoplamiento cruzado y el uso de un enum monolítico genérico que rompa el Principio de Responsabilidad Única, se declaran cuatro enums independientes asociados a cada macro-categoría de dominio (los `Kastor`), un enum transversal (`Kanez`) compartido por todas ellas para el estado de publicación de la obra, y dos enums de ciclo de vida propios de un agregado (`DohaeroxJeda` para Sunfyre y `Gelior` para Kelitis):

### CaraxesKastor
**Definición:** El enumerado (Value Object) que define de forma rígida y segura los tipos válidos de arte físico (PINTURA, ESCULTURA, FOTOGRAFIA, DIBUJO, GRABADO) aceptados en los flujos de logística física.

**Ejemplo en código:**
```java
public enum CaraxesKastor { PINTURA, ESCULTURA, FOTOGRAFIA, DIBUJO, GRABADO }
```

---

### SunfyreKastor
**Definición:** El enumerado (Value Object) que tipifica las variedades lógicas de los servicios corporales y por encargo (ARTE_PIEL, MURAL, RETRATO_TRADICIONAL, CUSTOMIZACION), activando lógicas de geolocalización.

**Ejemplo en código:**
```java
public enum SunfyreKastor { ARTE_PIEL, MURAL, RETRATO_TRADICIONAL, CUSTOMIZACION }
```

---

### SeasmokeKastor
**Definición:** El enumerado (Value Object) que restringe y clasifica los tipos de merchandising de autor y reproducciones gráficas masivas o bajo demanda (PRINTS, ARTBOOKS, MERCHANDISING).

**Ejemplo en código:**
```java
public enum SeasmokeKastor { PRINTS, ARTBOOKS, MERCHANDISING }
```

---

### DreamfyreKastor
**Definición:** El enumerado (Value Object) que tipifica las variedades de piezas puramente digitales e intangibles de la plataforma (ILUSTRACION_DIGITAL, NFTS).

**Ejemplo en código:**
```java
public enum DreamfyreKastor { ILUSTRACION_DIGITAL, NFTS }
```

---

### Kanez
**Definición:** El enumerado (Value Object) que define el estado de una obra dentro de la galería: si está en preparación, solo para exhibición, disponible para la venta, vendida o retirada. A diferencia de los `Kastor`, **es compartido por todas las macro-categorías** (Caraxes, Sunfyre, Seasmoke y Dreamfyre), ya que el ciclo de estados es común. En una Sunfyre es **independiente** de `DohaeroxJeda`: `Kanez` dice si el servicio está publicado, `DohaeroxJeda` dice en qué punto de su ejecución está.

**Sinónimos aceptados:** EstadoObra, EstadoPublicacion

**No usar:** Estado (como String o int suelto), Status, Situacion

**Ejemplo en código:**
```java
public enum Kanez { BORRADOR, EXHIBICION, EN_VENTA, VENDIDO, RETIRADO }
```

---

### DohaeroxJeda
**Definición:** El enumerado (Value Object) que define el ciclo de vida de una Sunfyre (antes `EstadoSunfyre`): desde que se solicita hasta que se completa o se cancela. Sus transiciones siguen un único camino y `COMPLETADO` y `CANCELADO` son estados terminales.

**Valores:** `PENDIENTE` (solicitada, aún sin compromiso firme), `ABONADO` (confirmada y con pago retenido, ocupa agenda), `EN_EJECUCION` (el Azantys inició el servicio), `COMPLETADO` (finalizada) y `CANCELADO` (cerrada con motivo).

**Transiciones válidas:** `PENDIENTE → ABONADO → EN_EJECUCION → COMPLETADO`; `PENDIENTE → CANCELADO`; `ABONADO → CANCELADO`.

**Sinónimos aceptados:** EstadoServicio, CicloSunfyre

**No usar:** EstadoSunfyre, Estado (como String o int suelto), Status

**Ejemplo en código:**
```java
public enum DohaeroxJeda { PENDIENTE, ABONADO, EN_EJECUCION, COMPLETADO, CANCELADO }
```

---

### Gelior
**Definición:** El enumerado (Value Object) que define el ciclo de vida de un Kelitis (antes `EstadoKelitis`): si el pago del cliente está retenido en garantía, liberado al Azantys, completado o reembolsado al Zentys.

**Valores:** `RETENIDO` (el monto está en garantía), `LIBERADO` (el pago se liberó al Azantys), `COMPLETADO` (el Kelitis cerró su ciclo tras la liberación) y `REEMBOLSADO` (el monto volvió al Zentys).

**Transiciones válidas:** `RETENIDO → LIBERADO → COMPLETADO`; `RETENIDO → REEMBOLSADO`.

**Sinónimos aceptados:** EstadoGarantia, CicloKelitis

**No usar:** EstadoKelitis, EstadoEscrow, Estado (como String o int suelto)

**Ejemplo en código:**
```java
public enum Gelior { RETENIDO, LIBERADO, COMPLETADO, REEMBOLSADO }
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
| Especialidad / String especialidad | **Kostion** |
| Email / String email | **Runiapos** |
| Intereses / String intereses | **Indior** |
| DocumentoIdentidad / Cédula suelta | **Iksia** |
| FotoPerfil / Avatar suelto | **Laehurlion** |
| String nombreObra / Titulo | **Sete** |
| String descripcion / Detalle | **Jorva** |
| String urlImagen / Imagen suelta | **Urnebion** |
| Estado / Status de la obra | **Kanez** |
| EstadoSunfyre / Estado de la cita o del servicio | **DohaeroxJeda** |
| EstadoKelitis / Estado del escrow o la garantía | **Gelior** |