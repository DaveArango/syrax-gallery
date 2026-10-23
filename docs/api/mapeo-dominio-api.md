# Mapeo Dominio 
# API - Caraxes

|Operación del dominio|Método HTTP|Endpoint|
|---|---|---|
| caraxes.crear(...) | POST | /api/caraxes |
| caraxes.get(...) | GET | /api/caraxes/{id} |
| caraxes.publicar(...) | PUT | /api/caraxes/{id}/publicar/{kanez} |

# API - Sunfyre

| Operación del dominio | Método HTTP | Endpoint |
|---|---|---|
| Sunfyre.solicitar(...) | POST | /api/sunfyre |
| sunfyre.get(...) | GET | /api/sunfyre/{id} |
| sunfyre.publicar(...) | PUT | /api/sunfyre/{id}/publicar |
| catálogo de Sunfyre publicadas (paginado) | GET | /api/sunfyre?ciudad=&kastor=&page=&size= |

# API - Azantys

| Operación del dominio | Método HTTP | Endpoint |
|---|---|---|
| Azantys.crear(...) | POST | /api/azantys |
| azantys.verificarIksia(iksia) + azantys.subirLaehurlion(laehurlion) | PUT | /api/azantys/{id}/identidad |
| consultar perfiles (paginado) | GET | /api/azantys?busqueda=&page=&size= |
| consultar perfil | GET | /api/azantys/{id} |
| ranking por Sunfyre completadas | GET | /api/azantys/mas-solicitados?limite= |

# API - Kelitis

| Operación del dominio | Método HTTP | Endpoint |
|---|---|---|
| Kelitis.realizar(...) + sunfyre.confirmar() | POST | /api/kelitis |
| consultar un Kelitis | GET | /api/kelitis/{id} |
| kelitis.liberarPago() | PUT | /api/kelitis/{id}/aprobar |