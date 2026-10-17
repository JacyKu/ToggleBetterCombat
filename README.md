# Toggle Better Combat

Server-authoritative Better Combat toggle for Minecraft 1.21.1 (Fabric + NeoForge).

**Developers:** jkitter, Deepacat

## Features

- `B`: toggle Better Combat swinging
- Server-stored state; survives relogs/respawns
- Off: vanilla attacks/sweeping, poses, offhand, sweep particles; no dual-wield speed
- Client notification: action bar or toast
## Requires

- Minecraft 1.21.1, Java 21
- Better Combat 2.4.x
- Architectury API 13.x
- Fabric Loader 0.16.5+, Fabric API 0.103.0+1.21.1 (Fabric)
- NeoForge 21.1.x (NeoForge)
- Better Combat's dependencies

Server required. Client optional; keybind and config need it.

## Build

`.\gradlew.bat build`

```
fabric/build/libs/togglebettercombat-fabric-1.0.0+1.21.1.jar
neoforge/build/libs/togglebettercombat-neoforge-1.0.0+1.21.1.jar
```

## Config

`config/togglebettercombat-client.json`

```json
{ "notificationStyle": "ACTION_BAR" }
```

`ACTION_BAR` or `TOAST`. Changes apply on the next toggle; no restart needed.

## Dev

```powershell
.\gradlew.bat :fabric:runClient
.\gradlew.bat :neoforge:runClient
```
