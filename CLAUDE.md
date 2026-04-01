# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build
./gradlew build

# Run
./gradlew run

# Build fat jar
./gradlew jar
```

The project requires no compilation step beyond Gradle. No tests exist in this codebase.

## Architecture Overview

**Perceptionallity** is a 2D game built on Java Swing with a custom engine. Entry point: [Perceptionallity.java](src/main/java/de/furkan/perceptionallity/Perceptionallity.java) → creates `Game`, sets system L&F, starts the game loop.

### Threading Model

Two independent loops run concurrently:

- **Game logic thread** ([GameManager](src/main/java/de/furkan/perceptionallity/game/GameManager.java)): Fixed 15ms timestep via `java.util.Timer`. Handles physics (velocity), input, animations, and registered `GameAction` callbacks.
- **Render thread** ([GameRenderer](src/main/java/de/furkan/perceptionallity/game/GameRenderer.java)): Daemon thread, ~6ms sleep. Updates `JLabel` icons from animation frames, calculates camera-relative positions, repaints all `JComponent`s.

### GameObject / Entity Hierarchy

All visible objects extend [GameObject](src/main/java/de/furkan/perceptionallity/game/GameObject.java), which wraps a `JLabel` for rendering. `GameEntity` adds an `EntityAttributes` system. Concrete types: `GamePlayer`, `GameNPC`, `GameCampfire`.

### Camera System

[Camera](src/main/java/de/furkan/perceptionallity/game/Camera.java) can track a `GameObject` or free-roam. Translates world positions to screen positions for the renderer; caches results per frame in a `HashMap`.

### Menu System

Abstract [Menu](src/main/java/de/furkan/perceptionallity/menu/Menu.java) base with optional per-menu update timer. [MenuManager](src/main/java/de/furkan/perceptionallity/menu/MenuManager.java) handles transitions. Menu components (buttons, labels, sliders, checkboxes, sprites) live under `menu/components/`.

### Rendering & Lighting

`GameRenderer` extends `JLayeredPane`. Integer Z-layers control draw order. [GameLightingManager](src/main/java/de/furkan/perceptionallity/game/lighting/GameLightingManager.java) renders a darkness overlay and glow layer at 0.5× resolution for performance; default ambient darkness is 0.9f.

### Resource System

[ResourceManager](src/main/java/de/furkan/perceptionallity/resources/ResourceManager.java) stores sprites, animations, fonts, and sounds in a `ConcurrentHashMap`. Resources are **cloned on retrieval** to allow independent playback state.

### Game State

`GameState` enum: `NONE`, `MENU`, `IN_GAME`, `IN_PAUSE`, `IN_DIALOGUE`, `IN_CUTSCENE`, `RESOURCE_LOADING`.

## Debug Keys (in-game)

| Key | Action |
|-----|--------|
| F | Toggle component outlines |
| R | Reload last menu |
| H | Toggle camera mode |
| G | Reset to boot menu |
| X | Test crash |
| P | Pause/resume |
