## Integrated Derivative

Giving the Integrated Terminals mod the much-needed love, for Minecraft 1.12.

Integrated Dynamics is a very powerful automation mod that also offers terminals similar to mods such as AE2 and RS (with Integrated Terminals). Unfortunately, Integrated Terminals' UX is relatively bad and can be daunting and difficult to use for many players. This mod is made to fix those difficulties.

### Dependencies

* [Cyclops Core](https://www.curseforge.com/minecraft/mc-mods/cyclops-core) - this is required for ID to work
* [Common Capabilities](https://www.curseforge.com/minecraft/mc-mods/common-capabilities) - this is required for ID to work
* [Integrated Dynamics](https://www.curseforge.com/minecraft/mc-mods/integrated-dynamics)
* [Integrated Terminals](https://www.curseforge.com/minecraft/mc-mods/integrated-terminals) (optional)

### Features

Most of these features can be toggled in the configuration file of the mod or edited in the ingame menu.

* Clearing the crafting terminal in any way will attempt to put items back into the network by default.
  * This applies to clearing it via the x button (unless it is shift-clicked), as well as shift-clicking JEI recipes.
  * This only has to be enabled on the clientside.
  * Note: the button's tooltip still will mention shift-clicking putting items back into the network (unless a resourcepack is used to circumvent this).
* Shift-clicking JEI recipes in the Storage terminal will also try to pull items from inventory in addition to the network.
  * This must be enabled on the server side to work, and can be disabled on the client side if desired.
* Recipes can be shift-clicked from JEI even if not all components are present in the network and/or inventory.
  * This only has to be enabled on the clientside. Disabled by default.
