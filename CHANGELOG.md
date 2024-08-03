# Changelog

## [1.0.2] - 2024-08-04

### Tweaks

- Network Terminal is now compatible with Mouse Tweaks! Shift+Dragging items in the player's inventory will send all of those items to the network.

### Fixes

- Shift-clicking recipes from JEI into the crafting terminal will no longer void items in special cases (introduced in 1.0.0).
- Shift-clicking the output from the crafting grid will no longer void items in special cases (introduced in 1.0.0).
- Shift-clicking recipes from JEI into the crafting terminal will properly pull items from the player's inventory in the production environment (in 1.0.0 only worked in dev environment).

## [1.0.1] - 2024-07-21

### Fixes
- Fixed a dupe with shift-clicking in the crafting grid which was introduced in 1.0.0.
  - This unfortunately required slowing down shift-clicking, but it seems acceptable and unfixable beyond it.
- Fixed Delayer outputting a cryptic error when the variable output by it is used as an invalid type.

## [1.0.0] - 2024-07-20

### Tweaks
- Adjusted the behavior of shift-clicking in various UIs, "+" button in JEI and the clear button to be more familiar to AE2 users. Configurable.
- Improved the Logic Programmer's JEI support.

### Bugfixes
- Fixed recipe variables made for Carpenter and Thermionic Fabricator not working.
- Fixed a dupe when a terminal is cleared.

### Performance
- Significantly improved the performance of shift-clicking the crafting output in the crafting grid.
