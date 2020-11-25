# Compatibility tutorial

## Dependencies

This part allows mods to easily detect each other presence, while minimally cluttering the table

* To mark you mod as loaded, create aspect `mod.YOURMODID`, and use `aspects$add` property to add it to `dependencytracker` card
* To check if desired mod is locked (for a recipe), use `"extantreqs": { "mod.DESIREDMODID": 1 }` (or `-1` to check that it's not loaded)
* Every legacy should have an `effect` of `"dependencytracker": 1`
* If `needs` or `turn.begin` recipes are not triggered, it is preferable, but not mandatory, to "lock" `dependencytracker` in some looping verb, to clear space on the table

## Subversion chain

This part reworks upgrade/subversion, making slots require aspects instead of direct elements, because slot modifications are incompatible

* Lore/Influence upgrade/subvert slots should require aspect `upgrades.ELEMENTID`
* 'Knowledge' slots on influence upgrades should require aspect `knowledge.PRINCIPLE` (TODO: Unique knowledge aspect for each influence? Causes lore to have enormous amount of aspects)
* Everything upgrading or subverting these cards should have this aspects
* Recipes themselves are still allowed to condition on cards


To insert your principle into subversion chain, you should, say, insert Flame, between Forge and Edge:

* Create `knowledge.flame` aspect, and add it to all Flame Lores starting from Flame6 (if following vanilla conventions of influence upgrading)
* Make `upgrades.ID` for all Flame lores & influences
* Use `aspects$remove` property to remove `upgrades.*forge*` from Edge lores & influences
* Use `aspects$add` property to add `upgrades.*flame*` to Edge lores & influences
* Add `upgrades.*forge*` to corresponding Flame lores & influences
* Make proper recipes of course

If two independent mods decide to modify the chain in one place, it would result in chain branching, e.g if both Flame and Shade are inserted between Forge and Edge, Forge lore would be subvertable to both Flame and Shade, and both Flame and Shade would be subvertable to Edge

In contrast, when two mods modify the chain 'standard' way (by modifying slots themselves), only one of branches would be available, dictated by unknown loading order  
