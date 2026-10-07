# Diseño de DTOs y mapeo Dominio → API

Convención general: los DTOs viven en `application/dto`. 
Los Request validan solo que los campos obligatorios vengan (`@NotBlank`, `@NotNull`); 
las reglas de formato y de negocio las aplican los Value Objects y las entidades del dominio.

---

## 1. Caraxes (obra física)

### RegistrarCaraxesRequest → Caraxes.crear(...)

| Campo | Por qué es necesario |
|---|---|
| azantysId | Una Caraxes siempre tiene creador; el caso de uso verifica que el Azantys exista |
| monto, divisa | Forman el Vala (monto >= 0, divisa ISO de 3 letras) |
| sete | Nombre de la obra (3 a 100 caracteres) |
| jorva | Descripción de la obra (10 a 1000 caracteres) |
| urnebion | URL de la imagen de referencia. Regla: toda obra publicada debe tener al menos una imagen |
| kastor | Tipo de obra (CaraxesKastor). Regla: todo producto pertenece a una categoría |

No se reciben:
- `id`: lo genera el servidor.
- `kanez`: toda Caraxes nace en BORRADOR.

### CaraxesDetalleResponse (respuesta de crear, obtener y publicar)

| Campo | Por qué es necesario |
|---|---|
| id | Identidad de la obra; el cliente la usa en los demás endpoints |
| azantysId | Creador de la obra |
| monto, divisa | Precio (Vala) |
| sete, jorva | Nombre y descripción para mostrar la obra |
| urnebion | Imagen de referencia para el comprador |
| kanez | Estado de publicación (BORRADOR, EXHIBICION, EN_VENTA, VENDIDO, RETIRADO) |
| kastor | Tipo de obra |

### Publicar: sin body
El destino viaja en la ruta (`{kanez}`) porque la operación solo necesita ese valor.
El Azantys creador se obtiene de la propia Caraxes y se valida con `puedePublicar()`.

---

## 2. Sunfyre (servicio artístico)

### CrearSunfyreRequest → Sunfyre.solicitar(...)

| Campo | Por qué es necesario |
|---|---|
| azantysId | Invariante 1: no existe una Sunfyre sin creador |
| zentysId | Invariante 1: no existe sin cliente |
| inicio, fin | Forman el Sari; el fin debe ser posterior al inicio y no estar en el pasado |
| kastor | Tipo de servicio (SunfyreKastor). Regla: todo servicio pertenece a una categoría |
| ciudad, pais, latitud, longitud | Forman el Alion. Regla: todo servicio especifica su ubicación |
| monto, divisa | Forman el Vala pactado, que se congela (invariante 2) |

No se reciben:
- `id`: lo genera el servidor.
- `estado` (DohaeroxJeda): toda Sunfyre nace PENDIENTE.
- `kanez`: toda Sunfyre nace en BORRADOR.

### PublicarSunfyreRequest → sunfyre.publicar(destino)

| Campo | Por qué es necesario |
|---|---|
| azantysId | Actor que publica: se valida que sea el creador y que cumpla `puedePublicar()` (identidad verificada y foto de perfil) |
| destino | Kanez objetivo (EXHIBICION o EN_VENTA) |

El `id` de la Sunfyre va en la ruta. Aquí el destino va en el body (y no en la ruta como en
Caraxes) porque la operación también necesita el `azantysId` del actor.

### SunfyreDetalleResponse (respuesta de crear, obtener y publicar)

| Campo | Por qué es necesario |
|---|---|
| id, idAzantys, idZentys | Identifican el servicio y las dos partes |
| inicio, fin | Ventana de tiempo (Sari) |
| kastor | Tipo de servicio |
| ciudad, pais | Ubicación del servicio (Alion) |
| monto, divisa | Precio congelado (valaCongelado) |
| estado | DohaeroxJeda: en qué punto del ciclo de vida está el servicio |
| kanez | Estado de publicación |

---

## 3. Azantys (artista)

### RegistrarAzantysRequest → Azantys.crear(...)

| Campo | Por qué es necesario |
|---|---|
| id | Identidad del Azantys; el dominio exige un id no vacío |
| nombre | Un Azantys siempre tiene nombre (no vacío) |
| kostion | Especialidad artística (3 a 100 caracteres); se convierte en el VO Kostion |
| runiapos | Correo con formato válido; se convierte en el VO Runiapos |
| lentorId | Estudio al que pertenece. Es opcional: un artista puede ser independiente, pero si viene no puede ser vacío |

No se reciben la identidad ni la foto: se completan después, en sus propias operaciones.

### VerificarIdentidadRequest → azantys.verificarIdentidad(iksia)

| Campo | Por qué es necesario |
|---|---|
| iksia | Número de documento (6 a 10 dígitos). Es la base de la regla "el vendedor debe completar su identificación para publicar" |

El `id` del Azantys va en la ruta.

### AzantysDetalleResponse

| Campo | Por qué es necesario |
|---|---|
| id | Identidad del artista |
| nombre, kostion, runiapos | Datos del perfil |
| lentorId | Estudio al que pertenece (puede ser nulo) |
| activo | Si la cuenta está habilitada |
| iksiaVerificada, laehurlionVerificado | Indican si ya cumple los dos requisitos para publicar |

No se devuelve el número de documento (`iksia`): es un dato sensible y el cliente solo
necesita saber si ya fue verificado.

---