# Batalla Mutante - Workplan

## Objetivo

Completar el juego automatico de batalla de mutantes en dos semanas y reservar una tercera semana para pruebas, correcciones, documentacion y mejoras finales.

El trabajo se divide entre dos personas, pero las decisiones de arquitectura, la integracion y la entrega final se revisan en conjunto.

## Decisiones iniciales

Antes de comenzar la implementacion, ambos integrantes deben acordar lo siguiente:

- Mantener las cuatro capas: Model, Game, Control y UI.
- Usar interfaces y composicion para poderes combinados, porque Java no permite herencia multiple de clases.
- Estandarizar nombres como `Position`, `Monster`, `ScoreboardPanel` y `Constants`.
- Definir energia inicial de 100 para cada mutante.
- Mantener defensa y dano inicial entre 1 y 3.
- Definir el aumento de poder hasta un maximo de 7.
- Permitir equipos de 3 a 11 mutantes, siempre con el mismo tamano.
- Usar una fuente de aleatoriedad controlable para las pruebas.
- Mantener la logica de juego fuera de la interfaz grafica.
- Definir un estado observable o snapshot para que la UI pueda actualizarse sin modificar la logica.

## Semana 1 - Modelo y reglas del juego

### Persona 1 - Model Layer

**Dias 1 y 2**

- Revisar y normalizar las entidades descritas en `docs/class.txt`.
- Crear las constantes de reglas, limites, dimensiones y valores iniciales.
- Implementar `Power` y sus atributos principales.
- Implementar la abstraccion `Mutant`.

**Dias 3 y 4**

- Implementar los tipos concretos de mutantes.
- Implementar los poderes concretos mediante polimorfismo y composicion.
- Agregar energia, defensa, dano, equipo, icono, posicion y estado vivo/muerto.
- Agregar validaciones para evitar valores fuera de rango.

**Dia 5**

- Crear pruebas del modelo.
- Verificar energia inicial, dano, defensa, aumento de poder y muerte.
- Entregar una API estable para que Game y Control puedan utilizar el modelo.

### Persona 2 - Game y Control Layer

**Dias 1 y 2**

- Implementar `Position` y operaciones de movimiento.
- Implementar `Team` con color, simbolo, lista de mutantes y conteos.
- Implementar `Battlefield` con ancho, alto y dos equipos.

**Dias 3 y 4**

- Generar dos equipos del mismo tamano entre 3 y 11 mutantes.
- Implementar marcador, conteos de vivos y muertos, y condicion de victoria.
- Implementar `Movement` y limites del campo.
- Implementar deteccion de mutantes dentro del radio de combate.

**Dia 5**

- Implementar la resolucion de ataque y defensa.
- Verificar dano normal, dano contra defensa y aumento del poder atacante.
- Crear pruebas deterministas para las reglas de combate.

### Trabajo conjunto al final de la semana 1

- Integrar Model, Game y Control en una batalla pequena.
- Verificar que un equipo puede perder todos sus mutantes.
- Confirmar que los conteos y el ganador son correctos.
- Ejecutar `make build`.
- Revisar que los `main` de cada capa sigan funcionando.
- Actualizar la especificacion del README si las decisiones finales cambiaron.

## Semana 2 - Simulacion, concurrencia e interfaz

### Persona 1 - Simulacion y concurrencia

**Dias 6 y 7**

- Implementar la orquestacion de una batalla completa.
- Implementar movimiento continuo de los mutantes.
- Crear tareas o hilos para procesar encuentros en paralelo.

**Dias 8 y 9**

- Sincronizar cambios de energia, estado vivo/muerto y conteos.
- Evitar ataques duplicados, carreras y deadlocks.
- Implementar la condicion de finalizacion y calculo del ganador.
- Agregar detencion limpia de los hilos.

**Dia 10**

- Crear una prueba end-to-end sin UI.
- Ejecutar batallas con equipos pequenos, medianos y grandes.
- Verificar que la batalla siempre termina correctamente.

### Persona 2 - UI Layer

**Dias 6 y 7**

- Implementar `GameFrame` y la ventana principal.
- Implementar `GameCanvas` para dibujar el campo y los mutantes.
- Mostrar color, simbolo, posicion y estado de cada equipo.

**Dias 8 y 9**

- Implementar `ScoreboardPanel`.
- Mostrar energia, vivos, muertos y marcador de ambos equipos.
- Mostrar el anuncio del ganador.
- Conectar la UI con Observer y MVC.

**Dia 10**

- Implementar el reinicio de una partida terminada.
- Configurar el intervalo de refresco.
- Confirmar que la UI solo consulta y muestra el estado del juego.

### Trabajo conjunto al final de la semana 2

- Conectar simulacion y UI.
- Ejecutar el juego completo desde `Main`.
- Confirmar que todos los mutantes se ven y se mueven.
- Confirmar que la interfaz se actualiza sin bloquear Swing.
- Confirmar que una nueva partida puede comenzar sin cerrar la aplicacion.
- Actualizar `docs/diagram.puml` con las clases e interfaces reales.

## Semana 3 - Pruebas, documentacion y mejoras

### Persona 1 - Pruebas de logica y concurrencia

- Probar equipos con tamanos 3, 5, 7, 9 y 11.
- Probar entradas invalidas y valores fuera de rango.
- Probar movimiento en los limites del campo.
- Probar ataques, defensa y aumento maximo del poder.
- Probar varios encuentros simultaneos.
- Ejecutar batallas repetidas con una semilla controlada.
- Buscar carreras, bloqueos y errores de terminacion.
- Verificar consistencia de vivos, muertos, energia y ganador.

### Persona 2 - Pruebas de UI y entrega

- Verificar el renderizado de todos los mutantes.
- Verificar colores, simbolos, energia y estado de los equipos.
- Verificar el refresh y el anuncio del ganador.
- Verificar el reinicio de partidas.
- Corregir problemas de layout y actualizaciones de Swing.
- Mejorar el `Makefile` para compilar todos los paquetes.
- Evitar que los archivos `.class` queden dentro de `src/main/java`.
- Completar README, UML e instrucciones de ejecucion.

### Trabajo conjunto de cierre

- Ejecutar una compilacion limpia.
- Ejecutar los programas de prueba de cada capa.
- Ejecutar el flujo completo varias veces.
- Revisar todos los requisitos del enunciado.
- Revisar los cambios y eliminar archivos generados.
- Confirmar que ambos integrantes tengan commits significativos en GitHub.
- Preparar una explicacion del diseno, las reglas y la concurrencia para la presentacion.

## Criterios de terminado

El proyecto se considera listo cuando:

- Compila desde la raiz con `make build`.
- La aplicacion puede iniciar una batalla automaticamente.
- Los equipos tienen entre 3 y 11 mutantes del mismo tamano.
- Los mutantes se mueven dentro del campo.
- Los encuentros se detectan por radio.
- Las reglas de ataque, defensa y dano funcionan correctamente.
- Los combates pueden ejecutarse en paralelo sin inconsistencias.
- La batalla termina cuando un equipo queda sin mutantes.
- La UI muestra posiciones, equipos, energia, vivos, muertos y ganador.
- La UI usa Observer y MVC sin implementar reglas del juego.
- Se puede iniciar una nueva partida despues de terminar una batalla.
- Cada capa tiene un programa de prueba independiente.
- El README y el diagrama UML coinciden con la implementacion.
- Existen pruebas para casos normales, limites, errores y concurrencia.

## Riesgos y prevencion

| Riesgo | Prevencion |
|---|---|
| El modelo de `class.txt` usa herencia multiple | Usar interfaces y composicion antes de implementar poderes |
| La concurrencia produce estados inconsistentes | Centralizar actualizaciones y probar con semillas controladas |
| La UI queda acoplada a la logica | Usar snapshots observables y mantener las reglas en Control/Game |
| El proyecto tarda demasiado en integrar | Definir contratos entre capas durante el primer dia |
| Las pruebas no son repetibles | Inyectar una fuente de aleatoriedad o usar semillas |
| El build no encuentra nuevos paquetes | Revisar el `Makefile` durante la semana 3 |
| Solo una persona aparece en GitHub | Dividir tareas y realizar commits pequenos y significativos |

## Verificacion final

```bash
make build
make run
```

Tambien se deben ejecutar los `main` de cada capa por separado y verificar manualmente el flujo completo de una batalla, el anuncio del ganador y el reinicio de una nueva partida.
