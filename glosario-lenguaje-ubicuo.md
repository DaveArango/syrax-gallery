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
