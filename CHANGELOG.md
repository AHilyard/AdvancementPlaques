# Changelog

### 1.4.8
- Fixed an intermittent crash with some modded advancements.
- First Minecraft 1.19.3 release.

### 1.4.7
- Fixed incompatible mod resources.

### 1.4.6
- Added support for Toast Manager.

### 1.4.5
- Added support for recent versions of Jade and WTHIT. (Fabric Only)
- Updated configuration system to use Forge Config API Port for consistency with Forge version. (Fabric Only)
- Initial release for Fabric 1.19.

### 1.4.4
- Added a missing Iceberg dependency in Fabric version.

### 1.4.3
- Added horizontal offset configuration option.
- Plaques will no longer render while the game is paused or loading.
- Initial release for Fabric 1.18.

### 1.4.2
- Decoupled custom item renderer to use Iceberg (Fabric).
- Added support for 1.17.1/1.18.1 version of Toast Control.

### 1.4.1
- Added plaque title and advancement name color configuration options.
- Fixed a log-spam issue with WTHIT.
- Initial release for Forge 1.18.

### 1.4.0
- Updated license.
- Compressed assets for slight filesize reduction.
- Fixed a bug that caused multiple plaques to appear at the same time.
- Decoupled custom item renderer to use Iceberg (Forge).
- Added partial Canvas renderer support (due to limitations, plaques cannot fade out when using Canvas).

### 1.3.1
- Fixed a registry issue in multiplayer games that was causing crashes.
- Fixed a compatibility issue with WTHIT.
- Added explicit dependency on Cloth Config API to fix a crash bug.

### 1.3.0
- Added sounds for task and goal advancements.  These can be overridden with resource packs.
- Added configuration options to selectively mute tasks, goals, or challenges.

### 1.2.5
- Advancement whitelist will now override disabled advancement type options.  For example, if you disable task advancements but still want to display a plaque for "Cover me with diamonds!", this will now work.
- Initial release for Forge 1.17.1.
- Initial release for Fabric 1.17.1.

### 1.2.4
- Removed waila version requirements to better support alternative mods with the same "waila" mod id.

### 1.2.3
- Added plaque duration configuration options.

### 1.2.2
- Added advancement whitelist feature to only show plaques for select advancements.

### 1.2.1
- Added update JSON file for update notification support.
- Added proper dependency information for optional mod dependencies.
- Updated icon file to look nicer in the mod menu.

### 1.2.0
- Added a configuration option to hide waila / hwyla / jade tooltips while plaques are showing.
- Fixed an issue with plaques not properly overlapping recipe toasts.

### 1.1.1
- Added "distance" configuration option to configure how far from the top or bottom of the screen the plaque will appear.

### 1.1.0
- Added support for Toast Control.

### 1.0.0
- Initial release for Forge 1.16.5.