# Invariantes de los Agregados

---

## Agregado `Sunfyre`

1. **Creación completa.** Toda `Sunfyre` siempre debe nacer con `idAzantys`, `idZentys`, `sari`, `kastor`, `alion` y `valaCongelado` no nulos. Nunca puede existir un servicio artístico sin creador, sin cliente, sin ventana de tiempo, sin tipo de servicio, sin ubicación ni sin precio pactado.

2. **Precio congelado.** `valaCongelado` nunca puede cambiar durante todo el ciclo de vida de la `Sunfyre`. Siempre se fija en la creación, y ninguna operación posterior (`reprogramar`, `iniciarEjecucion`, `finalizar`, `cancelar`) puede alterarlo.

3. **No solapamiento de agenda.** El `sari` de una `Sunfyre` nunca puede solaparse con el `sari` de otra `Sunfyre` en estado `ABONADO` del mismo `idAzantys`. Siempre se valida dentro de `confirmar()` y de `reprogramar(nuevoSari)`, comparando contra las citas ya abonadas de ese artista.

4. **Reprogramación controlada.** `reprogramar(nuevoSari)` solo es válido si `estado == ABONADO`. Nunca tiene sentido reprogramar algo `PENDIENTE` (aún no hay compromiso firme) ni algo `COMPLETADO` o `CANCELADO` (ya cerró su ciclo).

5. **Camino único de transición.** Las transiciones siempre siguen un único orden: `PENDIENTE → ABONADO → COMPLETADO`, mediante `confirmar() → iniciarEjecucion() → finalizar()`. Nunca se puede invocar `finalizar()` sin haber pasado por `iniciarEjecucion()`, ni `iniciarEjecucion()` sin que `estado == ABONADO`.

6. **Cancelación restringida.** `CANCELADO` solo es alcanzable desde `PENDIENTE` o `ABONADO`, nunca desde `COMPLETADO`. Consistente con la regla de negocio: *"un servicio artístico personalizado no puede ser devuelto una vez iniciada su ejecución, salvo incumplimiento"*.

7. **Motivo obligatorio.** `cancelar(motivo)` siempre exige un motivo no nulo ni vacío.

8. **Estados terminales.** `COMPLETADO` y `CANCELADO` son siempre estados definitivos. Ningún método puede reabrir o modificar una `Sunfyre` que ya cerró su ciclo de vida.

---

## Agregado `Kelitis`

1. **Creación completa.** Todo `Kelitis` siempre debe nacer con `idAzantys`, `idZentys` y `valaCongelado` no nulos, mediante `realizar(...)`, y siempre en estado inicial `RETENIDO`. El escrow nunca puede existir sin las dos partes de la transacción ni sin el monto retenido.

2. **Monto congelado.** `valaCongelado` nunca puede cambiar durante todo el ciclo de vida del `Kelitis`. Ni `confirmar()` ni `solicitarReembolso()` pueden alterar el monto — solo cambian el `estado`.

3. **Confirmación desde RETENIDO.** `confirmar()` solo es válido si `estado == RETENIDO`. Es la transición que libera el pago al `Azantys` una vez el servicio está terminado y aprobado, tal como establece el glosario del proyecto. Nunca puede confirmarse un `Kelitis` que ya está `LIBERADO`, `COMPLETADO` o `REEMBOLSADO`.

4. **Reembolso desde RETENIDO.** `solicitarReembolso(motivo)` solo es válido si `estado == RETENIDO`, y siempre exige un motivo no nulo ni vacío. Nunca se puede reembolsar un `Kelitis` que ya fue liberado o completado.

5. **Exclusividad de caminos.** Un `Kelitis` solo puede seguir uno de dos caminos posibles a partir de `RETENIDO`: `RETENIDO → LIBERADO → COMPLETADO` (pago exitoso) o `RETENIDO → REEMBOLSADO` (devolución). Nunca ambos.

6. **Estados terminales.** `COMPLETADO` y `REEMBOLSADO` son siempre estados definitivos e inmutables. Ningún método puede modificar un `Kelitis` que ya cerró su ciclo de vida.
