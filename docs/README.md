# Syrax Gallery

Marketplace de arte donde los artistas (**Azantys**) venden obras físicas (**Caraxes**) y ofrecen servicios artísticos por encargo (**Sunfyre**: tatuajes, murales, retratos, customización). El pago del cliente (**Zentys**) queda retenido en un depósito en garantía (**Kelitis**) hasta que el servicio se ejecuta y el cliente lo aprueba.

Proyecto final de Programación Avanzada (01N), Universidad del Quindío. **Entrega 2: backend completo.**

## Integrantes

| Integrante | Parte principal |
|---|---|
| Valery Andrea Ortiz Mesa | Azantys, Sunfyre y Kelitis (dominio, casos de uso, API, persistencia, consultas y seguridad por usuario) |
| [nombre completo de Dave] | Zentys y Caraxes, configuración JWT, Swagger y manejo de errores |

## Lenguaje ubicuo (alto valyrio)

| Término | Significado |
|---|---|
| Azantys | Artista (vendedor) |
| Zentys | Cliente (comprador) |
| Sunfyre | Servicio artístico por encargo |
| Kelitis | Depósito en garantía (escrow) del pago de una Sunfyre |
| Caraxes | Obra física |
| Jovennio | Administrador (revisa la identidad de los Azantys) |
| DohaeroxJeda | Ciclo de vida de la Sunfyre: PENDIENTE, ABONADO, EN_EJECUCION, COMPLETADO, CANCELADO |
| Gelior | Estado del Kelitis: RETENIDO, LIBERADO, COMPLETADO, REEMBOLSADO |
| Kanez | Publicación: BORRADOR, EXHIBICION, EN_VENTA, VENDIDO, RETIRADO |

El glosario completo está en `docs/glosario-lenguaje-ubicuo.md`.

## Cómo ejecutar

Requisitos: Java 21.

```bash
./gradlew bootRun
```

- Swagger: http://localhost:8080/swagger-ui.html
- Consola H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:syraxdb`, usuario `sa`, sin contraseña)
- Pruebas: `./gradlew test`

La base de datos es H2 en memoria. Al arrancar se cargan los datos de prueba de `src/main/resources/data.sql` y los usuarios de `UsuariosIniciales`.

### Cómo usar el token en Swagger

1. `POST /api/auth/login` con `{"username": "<correo>", "password": "<clave>"}`.
2. Copie el valor de `token` de la respuesta.
3. Clic en **Authorize**, pegue el token (sin escribir "Bearer") y acepte.

## Usuarios de prueba

| Correo | Contraseña | Rol | Id en el dominio |
|---|---|---|---|
| admin@syraxgallery.com | admin123 | ADMIN (Jovennio) | admin |
| rhaenyra@syraxgallery.com | rhaenyra123 | AZANTYS | az-001 (identidad aprobada) |
| daemon@syraxgallery.com | daemon123 | AZANTYS | az-002 (sin verificar) |
| laena@syraxgallery.com | laena123 | AZANTYS | az-003 (sin foto de perfil) |
| baela@syraxgallery.com | baela123 | AZANTYS | az-004 (identidad aprobada) |
| alicent.hightower@gmail.com | alicent123 | ZENTYS | zentys-01 |
| aegon.targaryen@gmail.com | aegon123 | ZENTYS | zentys-02 |
| helaena.targaryen@gmail.com | helaena123 | ZENTYS | zentys-03 |

Los usuarios nuevos que se registran deben usar una contraseña de mínimo 8 caracteres con una mayúscula y un número.

## Endpoints principales

Quien hace la acción (Azantys o Zentys) se toma del token, no del cuerpo de la petición.

### Autenticación
| Método | Endpoint | Acceso |
|---|---|---|
| POST | /api/auth/login | Público |

### Azantys
| Método | Endpoint | Acceso |
|---|---|---|
| POST | /api/azantys | Público (registro, crea también la cuenta de login) |
| GET | /api/azantys?busqueda=&page=&size= | Público |
| GET | /api/azantys/{id} | Público |
| GET | /api/azantys/mas-solicitados?limite= | Público (ranking) |
| PUT | /api/azantys/{id}/identidad | AZANTYS (solo el propio) |
| PUT | /api/azantys/{id}/revision | ADMIN |

### Sunfyre
| Método | Endpoint | Acceso |
|---|---|---|
| GET | /api/sunfyre?ciudad=&kastor=&precioMin=&precioMax=&page=&size= | Público (catálogo) |
| POST | /api/sunfyre | AZANTYS |
| GET | /api/sunfyre/{id} | Azantys o Zentys de la Sunfyre |
| GET | /api/sunfyre/mis-servicios?estado=&page=&size= | AZANTYS |
| PUT | /api/sunfyre/{id}/publicar | AZANTYS (el creador) |
| PUT | /api/sunfyre/{id}/iniciar | AZANTYS (el creador) |
| PUT | /api/sunfyre/{id}/finalizar | AZANTYS (el creador) |
| PUT | /api/sunfyre/{id}/reprogramar | AZANTYS (el creador) |
| PUT | /api/sunfyre/{id}/cancelar | Azantys o Zentys de la Sunfyre |

### Kelitis
| Método | Endpoint | Acceso |
|---|---|---|
| POST | /api/kelitis | ZENTYS (el cliente de la Sunfyre) |
| GET | /api/kelitis/{id} | Azantys o Zentys del servicio |
| PUT | /api/kelitis/{id}/aprobar | ZENTYS (el cliente) |

### Zentys
| Método | Endpoint | Acceso |
|---|---|---|
| POST | /api/zentys | Registro |
| GET | /api/zentys/{id} | Con token |
| GET | /api/zentys?busqueda=&page=&size= | Con token |
| GET | /api/zentys/mas-activos?limite= | Con token (ranking) |

### Caraxes
| Método | Endpoint | Acceso |
|---|---|---|
| GET | /api/caraxes?texto=&kastor=&page=&size= | Público (catálogo) |
| POST | /api/caraxes | Con token |
| GET | /api/caraxes/{id} | Con token |
| PUT | /api/caraxes/{id}/publicar | Con token |
| PUT | /api/caraxes/{id}/retirar | Con token |
| PUT | /api/caraxes/{id}/vender | Con token |

### Códigos de error

Todas las respuestas de error tienen la forma `{"timestamp": ..., "status": ..., "mensaje": ...}`.

| Código | Cuándo |
|---|---|
| 400 | La petición viene mal: falta un campo, JSON mal formado, parámetro inválido, página negativa |
| 401 | No se envió token, o el token es inválido o venció |
| 403 | El token es válido pero el rol no tiene permiso para esa ruta |
| 404 | El recurso no existe |
| 422 | La petición está bien formada pero rompe una regla del negocio (la lanza el dominio) |

## Flujo de punta a punta

1. Login como **Rhaenyra** (AZANTYS).
2. `POST /api/sunfyre` con `zentysId: "zentys-01"` y una ventana futura, por ejemplo `2027-09-10T10:00:00` a `2027-09-10T12:00:00`.
3. `PUT /api/sunfyre/{id}/publicar` con `{"destino": "EN_VENTA"}`.
4. Login como **Alicent** (ZENTYS) y `POST /api/kelitis` con `{"sunfyreId": "{id}"}`. El Kelitis queda RETENIDO y la Sunfyre ABONADO.
5. Login como **Rhaenyra**: `PUT /api/sunfyre/{id}/iniciar` y luego `PUT /api/sunfyre/{id}/finalizar`.
6. Login como **Alicent**: `PUT /api/kelitis/{idKelitis}/aprobar`. El pago queda LIBERADO.

Errores para mostrar:
- Sin token, `POST /api/sunfyre` responde **401**.
- Con el token de Alicent, `POST /api/sunfyre` responde **403**.
- Con el token de Alicent, cancelar la Sunfyre mientras está EN_EJECUCION responde **422**: "Solo una Sunfyre PENDIENTE o ABONADA puede cancelarse".

## Correcciones de la Entrega 1

| Observación que nos dieron | Qué cambiamos | Dónde se ve (archivo o rama) |
|---|---|---|
| No hubieron observaciones | Los estados se renombraron al lenguaje ubicuo: EstadoSunfyre pasó a DohaeroxJeda y EstadoKelitis a Gelior | `DohaeroxJeda.java`, `Gelior.java`, rama `fix/correccion-lenguaje-ubicuo` |
| " | Se corrigieron las invariantes de Sunfyre y Kelitis | `docs/diagrama-agregado/diagrama-agregados.md`, `Sunfyre.java`, `Kelitis.java`, rama `fix/coherencia-codigo` |


## Pruebas

`./gradlew test` ejecuta:
- Pruebas de dominio: entidades, Value Objects y servicios de dominio.
- Pruebas de casos de uso con Mockito: `CancelarSunfyreUseCaseTest`, `ConstituirKelitisUseCaseTest`, `AprobarEjecucionKelitisUseCaseTest` y `PublicarSunfyreUseCaseTest`, incluidos los casos que fallan por reglas de negocio.
- Pruebas de controller con `@WebMvcTest`.
- Prueba de repositorio con `@DataJpaTest`: `SunfyreRepositoryJpaTest`.
