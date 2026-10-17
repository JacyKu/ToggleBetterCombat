# Toggle Better Combat

Server-authoritative Better Combat toggle for Minecraft 1.20.1 (Fabric + Forge).

## Features

- `B`: toggle Better Combat swinging
- Server-stored state; survives relogs/respawns
- Off: vanilla attacks/sweeping, offhand, sweep particles; no dual-wield speed
- Client notification: action bar or toast
- No server config

## Requires

- Minecraft 1.20.1, Java 17
- Better Combat 1.9.x
- Architectury API 9.x
- Fabric Loader 0.14.21+, Fabric API 0.83.1+1.20.1 (Fabric)
- Forge 47.1.x (Forge)
- Better Combat's dependencies

Server required. Client required for the keybind and feedback.

## Build

`.\gradlew.bat build`

```
fabric/build/libs/togglebettercombat-fabric-1.0.0+1.20.1.jar
forge/build/libs/togglebettercombat-forge-1.0.0+1.20.1.jar
```

## Config

Open with `O` in-game, or use the Config button in the Forge mods screen.

`config/togglebettercombat-client.json`

```json
{ "notificationStyle": "ACTION_BAR" }
```

`ACTION_BAR` or `TOAST`. Changes apply on the next toggle; no restart needed.

## Dev

```powershell
.\gradlew.bat :fabric:runClient
.\gradlew.bat :forge:runClient
```
