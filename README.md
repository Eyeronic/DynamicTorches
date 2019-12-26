# DynamicTorches

This mod for Minecraft 1.12.2 replaces standard minecraft torches in a way that torches can be placed exactly in a corner.
A torch's position will dynamically change if surrounding blocks change, i.e. if one of the corner's blocks is broken it will 
automatically move from a corner position to the remaining blocks middle and the other way around.
Standard torch behaviour is still provided (i.e. vanilla crafting recipes) but I can't guarantee compatibility with any mod changing/extending torch behaviour.
You can also cycle through all valid positions by right clicking a torch.

You can partially influence this behaviour via the mod's config menu to make torches automatically switch to a position on the
ground if the torch-bearing wall is broken.
Additionally you can activate either a "First Run" mode to automatically move torches from vanilla positions into corners or a "Clean Up" mode to do the opposite.
It's recommended to use "Clean Up" mode before uninstalling this mod or you will lose all torches placed in corner positions!

Automatic modes can be used on servers only by either the server owner or any opped player with at least op level 1. I might add a config variable to change this behaviour though.

## Dependencies

- Minecraft 1.12.2
- [Minecraft Forge 1.12.2-14.23.5.2768](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)

## Download

[Latest Release](https://github.com/Eyeronic/DynamicTorches/releases/tag/v1.12.2-1.1)

[Latest Stable](https://github.com/Eyeronic/DynamicTorches/releases/tag/v1.12.2-1.1)

## Known Issues

- Torch positions in "First Run" and "Clean Up" mode will only be updated in currently active chunks. So you might have to run around to update all placed torches before deactivating these modes.

## Planned Features

- [x] Fix rendering issue when changing torch behaviour and not restarting Minecraft.
- [x] If possible add a way to reset torch metadata when deactivating Dynamic Torches mod.
- [x] Manually change a torch's position (via metadata) by right-clicking the torch.
- [x] Automatically update torch metadata on loading a save game which previously didn't use Dynamic Torches (aka move all torches into corners where possible).
- [ ] Add compatibility with HitchH1k3r's [Torch Levers](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1288257-1-7-10-forge-torch-levers-and-more-version-1-4-2) mod if there is a 1.12.2 version.

## Notes

Beware, this is my first mod for Minecraft 1.12.2, so code probably is neither beautiful to look at nor the most performant or easiest way to implement what this mod does.

I'm open for any input on that behalf, just let me know via the [Issues tab](https://github.com/Eyeronic/DynamicTorches/issues). 

## Copyright

DynamicTorches is (c) 2019 Eyeronic and licensed under GPL v3. See the LICENSE.txt for details or go to http://www.gnu.org/licenses/gpl-3.0.txt for more information.

You are free to use, modify and distribute this mod and it's code in any kind of mod pack or own mod project but I would appreciate it if you give credit for my work.
