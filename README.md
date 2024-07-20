## Integrated Derivative

Giving the Integrated Terminals mod the much-needed love, for Minecraft 1.12.

Integrated Dynamics is a very powerful automation mod that also offers terminals similar to mods such as AE2 and RS (with Integrated Terminals). Unfortunately, Integrated Terminals' UX is relatively bad and can be daunting and difficult to use for many players. This mod is made to fix those difficulties.

### Dependencies

* [Cyclops Core](https://www.curseforge.com/minecraft/mc-mods/cyclops-core) - this is required for ID to work
* [Common Capabilities](https://www.curseforge.com/minecraft/mc-mods/common-capabilities) - this is required for ID to work
* [MixinBooter](https://www.curseforge.com/minecraft/mc-mods/mixin-booter) - your pack likely already has this
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
* Significantly optimized performance of shift+clicking the crafting result in the Storage Terminal.
  * Some statistics: shift+clicking the Compressed Dirt recipe in Enigmatica 2 Expert Extended without this mod causes a lag spike of 5-10 seconds. With this mod, it is barely slower than crafting in a vanilla crafting table.
  * This has to be enabled both on the server and the client.
  * Technical details for the nerds: we do this by replacing the IT's own packet with our packet containing a significantly optimized code snippet, which changes two main things:
    * Uses binary search to find the amount of recipes that can be executed. This is unlikely to change anything, but it was such a free fix I applied it anyway.
    * Replaces the vanilla crafting grid behavior with automatic refill (very slow) with the packet doing the entirety of work inside of it. There are many different issues that it fixes:
      * The inventories are only updated once per shift+click, rather than 64 times. This means any inventory watcher performance bottleneck is fixed (example: Triumph mod in E2:EE adds 4-5 seconds to the time needed to execute a shift+click).
      * The set of items in the network is computed only once, rather than 64 times. This is normally not a problem due to ID having an index, although might come up at some point.
* Shift+clicking in the crafting grid now outputs configurable amounts of an item.
  * Example: when crafting 6 slabs at a time, AE2 will craft 60 with a shift-click, and Integrated Terminals will craft 66. This mod allows choosing between these 2 behaviors or crafting 6 at a time.
  * Clientside setting. Defaults to the AE2 behavior.
  * Will only work if the shift+click performance optimizations are enabled!
