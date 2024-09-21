# RaidVisualizer

This is a fabric mod for Minecraft Java 1.16.1. Its purpose is to practice raids for all advancements speedrunning.

The mod shows the following things with the following colors:

YELLOW: The points of interest considered for raids including beds, workstations, and bells.

RED : The raid center, calculated from the center of mass of the POIs from which all spawning is done.

PINK : The 5x5 area (extends +/+ from the center) within which waves will spawn.

PURPLE: The wave spawn positions in outer rings. The game will always prioritize this location IF it is loaded.

By default, YELLOW/RED/PINK will render when a raid starts, and PURPLE will render when a raid is ticked.

The mod also includes a few commands:

/raid center <ServerPlayerEntity> calculates the POIs and center around wherever the player is positioned.

/raid clear removes all renderings.

Made by PacManMVC