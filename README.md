# DynamicTorches

This mod for Minecraft 1.7.10 replaces standard minecraft torches in a way that torches can be placed exactly in a corner.
A torch's position will dynamically change if surrounding blocks change, i.e. if one of the corner's blocks is broken it will 
automatically move from a corner position to the remaining blocks middle and the other way around.
Standard torch behaviour is still provided but I can't guarantee compatibility with any mod changing/extending torch behaviour.

You can partially influence this behaviour via the mod's config menu to make torches automatically switch to a position on the
ground if the torch-bearing wall is broken and vice versa.
This requires you to restart Minecraft or unintended side effects might occur!

## Dependencies

- Minecraft 1.7.10
- [Minecraft Forge 1.7.10-10.13.4.1558](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.7.10.html)

## Download

[Latest Release](https://github.com/Eyeronic/DynamicTorches/releases/tag/v0.7)

## Known Issues

- Torches won't be rendered correctly if you toggle "shouldTorchesSwitchToGround" and reload a save without restarting Minecraft.
- Torches will float in the air when deactivating/removing DynamicTorches mod if they were placed in corners.
- Torches will not update their metadata when loading a save for the first time after adding Dynamic Torches mod.

## Planned Features

- [ ] Fix rendering issue when changing torch behaviour and not restarting Minecraft.
- [ ] If possible add a way to reset torch metadata when deactivating Dynamic Torches mod.
- [ ] Automatically update torch metadata on loading a save game which previously didn't use Dynamic Torches (aka move all torches into corners where possible).
- [ ] Port to Minecraft 1.11.2 (probably quite far in the future)
- [ ] Manually change a torch's position (via metadata) by right-clicking the torch.
- [ ] Add compatibility with HitchH1k3r's [Torch Levers](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1288257-1-7-10-forge-torch-levers-and-more-version-1-4-2) mod which is unfortunately pretty much dead but still can be downloaded.

## Notes

Beware, this is my first mod for Minecraft! 

Code probably is neither beautiful to look at nor the most performant or easiest way to implement what this mod does.
I'm open for any input on that behalf, just let me know via the [Issues tab](https://github.com/Eyeronic/DynamicTorches/issues). (I'll probably change that and provide some kind of 'business mail' or such in the future ^^)

## Copyright

DynamicTorches is (c) 2017 Eyeronic and licensed under GPL v3. See the LICENSE.txt for details or go to http://www.gnu.org/licenses/gpl-3.0.txt for more information.

You are free to use, modify and distribute this mod and it's code in any kind of mod pack or own mod project but I would of course appreciate it if you would give credit for my work.
