# Mapeo Dominio 
# API - Caraxes

|Operación del dominio|Método HTTP|Endpoint|
|---|---|---|
| caraxes.crear(...) | POST | /api/caraxes |
| caraxes.get(...) | GET | /api/caraxes/{id} |
| caraxes.publicar(...) | PUT | /api/caraxes/{id}/publicar |
| caraxes.retirar(...) | PUT | /api/caraxes/{id}/retirar |
| caraxes.marcarComoVendido(...) | PUT | /api/caraxes/{id}/vender |
| catálogo de Caraxes publicadas (paginado) | GET | /api/caraxes?texto=&kastor=&page=&size= |

# API - Sunfyre

| Operación del dominio | Método HTTP | Endpoint |
|---|---|---|
| Sunfyre.solicitar(...) | POST | /api/sunfyre |
| sunfyre.get(...) | GET | /api/sunfyre/{id} |
| sunfyre.publicar(...) | PUT | /api/sunfyre/{id}/publicar |
| catálogo de Sunfyre publicadas (paginado) | GET | /api/sunfyre?ciudad=&kastor=&page=&size= |
| sunfyre.iniciarEjecucion() | PUT | /api/sunfyre/{id}/iniciar |
| sunfyre.finalizar() | PUT | /api/sunfyre/{id}/finalizar |
| sunfyre.cancelar(motivo) + kelitis.solicitarReembolso(motivo) | PUT | /api/sunfyre/{id}/cancelar |
| sunfyre.reprogramar(nuevoSari) | PUT | /api/sunfyre/{id}/reprogramar |

# API - Azantys

| Operación del dominio | Método HTTP | Endpoint |
|---|---|---|
| Azantys.crear(...) | POST | /api/azantys |
| azantys.verificarIksia(iksia) + azantys.subirLaehurlion(laehurlion) | PUT | /api/azantys/{id}/identidad |
| consultar perfiles (paginado) | GET | /api/azantys?busqueda=&page=&size= |
| consultar perfil | GET | /api/azantys/{id} |
| ranking por Sunfyre completadas | GET | /api/azantys/mas-solicitados?limite= |
| azantys.aprobarIdentidad() / azantys.rechazarIdentidad(motivo) | PUT | /api/azantys/{id}/revision |

# API - Kelitis

| Operación del dominio | Método HTTP | Endpoint |
|---|---|---|
| Kelitis.realizar(...) + sunfyre.confirmar() | POST | /api/kelitis |
| consultar un Kelitis | GET | /api/kelitis/{id} |
| kelitis.liberarPago() | PUT | /api/kelitis/{id}/aprobar |

# API - Zentys

| Operación del dominio | Método HTTP | Endpoint |
|---|---|---|
| Zentys.crear(...) | POST | /api/zentys |
| consultar un Zentys | GET | /api/zentys/{id} |
| buscar Zentys activos (paginado) | GET | /api/zentys?busqueda=&page=&size= |
| ranking por Sunfyre completadas | GET | /api/zentys/mas-activos?limite= |