# ArgentumJK
Este proyecto es una recreación del juego Argentum Online, tanto de su cliente como servidor.

## Características
- Basado en Argentum Online 0.12.3
- Cliente y servidor multiplataforma
  - El Cliente funciona en: Desktop (Windows, macOS, Linux), Mobile (Android, iOS) y Web.
  - El Servidor funciona en todas las anteriores excepto Web.
- Se puede jugar local sin conexión desde un celular.
- Utiliza los recursos originales en el mismo formato o similar

## Cliente
Se tomó como base el proyecto [JFenix13](https://github.com/francollamas/ArgentumOnline-Mobile)

- Lenguajes utilizados: Java

- Tecnologías utilizadas:
  - [libGDX](https://libgdx.com) (game development framework)

## Servidor
Se tomó como base el servidor [aoj-server](https://github.com/gorlok/aoj-server) creado por [Gorlok](https://github.com/gorlok)

- Lenguaje utilizado: Java
- Multiplataforma (Windows, Linux, macOS)
- Se adaptó para poder correr también en mobile: Android, iOS


## Ejecutar en Web

1. Instalar Rust y Cargo.
2. Instalar wstcp (proxy de Websocket a Socket TCP)

```
cargo install wstcp
```

3. Ejecutar servidor con:
```
cd server
./gradlew headless:run
```

4. Ejecutar el proxy
```
wstcp --bind-addr 0.0.0.0:7667 localhost:7666
```

5. Iniciar el juego en plataforma web:
```
cd client
./gradlew teavm:run
```