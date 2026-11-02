# Glosario del Lenguaje Ubicuo - Syrax Gallery

Syrax Gallery es un marketplace de arte con dos líneas de negocio: obras físicas (**Caraxes**) y servicios artísticos por encargo (**Sunfyre**). Los términos del dominio están en alto valyrio y se usan igual en las conversaciones, en los documentos y en el código (clases, métodos, endpoints y tablas).

**Agregados implementados:** `Sunfyre` y `Kelitis`, con sus raíces e invariantes protegidas en código.
**Entidades:** `Azantys`, `Zentys`, `Caraxes`, `Udvazir`, `Sunfyre` y `Kelitis`.
**Excepción de dominio:** `ReglaDominioException`. Toda regla de negocio rota la lanza el dominio y la API la responde con **422**.

---

## Actores

### Azantys
**Definición:** El Artista, Diseñador o Tatuador registrado en la plataforma. Es quien crea las obras (Caraxes) y ejecuta los servicios artísticos (Sunfyre). En la seguridad es el rol **AZANTYS** (vendedor).

**Composición:** `id`, `nombre`, `activo`, `Kostion` (especialidad), `Runiapos` (correo), `Iksia` (documento de identidad), `iksiaVerificada`, `Laehurlion` (foto de perfil), `laehurlionVerificado` e `identidadAprobada`.

**Creación:** Mediante la fábrica estática `Azantys.crear(id, nombre, kostion, runiapos)`. Todo Azantys nace activo, sin documentos y **pendiente de verificación**. El constructor es privado y la igualdad se define solo por `id`. `Azantys.reconstruir(...)` lo usa únicamente el adaptador de persistencia.

**Verificación de identidad (RN5), en dos pasos:**
1. El Azantys carga sus documentos: `verificarIksia(iksia)` y `subirLaehurlion(laehurlion)`. Solo él puede hacerlo: `validarTitular(idSolicitante)`. Queda **pendiente de revisión** (`estaPendienteDeRevision()`).
2. El Jovennio revisa: `aprobarIdentidad()` o `rechazarIdentidad(motivo)`. Al rechazar se borra la Iksia para que el Azantys la cargue de nuevo. El motivo es obligatorio y una identidad ya aprobada no se puede volver a revisar.

**Habilitación para publicar:** `estaHabilitadoParaPublicar()` es verdadero solo con Iksia verificada, foto de perfil **y** identidad aprobada. `validarHabilitacionParaPublicar()` es la versión que lanza `ReglaDominioException`; la usan los casos de uso de publicación de Sunfyre y Caraxes.

**Sinónimos aceptados:** Creador, Tatuador, Pintor, Muralista

**No usar:** Proveedor, Seller, UserArtista

**Ejemplo de uso en código:**
```java
Azantys nuevoArtista = Azantys.crear(id, "Rhaenyra Targaryen",
        new Kostion("Tatuaje realista y retratos"),
        new Runiapos("rhaenyra@syraxgallery.com"));
nuevoArtista.verificarIksia(new Iksia("1234567"));
nuevoArtista.subirLaehurlion(new Laehurlion("https://cdn.syrax.com/perfiles/az-001.jpg"));
nuevoArtista.aprobarIdentidad(); // lo hace el Jovennio
```

---

### Zentys
**Definición:** El Comprador, Cliente o Coleccionista. Es quien encarga y paga las Sunfyre y compra las Caraxes. En la seguridad es el rol **ZENTYS** (comprador).

**Composición:** `id`, `nombre`, `Runiapos` (correo), `Indior` (intereses artísticos) y `estaActivo`.

**Creación:** Mediante la fábrica estática `Zentys.crear(...)`. El nombre no puede estar vacío ni tener caracteres especiales. `validarPuedeComprar()` exige que el Zentys esté activo.

**Sinónimos aceptados:** Cliente, Comprador, Coleccionista

**No usar:** Customer, Client, CompradorId

**Ejemplo de uso en código:**
```java
Zentys comprador = zentysRepository.obtener("zentys-01")
        .orElseThrow(() -> new NoSuchElementException("Zentys no encontrado"));
```

---

### Jovennio
**Definición:** El Administrador de la plataforma. Es el actor que revisa los documentos de identidad que carga un Azantys y los aprueba o rechaza (CU-48). En la seguridad es el rol **ADMIN**: solo él puede usar `PUT /api/azantys/{id}/revision`.

**Nota de diseño:** Jovennio es un **actor**, no una entidad del dominio. Las reglas de la revisión (no aprobar sin documentos, no aprobar dos veces, motivo obligatorio al rechazar) viven en `Azantys`, porque es su identidad la que se revisa.

**Sinónimos aceptados:** Administrador

**No usar:** Admin (como entidad), Moderador, SuperUsuario

---

## Agregados

### Sunfyre
**Definición:** El agregado que representa un Servicio Artístico por Encargo (tatuajes, murales, retratos personalizados, customización) pactado entre un Azantys y un Zentys. Representa el arte que brilla por su personalización y que exige la ejecución directa del artista.

**Composición:** `id`, `idAzantys` (creador), `idZentys` (cliente), `Sari` (ventana de tiempo), `SunfyreKastor` (tipo de servicio), `Alion` (ubicación) y `valaCongelado` (`Vala` pactada). Además tiene dos estados que **no se reciben al crear**: `dohaeroxJeda` (ciclo de vida, nace `PENDIENTE`) y `kanez` (publicación, nace `BORRADOR`).

**Creación:** Mediante la fábrica estática `Sunfyre.solicitar(...)`, que valida que nada sea nulo y que la ventana no esté en el pasado. El constructor es privado, `id`, `idAzantys`, `idZentys` y `valaCongelado` son `final`, no hay setters y la igualdad se define solo por `id`. `Sunfyre.reconstruir(...)` lo usa únicamente el adaptador de persistencia.

**Operaciones del agregado:**
- **Publicación (`Kanez`):** `publicar(destino)`: de BORRADOR a EXHIBICION o EN_VENTA, y de EXHIBICION a EN_VENTA. Exige `dohaeroxJeda == PENDIENTE` y, para EN_VENTA, un precio mayor a cero. Antes, el caso de uso `PublicarSunfyre` verifica la autoría (`validarAutoria`) y que el Azantys esté habilitado (`Azantys.validarHabilitacionParaPublicar()`).
- **Ciclo de vida (`DohaeroxJeda`):** `confirmar()`, `reprogramar(nuevoSari)`, `iniciarEjecucion()`, `finalizar()` y `cancelar(motivo)`.
- **Quién puede actuar:**
    - `validarAutoria(idAzantys)`: solo el Azantys creador gestiona la Sunfyre (publicar, iniciar, finalizar, reprogramar).
    - `validarParticipante(id)`: solo el Azantys o el Zentys de la Sunfyre pueden consultarla o cancelarla.
    - `validarCliente(idZentys)`: solo el Zentys de la Sunfyre puede pagarla (constituir su Kelitis).
    - `validarCompletada()`: el Zentys solo puede aprobar el servicio cuando ya está COMPLETADO.

**Invariantes:**
1. **Creación completa.** Toda Sunfyre nace con `idAzantys`, `idZentys`, `sari`, `kastor`, `alion` y `valaCongelado` no nulos.
2. **Precio congelado.** `valaCongelado` nunca cambia durante el ciclo de vida.
3. **No solapamiento de agenda.** El `sari` de una Sunfyre nunca se cruza con el de otra Sunfyre `ABONADA` del mismo Azantys. Como necesita mirar **otras** Sunfyre, lo protege el servicio de dominio `VerificadorDeAgenda`, al confirmar (constituir el Kelitis) y al reprogramar.
4. **Reprogramación controlada.** `reprogramar(nuevoSari)` solo es válido si está `ABONADO` y la nueva ventana no está en el pasado.
5. **Camino único de transición.** `PENDIENTE → ABONADO → EN_EJECUCION → COMPLETADO`, mediante `confirmar() → iniciarEjecucion() → finalizar()`.
6. **Cancelación restringida.** `CANCELADO` solo se alcanza desde `PENDIENTE` o `ABONADO`. Un servicio personalizado no puede devolverse una vez iniciada su ejecución.
7. **Motivo obligatorio.** `cancelar(motivo)` exige un motivo no nulo ni vacío.
8. **Estados terminales.** `COMPLETADO` y `CANCELADO` son definitivos.
9. **Publicación controlada.** Solo se publica una Sunfyre `PENDIENTE`, con destino EXHIBICION o EN_VENTA; para EN_VENTA el precio debe ser mayor a cero.

**Límite del agregado:** Dentro quedan la cita, su precio y sus estados. Fuera quedan el Azantys, el Zentys y el Kelitis: la Sunfyre solo guarda sus ids y nunca modifica esos objetos.

**Sinónimos aceptados:** ServicioArtístico, EncargoPersonalizado

**No usar:** Trabajo, Contrato, Servicio, Prestación, Cita

**Ejemplo de uso en código:**
```java
Sunfyre sesionTatuaje = Sunfyre.solicitar(
        id,
        "az-001",
        "zentys-01",
        new Sari(inicio, fin),
        SunfyreKastor.ARTE_PIEL,
        new Alion("Armenia", "Colombia", 4.53, -75.68),
        new Vala(150000, "COP")
);
sesionTatuaje.publicar(Kanez.EN_VENTA);
```

---

### Kelitis
**Definición:** El depósito en garantía (Escrow). Retiene de forma segura el pago del Zentys por una Sunfyre y no se lo libera al Azantys hasta que el servicio esté terminado y aprobado.

**Composición:** `id`, `idSunfyre`, `idAzantys`, `idZentys`, `valaCongelado` (monto retenido) y `gelior` (estado). Nace siempre `RETENIDO`.

**Creación:** Mediante la fábrica estática `Kelitis.realizar(id, idSunfyre, idAzantys, idZentys, vala)`. El constructor es privado y la igualdad se define solo por `id`.

**Operaciones del agregado:**
- `liberarPago()`: RETENIDO → LIBERADO. Ocurre cuando el Zentys aprueba el servicio terminado.
- `completar()`: LIBERADO → COMPLETADO.
- `solicitarReembolso(motivo)`: RETENIDO → REEMBOLSADO, con motivo obligatorio. Ocurre al cancelar la Sunfyre.
- `validarCliente(idZentys)`: solo el Zentys del servicio puede aprobarlo.
- `validarParticipante(id)`: solo el Azantys o el Zentys del servicio pueden consultarlo.

**Invariantes:**
1. **Creación completa.** Nace con `idSunfyre`, `idAzantys`, `idZentys` y `valaCongelado` no nulos, siempre en `RETENIDO`.
2. **Monto congelado.** Ninguna operación cambia `valaCongelado`; solo cambia el estado.
3. **Liberación desde RETENIDO.** `liberarPago()` solo desde `RETENIDO`; `completar()` solo desde `LIBERADO`.
4. **Reembolso desde RETENIDO.** `solicitarReembolso(motivo)` solo desde `RETENIDO` y con motivo.
5. **Exclusividad de caminos.** O `RETENIDO → LIBERADO → COMPLETADO`, o `RETENIDO → REEMBOLSADO`. Nunca ambos.
6. **Estados terminales.** `COMPLETADO` y `REEMBOLSADO` son definitivos.
7. **Un solo Kelitis por Sunfyre.** Una Sunfyre se paga una sola vez. Como necesita consultar otros Kelitis, lo protege el servicio de dominio `VerificadorDeKelitis`.

**Sinónimos aceptados:** DepósitoEnGarantía

**No usar:** Escrow, Garantía, Pago, Depósito (como nombres de clase)

**Ejemplo de uso en código:**
```java
Kelitis deposito = Kelitis.realizar(id, sunfyre.getId(), sunfyre.getIdAzantys(),
        sunfyre.getIdZentys(), sunfyre.getValaCongelado());
deposito.liberarPago();
```

---

## Otras entidades

### Caraxes
**Definición:** Una obra de Arte Físico individual, original y tangible (pintura, escultura, fotografía, dibujo, grabado) que se envía por logística tradicional. Cada Caraxes es única e identificable por su `id`.

**Composición:** `id`, `idAzantys` (creador), `Vala` (precio), `Sete` (nombre), `Jorva` (descripción), `Urnebion` (imagen de referencia), `CaraxesKastor` (tipo de obra) y `Kanez` (estado). Toda Caraxes nace en `BORRADOR`.

**Operaciones:** `publicar(destino)` (BORRADOR → EXHIBICION o EN_VENTA, y EXHIBICION → EN_VENTA; exige precio mayor a cero para EN_VENTA), `marcarComoVendido()` (EN_VENTA → VENDIDO), `retirar()` (cualquier estado excepto VENDIDO y RETIRADO → RETIRADO) y `validarAutoria(idAzantys)`. La habilitación del Azantys se valida en el caso de uso con `Azantys.validarHabilitacionParaPublicar()`.

**Creación:** Mediante la fábrica estática `Caraxes.crear(...)`. El constructor es privado, no hay setters y la igualdad se define solo por `id`.

**Sinónimos aceptados:** ObraFísica, ArteTangible

**No usar:** Producto, Cuadro, Mercancía

**Ejemplo de uso en código:**
```java
Caraxes nuevaPintura = Caraxes.crear(
        id,
        "az-001",
        new Vala(1200000, "COP"),
        new Sete("Noche Estrellada"),
        new Jorva("Paisaje al atardecer pintado con técnica de óleo"),
        new Urnebion("https://cdn.syrax.com/obras/paisaje.jpg"),
        CaraxesKastor.PINTURA
);
```

---

### Udvazir
**Definición:** El comentario que un Zentys deja sobre una obra. Se compone de `id`, `zentysId`, `caraxesId` y `texto`. Se crea con `Udvazir.crear(...)` y el texto no puede estar vacío.

**Sinónimos aceptados:** Comentario, Reseña

**No usar:** Review, Comment, Opinion

---

## Comandos del negocio

### Dracarys
**Definición:** El comando que procesa el pago de una Sunfyre. Cuando el Zentys ejecuta un Dracarys, el dinero queda retenido en un Kelitis y la Sunfyre pasa de `PENDIENTE` a `ABONADO`.

**En el código:** Es el caso de uso `ConstituirKelitis` (`POST /api/kelitis`, solo rol ZENTYS). En orden:
1. Valida que quien paga es el cliente de la Sunfyre (`validarCliente`).
2. Valida que no exista ya un Kelitis para esa Sunfyre (`VerificadorDeKelitis`).
3. Valida que la cita no se cruce con la agenda del Azantys (`VerificadorDeAgenda`).
4. Confirma la Sunfyre y crea el Kelitis.

**Sinónimos aceptados:** Pagar, Abonar

**No usar:** Checkout, Comprar, ProcesarPago

---

## Servicios de dominio

Reglas de negocio que necesitan mirar **otros** objetos además del que se está modificando. No caben dentro de una sola entidad, pero tampoco van en el caso de uso. Viven en `domain/service`, no usan Spring y se registran como `@Bean` en `DomainServiceConfig`.

### VerificadorDeAgenda
**Definición:** Protege la invariante 3 de Sunfyre: el `sari` de una Sunfyre no se cruza con otra Sunfyre `ABONADA` del mismo Azantys. Excluye a la propia Sunfyre para que no choque consigo misma.

**Ejemplo en código:**
```java
verificadorDeAgenda.verificarDisponibilidad(sunfyre.getIdAzantys(), nuevoSari, sunfyre.getId());
```

### VerificadorDeKelitis
**Definición:** Protege la invariante 7 de Kelitis: una Sunfyre tiene como máximo un Kelitis.

**Ejemplo en código:**
```java
verificadorDeKelitis.verificarQueNoTengaKelitis(sunfyreId);
```

---

## Objetos de Valor (Value Objects)

### Vala
**Definición:** El precio o dinero dentro del marketplace. Une el monto con su divisa en un solo objeto inmutable.

**Sinónimos aceptados:** Precio, Dinero, Importe, Monto

**No usar:** Double, Float, PrecioId, Costo

**Precondiciones:** El monto no puede ser negativo y la divisa debe ser un código ISO de 3 letras.

**Ejemplo de uso en código:**
```java
Vala precio = new Vala(150000, "COP");
```

---

### Alion
**Definición:** La ubicación geográfica de un servicio o de un artista: ciudad, país, latitud y longitud en una sola estructura. Se escribe `Alion` en el código y Ālion en la documentación.

**Sinónimos aceptados:** Ubicación, Dirección, Geolocalización

**No usar:** String ciudad, double lat, UbicacionId

**Precondiciones:** Ciudad y país no pueden estar vacíos; latitud y longitud deben estar dentro de sus rangos válidos.

**Ejemplo de uso en código:**
```java
Alion ubicacion = new Alion("Armenia", "Colombia", 4.5339, -75.6811);
```

---

### Sari
**Definición:** La ventana de tiempo inmutable de una cita: un inicio y un fin.

**Sinónimos aceptados:** VentanaTemporal, Horario, AgendaSlot

**No usar:** FechaInicio, RangoFechas, CalendarioId

**Precondiciones:** Inicio y fin son obligatorios y el fin debe ser posterior al inicio. La regla de "no en el pasado" es de **agendar**, así que la aplica la Sunfyre con `iniciaEnElPasado()` y no el constructor. Si estuviera en el constructor, una Sunfyre guardada no podría reconstruirse después de su cita.

**Comportamiento:** `seSolapaCon(otro)`: dos ventanas se cruzan si una empieza antes de que la otra termine. Que una termine justo cuando la otra empieza no es cruce.

**Ejemplo de uso en código:**
```java
public record Sari(LocalDateTime inicio, LocalDateTime fin) {
    public boolean seSolapaCon(Sari otro) {
        return this.inicio.isBefore(otro.fin()) && otro.inicio().isBefore(this.fin);
    }
}
```

---

### Kostion
**Definición:** La especialidad artística de un Azantys.

**Sinónimos aceptados:** Especialidad, EspecialidadArtística

**No usar:** String especialidad, TipoArtista

**Precondiciones:** No puede estar vacía y debe tener entre 3 y 100 caracteres.

---

### Runiapos
**Definición:** El correo electrónico de un Azantys o un Zentys. También es el usuario con el que inicia sesión.

**Sinónimos aceptados:** Correo, CorreoElectrónico

**No usar:** Email, String email, Mail

**Precondiciones:** No puede estar vacío, debe contener "@" y al menos un punto, y no puede empezar ni terminar con "@".

---

### Indior
**Definición:** Los intereses artísticos de un Zentys.

**Sinónimos aceptados:** Intereses, PreferenciasArtísticas

**No usar:** String intereses, Tags

**Precondiciones:** No puede estar vacío, solo admite letras, números, tildes, ñ y puntuación básica, y debe tener entre 10 y 300 caracteres.

---

### Iksia
**Definición:** El número de documento de identidad de un Azantys, base de la verificación de identidad.

**Sinónimos aceptados:** DocumentoIdentidad, Cédula

**No usar:** String documento, CedulaId

**Precondiciones:** No puede estar vacío y debe tener entre 6 y 10 dígitos.

---

### Laehurlion
**Definición:** La fotografía de perfil de un Azantys, requisito, junto con la identidad aprobada, para poder publicar.

**Sinónimos aceptados:** FotoPerfil, Avatar

**No usar:** String foto, ProfilePicture

**Precondiciones:** La URL no puede estar vacía.

---

### Sete
**Definición:** El nombre o título de una obra.

**Sinónimos aceptados:** NombreObra, TituloObra

**No usar:** String nombre, Titulo

**Precondiciones:** No puede estar vacío, no admite caracteres especiales y debe tener entre 3 y 100 caracteres.

---

### Jorva
**Definición:** La descripción o historia de una obra.

**Sinónimos aceptados:** DescripcionObra, HistoriaObra

**No usar:** String descripcion, Detalle

**Precondiciones:** No puede estar vacía y debe tener entre 10 y 1000 caracteres.

---

### Urnebion
**Definición:** La URL de la imagen de referencia de una obra. Toda obra publicada debe tener al menos una imagen.

**Sinónimos aceptados:** ImagenObra, ImagenReferencia

**No usar:** String urlImagen, Imagen, Foto

**Precondiciones:** La URL no puede estar vacía.

---

## Enumerados del dominio (Value Objects)

### CaraxesKastor
**Definición:** Los tipos válidos de obra física: `PINTURA`, `ESCULTURA`, `FOTOGRAFIA`, `DIBUJO` y `GRABADO`.

---

### SunfyreKastor
**Definición:** Los tipos válidos de servicio artístico: `ARTE_PIEL`, `MURAL`, `RETRATO_TRADICIONAL` y `CUSTOMIZACION`.

---

### Kanez
**Definición:** El estado de publicación de una obra o servicio dentro de la galería: `BORRADOR`, `EXHIBICION`, `EN_VENTA`, `VENDIDO` y `RETIRADO`. Es compartido por Caraxes y Sunfyre. En una Sunfyre es **independiente** de `DohaeroxJeda`: `Kanez` dice si el servicio está publicado y `DohaeroxJeda` dice en qué punto de su ejecución está.

**Sinónimos aceptados:** EstadoPublicacion

**No usar:** Estado (como String suelto), Status

---

### DohaeroxJeda
**Definición:** El ciclo de vida de una Sunfyre.

**Valores:** `PENDIENTE` (solicitada, sin compromiso firme), `ABONADO` (pagada con un Dracarys, ocupa la agenda), `EN_EJECUCION` (el Azantys inició el servicio), `COMPLETADO` (terminada) y `CANCELADO` (cerrada con motivo).

**Transiciones válidas:** `PENDIENTE → ABONADO → EN_EJECUCION → COMPLETADO`; `PENDIENTE → CANCELADO`; `ABONADO → CANCELADO`.

**No usar:** EstadoSunfyre, Estado (como String suelto), Status

---

### Gelior
**Definición:** El ciclo de vida de un Kelitis.

**Valores:** `RETENIDO` (el dinero está en garantía), `LIBERADO` (el pago se liberó al Azantys), `COMPLETADO` (el Kelitis cerró su ciclo) y `REEMBOLSADO` (el dinero volvió al Zentys).

**Transiciones válidas:** `RETENIDO → LIBERADO → COMPLETADO`; `RETENIDO → REEMBOLSADO`.

**No usar:** EstadoKelitis, EstadoEscrow, Estado (como String suelto)

---

## Roles de seguridad

| Rol | Actor del dominio | Ejemplos de lo que solo él puede hacer |
|---|---|---|
| AZANTYS | Azantys (vendedor) | Crear, publicar, iniciar, finalizar y reprogramar Sunfyre; cargar su identidad; ver sus servicios |
| ZENTYS | Zentys (comprador) | Pagar una Sunfyre (Dracarys / constituir Kelitis) y aprobar el servicio terminado |
| ADMIN | Jovennio | Aprobar o rechazar la identidad de un Azantys |

El rol viaja en el token JWT. Además del rol, el dominio valida que la persona sea la dueña del recurso: por ejemplo, un AZANTYS no puede publicar la Sunfyre de otro (`validarAutoria`).

---

## Anti-patrones (términos a EVITAR)

| No usar | Usar |
|---|---|
| Producto / Obra física | **Caraxes** |
| Servicio / Contrato / Cita | **Sunfyre** |
| Seller / Artista / Vendedor (como clase) | **Azantys** |
| Customer / Cliente / Comprador (como clase) | **Zentys** |
| Admin / Moderador (como actor) | **Jovennio** |
| Escrow / Garantía / Depósito | **Kelitis** |
| Checkout / Pagar / ProcesarPago | **Dracarys** |
| Review / Comentario | **Udvazir** |
| Double / Float / Precio | **Vala** |
| String ciudad / Coordenadas / Dirección | **Alion** |
| RangoFechas / Calendario / Horario | **Sari** |
| Subcategoría / TipoProducto | **CaraxesKastor / SunfyreKastor** |
| Especialidad / String especialidad | **Kostion** |
| Email / String email | **Runiapos** |
| Intereses / String intereses | **Indior** |
| DocumentoIdentidad / Cédula suelta | **Iksia** |
| FotoPerfil / Avatar suelto | **Laehurlion** |
| String nombreObra / Título | **Sete** |
| String descripción / Detalle | **Jorva** |
| String urlImagen / Imagen suelta | **Urnebion** |
| Estado / Status de la publicación | **Kanez** |
| EstadoSunfyre / Estado de la cita | **DohaeroxJeda** |
| EstadoKelitis / Estado del escrow | **Gelior** |

---

## Términos retirados del proyecto

`Seasmoke` (reproducciones y merchandising), `Dreamfyre` (arte digital y NFTs) y `Lentor` (estudios y colectivos), junto con `SeasmokeKastor` y `DreamfyreKastor`, se retiraron del alcance del proyecto y no existen en el código.