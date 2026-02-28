# Stasis

Stasis is a Fabric server-side performance mod that reduces BlockEntity tick load with an activity-aware scheduler.

## Features

- Intelligent BlockEntity tick gating via Mixin
- Dynamic tick scaling:
  - Active: every tick
  - Idle >= 20 ticks: every 5 ticks
  - Idle >= 200 ticks: every 20 ticks
  - Idle >= 600 ticks: frozen
- Instant reactivation hooks for common state mutations
- Optional chunk-level heuristic for far-from-player throttling
- Runtime admin commands for stats and toggling
- Cloth Config + ModMenu config UI support

## Compatibility

- Fabric Loader
- Java 21
- Server-safe environment

See releases for supported Minecraft versions.

## Dependencies

### Runtime

- fabric-loader
- fabric-api
- cloth-config-fabric (bundled via jar-in-jar include)

### Optional

- modmenu (for config screen in client mod menu; not required for dedicated server runtime)

## Build

```bash
gradle wrapper
./gradlew build
