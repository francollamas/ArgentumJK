# ArgentumJK
Este proyecto es una recreación del cliente de Argentum Online.

## Características
- Basado en Argentum Online 0.13.0
- Es una continuación del proyecto [ArgentumOnline-Mobile](https://github.com/francollamas/ArgentumOnline-Mobile)
- Multiplataforma: Desktop (Windows, macOS, Linux), Mobile (Android, iOS) y Web.
- Utiliza los recursos originales en el mismo formato o similar
- Lenguajes utilizados: Java
- Tecnologías utilizadas:
  - [libGDX](https://libgdx.com) (game development framework)

## Ejecutar en Web

1. Instalar Rust y Cargo.
2. Instalar wstcp (proxy de Websocket a Socket TCP)

```
cargo install wstcp
```

3. Ejecutar servidor

4. Ejecutar el proxy
```
wstcp --bind-addr 0.0.0.0:7667 localhost:7666
```

5. Iniciar el juego en plataforma web:
```
cd client
./gradlew web:run
```
