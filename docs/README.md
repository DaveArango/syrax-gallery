# Syrax Gallery

Marketplace de arte donde los artistas (**Azantys**) venden obras físicas (**Caraxes**) y ofrecen servicios artísticos por encargo (**Sunfyre**: tatuajes, murales, retratos, customización). El pago del cliente (**Zentys**) queda retenido en un depósito en garantía (**Kelitis**) hasta que el servicio se ejecuta y el cliente lo aprueba.

Proyecto final de Programación Avanzada (01N), Universidad del Quindío. **Entrega 2: backend completo.**

## Integrantes

| Integrante | Parte principal |
|---|---|
| Valery Andrea Ortiz Mesa | Azantys, Sunfyre y Kelitis (dominio, casos de uso, API, persistencia, consultas y seguridad por usuario) |
| Jhonatan David Vivas Arango | Zentys y Caraxes, configuración JWT, Swagger y manejo de errores |

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
| POST | /api/zentys | Público (registro, crea también la cuenta de login) |
| GET | /api/zentys/{id} | Con token |
| GET | /api/zentys?busqueda=&page=&size= | Con token |
| GET | /api/zentys/mas-activos?limite= | Con token (ranking) |

### Caraxes
| Método | Endpoint | Acceso |
|---|---|---|
| GET | /api/caraxes?texto=&kastor=&page=&size= | Público (catálogo) |
| POST | /api/caraxes | AZANTYS |
| GET | /api/caraxes/{id} | Con token |
| PUT | /api/caraxes/{id}/publicar | AZANTYS (el creador) |
| PUT | /api/caraxes/{id}/retirar | AZANTYS (el creador) |
| PUT | /api/caraxes/{id}/vender | ZENTYS |

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

En la Entrega 1 no recibimos observaciones. Aun así, hicimos estas mejoras sobre lo entregado:

| Mejora | Qué cambiamos | Dónde se ve (archivo o rama) |
|---|---|---|
| Lenguaje ubicuo en los estados | EstadoSunfyre pasó a DohaeroxJeda y EstadoKelitis a Gelior | `DohaeroxJeda.java`, `Gelior.java`, rama `fix/correccion-lenguaje-ubicuo` |
| Invariantes de los agregados | Se corrigieron las invariantes de Sunfyre y Kelitis | `diagrama-agregados.md`, `Sunfyre.java`, `Kelitis.java`, rama `fix/coherencia-codigo` |
| Reglas fuera de los casos de uso | La regla "solo se aprueba un servicio terminado" pasó a `Sunfyre.validarCompletada()`, y "un solo Kelitis por Sunfyre" al servicio de dominio `VerificadorDeKelitis` | rama `refactor/reglas-kelitis-en-dominio` |

## Pruebas

`./gradlew test` ejecuta:
- Pruebas de dominio: entidades, Value Objects y servicios de dominio.
- Pruebas de casos de uso con Mockito: `CancelarSunfyreUseCaseTest`, `ConstituirKelitisUseCaseTest`, `AprobarEjecucionKelitisUseCaseTest` y `PublicarSunfyreUseCaseTest`, incluidos los casos que fallan por reglas de negocio.
- Pruebas de controller con `@WebMvcTest`.
- Prueba de repositorio con `@DataJpaTest`: `SunfyreRepositoryDataJpaTest`.

## Pruebas avanzadas

**1. ¿Por qué el repositorio no se prueba con Mockito?**

Si se simula el repositorio, la prueba solo comprueba lo que nosotros mismos le dijimos que devolviera. Con H2 de verdad
(RepositoriosJpaTest, SunfyreRepositoryDataJpaTest, KelitisRepositoryDataJpaTest) se comprueba que el mapper
de persistencia guarda y reconstruye bien los value objects (Sari, Vala, Alion), que las consultas del catálogo paginan
y ordenan como esperamos y que el ranking de Azantys cuenta solo las Sunfyre COMPLETADAS de data.sql.

**2. ¿Qué diferencia hay entre @SpringBootTest y @DataJpaTest?**

Con @DataJpaTest solo se levanta la parte de la base de datos, o sea, las entidades, los repositorios de Spring Data y H2.
Por eso es más rápido, pero no carga nuestras clases, así que el adaptador y su mapper los tuvimos que traer con
@Import(KelitisRepositoryJpa.class, KelitisPersistenceMapper.class). @SpringBootTest en cambio, levanta toda la
aplicación, con la seguridad y los casos de uso incluidos. En Spring Boot 4 @DataJpaTest cambió de paquete y ahora se
importa de org.springframework.boot.data.jpa.test.autoconfigure y hay que agregar el starter
spring-boot-starter-data-jpa-test en el build.gradle, y así lo solucionamos.
Lo que más nos costó fue saber cuál usar en cada caso, al final entendimos que si solo queremos probar que algo se guarda
y se lee bien seria con @DataJpaTest, y que @SpringBootTest lo dejamos para cuando necesitamos la app completa
como en la seguridad o en la integración.

**3. ¿Cómo probamos la seguridad?**

Escogimos la ruta GET /api/sunfyre/mis-servicios, que solo puede usar un Azantys. Sin token responderia 401 porque el
sistema no sabe quién está haciendo la petición. Con el token de un Zentys responde 403 porque sí sabe quién es pero no
tiene permiso, y con el token de un Azantys responde 200. Como en los tests de controller apagamos los filtros, ahí no se
puede probar la seguridad, entonces SeguridadEndpointTest usa @SpringBootTest con @AutoConfigureMockMvc para que cargue
nuestro SecurityConfig de verdad.
Lo que más nos costó fue entender cómo simular el token, pensábamos que había que hacer login en cada prueba, pero con
jwt() de spring-security-test se crea un token de mentiras al que le ponemos el uid y el rol, y Spring lo toma como si
la persona ya se hubiera autenticado.

**4. ¿En qué se diferencia una prueba de integración de una unitaria?**

En FlujoSunfyreIntegracionTest no simulamos nada pues la petición pasa por la seguridad, el controller, el caso de uso,
el dominio, el repositorio y llega a H2. Por ejemplo un Zentys paga la Sunfyre sf-007 y después revisamos directamente
en la base de datos que quedó ABONADA y que se creó su Kelitis. Una prueba unitaria prueba una sola clase y lo demás lo
simula con Mockito, por eso es rápida, y si falla se sabe exactamente dónde está el problema. La de integración es más
lenta, pero muestra que todas las piezas sí funcionan juntas, así que conviene usarla en los flujos más importantes y no
en cada caso pequeño.
Lo que más nos costó fue escoger los datos de data.sql, para pagar necesitábamos una Sunfyre PENDIENTE de ese mismo Zentys,
y para que cancelar fallara una que ya estuviera COMPLETADA, así que nos tocó revisar el archivo con calma para no
equivocarnos de estado.