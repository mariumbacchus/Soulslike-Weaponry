# Changelog

## 1.2.5 - TODO test these things
- Added the new Blight Carrier gun enchantment, making Silver Bullets apply stacking Blight effect on targets, increasing their damage taken based on their armor amount
- Added the new Frostsilver gun enchantment, making Silver Bullets apply strong Permafrost effect if the target Posture Breaks
- Hitting a target with Posture Break effect while having Permafrost triggers the Ice Explosion early and removes the effect (together with the Posture Break since it triggers)
  - Note: Permafrost & other damage over time effects still trigger Posture Breaks!
- Guns can no longer have bow enchants, and Punch & Flame no longer affects guns and Silver Bullets (Power & Infinity still work)
- Fixed a bug causing guns not to find stacks of Silver Bullets in inventory even though the player has enough, and now decrements correct amount as well
- Adjusted the position of the Gatling Gun when using in first person (now centered more)
- Adjusted the particle spread of the guns
- Buffed Posture Loss applied by all guns
  - Blunderbuss: Base 25 -> 30 (per bullet), Bonus per enchant level 2 -> 3
  - Hunter Cannon: Base 60 -> 120, Bonus per enchant level 60 -> 50
  - Hunter Pistol: Base 30 -> 50, Bonus per enchant level 7 -> 10
  - Gatling Gun: Base 9 -> 10, Bonus per enchant level 1 -> 3

## 1.2.4
- Fixed a bug that removed original paper recipe
- Fixed a bug preventing Day Stalker from using OVERHEAT attack
- Fixed a bug preventing Night Prowler from using BLADES REACH and RIPPLE FANG attacks
- Fixed a bug causing Excalibur's Sonic Boom ability to auto target armor stands


- Crucible Sword has reduced cooldown in the Nether
- Chungus Staff now can be used to shoot Charged Chungus heads (which are primed TNT) that go through walls and explode after 15 ticks. Pressing ability keybind gives the user Haste and Speed like the old use ability
- Silver Bullets no longer have gravity, but ticks before respawning has been reduced from 200 to 100
- Added the new Ethereal Ammunition gun enchantment, making Silver Bullets go through walls, but they last only 25 ticks
- Added the new Explosive Rounds gun enchantment, making Silver Bullets explode on contact with entities
- Added the new Chain Lightning gun enchantment, making Silver Bullets damage mobs near the main target hit with soul infused lightning
- Added the new Curse of Misfire enchantment, adding a chance to cause an explosion on the user each Silver Bullet fired


- Added info to Demon Chunks that they can be used to summon the Decaying King when used on a Blackstone Pedestal
- Added info to the Draugr that it can summon the Old Champion for a re-fight when used on an Old Moon Altar
- Added info/hint to the Lost Soul that it can be used on the Old Moon Altar
- Added info to the Essence of Eventide that it can be used on the Old Moon Altar to summon the Fallen Icon


- Old Champion's Remains now bounce off projectiles after 3 consecutive projectile hits
  - Melee attacks reset the counter
  - The amount of hits before growing immune can be changed with config line ```old_champions_remains_max_projectile_hits_before_immune```
- Removed the darkness effect from Frenzied Shade's shadow orb attack
- Copies of the Frenzied Shade now die when the main one does
- Added boss music to Old Champion's Remains and Frenzied Shade and disc of the Champion's song
- Returning Knight now only uses the summon attack if no healer is alive
  - It will no longer summon healers under 50%, instead increasing the Remnant spawn amount
  - Obliterate attack will summon two Remnants instead of one if under 50% health, and will spawn regardless if the target that died was undead or not
  - Added config line for the summon attack cooldown
- Added hurt sounds to Day Stalker and Night Prowler (phantom sounds for now)
- Soulmasses can now be healed by using Lost Souls on it (items inside the common lost_soul tag)


- Added config lines for entity health and armor for entities that didn't have it already and changed values of some, the config has to be deleted and re-generated for these changes to take effect
  - Mobs/Entities:
    - Dark Sorcerer: Health 10, Bonus Armor 0 (unchanged)
    - Evil Forlorn: Health 15 -> 30, Bonus Armor 0 (unchanged)
    - Familiar Ghost: Health 10, Armor 0 (unchanged)
    - Forlorn: Health 15 -> 30, Bonus Armor 0 (unchanged)
    - Frost Giant: Health 50, Armor 8 (unchanged)
    - Moderately Sized Chungus: Health 14, Armor 0 (unchanged)
    - Remnant: Health 10 -> 20, Bonus Armor 0 (unchanged)
    - Rime Spectre: Health 10, Bonus Armor 0 (unchanged)
    - Soulmass: Health 50 -> 75, Armor 4 -> 10 (unchanged)
    - Sword of Freyr: Health 50 (unchanged), Armor 0 -> 10
    - Sphere of Warmth: Health 20, Armor 0 (unchanged)
    - Withered Demon: Health 80 (unchanged), Armor 0 -> 2
  - Bosses:
    - Decaying King: Health 500 -> 600, Armor 5 -> 10
    - Returning Knight: Health 400 -> 500, Armor 8 -> 15
    - Old Champion's Remains: Health 200 -> 300, Armor 5 -> 10
    - Frenzied Shade: Health 100 -> 150, Armor 0 -> 2
    - Monarch of Chaos: Health 400 -> 450, Armor 4 (unchanged)
    - Fallen Icon: Health 500 -> 550, Armor 20 (unchanged)
    - Day Stalker: Health 500 -> 600, Armor 10 -> 15
    - Night Prowler: Health 400 -> 500, Armor 10 (unchanged)


- Fabric only: Made it compatible with Epic Fight mod in case someone downloads the fabric version and uses Sinytra Connector with Epic Fight

## 1.2.3
- Fixed a bug preventing other weapons such as axes from disabling shields
- Increased the durability of Forlorn Scythe to be on par with the other legendary weapons
- Added compatibility with Terramity, Lost Souls from that mod will work with this mods interactions and recipes
  - Added it to the c:lost_soul (fabric) / forge:lost_soul tags, so if any other mods have similar items, they can be added into that tag through a datapack
- Forge specific: Fixed bug delaying custom keybind inputs if bound to mouse buttons

## 1.2.2
- All custom bows/crossbows' arrows apply custom effects from tipped arrows (can be disabled in config for each weapon)
- Galeforce ability doesn't need an arrow in inventory to shoot, won't consume one either and arrows can have custom effects, but they can't be picked up
- Added missing recipe to turn Chungus Emerald Blocks back into Chungus Emeralds
- Changed NightProwler ECLIPSE attack
  - It now summons a portal above itself
  - No longer shoots 3 skulls together
  - Spawns skulls randomly from the summoned portal with shorter interval between
  - These skulls may be hard to see, so added a config line ```night_prowler_eclipse_skulls_glow``` to make them glow if the player wishes so
- Fixed bug where ShadowOrbs would collide with each-other
- Removed custom hold animations for some weapons due to messing with Sinytra Connector and forge
- Weapons saving to the item NBT generate a new NBT if it doesn't exist already
- Dragon Staff Vigorous Fog ability heals tamed entities, including those not owned by the user based on config line ```dragon_staff_vigorous_fog_heal_tamed_entities_owned_by_others```
  - Added config line ```dragon_staff_vigorous_fog_damage_and_heal``` to change heal and damage by the ability
- Fixed bug where Holy Moonlight Greatsword and Night's Edge abilities would not spawn entities when standing on 1 block thick ground
- Fixed bug where Night Prowler would be stuck in the sky in phase 2
- Added config lines for each boss that contains a list of all status effects the boss is immune to (for example ```chaos_monarch_status_effect_blacklist```)
- Made the fabric version of this mod compatible with Epic Fight mod
- Made the mod compatible with Rat's Arsenal mod

## 1.2.1

### Bug Fixes
- Added missing translation for Purified Blood fluid block
- Replaced chungus tonic blacklist with whitelist in config to avoid crashes due to attempting to turn into other mods' entities (new config file called soulsweapons_chungus_tonic_whitelist)
- Fixed a bug where Glass Vials did not appear in inventory after using a Blood Vial in the first hotbar slot
- The Withered Wabbajack now looks at the luck attribute of the player instead of luck effect (additionally reworked all it's code)
- Chaos Crown and Chaos Helmet gives the player value to the luck attribute instead of an effect, this value can be changed in the config

## 1.2

### Important:

*   Updated fabric loader to 0.16.10 and geckolib to 4.7

### New Stuff:

*   Added Dark Moon Greatsword from Elden Ring
    *   Apply Permafrost post hit to mobs hit
    *   Use ability keybind to give yourself Frost Moon effect
    *   While having Frost Moon effect, use the blade to shoot out moonlight projectiles that apply Permafrost
*   Added Frost Moon effect, makes you immune to Permafrost, freezing and powdered snow
*   Made entities inside the freeze\_immune\_entity\_types tag immune to Permafrost and added Frost Giant and Rime Spectre to it
*   Added Purified Blood fluid, standing in it gives regeneration, using glass bottles on it will consume the fluid and give you potion of healing
*   Added Glass Vials, crafted with 5 glass
*   Added Blood Vials, gained by using glass vials on Purified Blood in a cauldron, using it will heal the user instantly by 3 hearts
*   Added new functionality to Crimson Obsidian:
    *   Standing on it damages the entity with hotFloor damage type, just like magma blocks
    *   If the entity is not a skeleton and the damage was successful, blood is stored in the block
    *   When a cauldron is under this block and enough blood is stored, Purified Blood will drip down into the cauldron, filling it up
*   Added Glaive of Hodir
    *   Attacking mobs grants stacks of Blade Dance effect
    *   Taking damage reduces stacks of Blade Dance by 1
    *   At 5 stacks of Blade Dance, gain Resistance 2 and Absorption 3 with 10 seconds cooldown
    *   Use the glaive to throw out a spectral glaive, passing through blocks and mobs, damaging and applying 125 posture loss to each mob it flies through
*   Added Blade Dance effect
    *   When the effect is added, each stack of Blade Dance increases attack speed and damage of the held “Blade Dance” weapon, only certain weapons are accepted (so far only affects the Glaive of Hodir)
*   Added Moonveil from Elden Ring
    *   Use to shoot out a wide horizontal moonlight wave, or shoot a vertical wave if crouching
*   Added Excalibur, crafted with Echo Shards
    *   Turn 30% of damage taken into damage to the stack instead
    *   Upon taking lethal damage and while holding the sword, there is a 30% chance to negate all damage and cause an explosion instead, knocking back enemies
    *   Use to shoot a sonic boom towards your last target hit if within 40 blocks range, else target the closest entity within 16 blocks
*   Changed the model of the Fallen Icon, gave it a changing texture based on the phase and made some of the animations smoother (some timings of the attacks have been changed due to this)
    *   Particles will now spawn on the sword instead of around the Fallen Icon itself
*   Added new Fallen Icon attacks and changed some aspects
    *   Added boss music (song made by NoRestForWicked), can be turned off with the music volume slider in options
    *   It no longer starts the fight with Unbreakable
    *   Heavy Swing: Charge up a heavy attack that shoots a large horizontal moonlight projectile. The sword does not have a hitbox here
    *   Unbreakable & Repulse: Unbreakable attack has been changed to give shorter Nausea, Slowness and Weakness, and the amplifier of Weakness has been reduced from 2 to 1. This attack no longer only triggers at the start of the fight, it can now randomly trigger throughout. In phase 2, the boss will only do this attack if recently hit by a projectile. The projectile is then “absorbed” (or copied rather), so the next time this attack occurs, an orb will spawn above the target that will shoot out 3 times the amount of projectiles that the boss had absorbed.
    *   Archimedean Spiral: The boss will charge up a heavy ground slam which will then cause ruptures outwards in multiple archimedean spirals pattern
*   Trick Weapons can now be made with datapacks
    *   Creating a new json file inside soulsweapons/trickweapons/item\_mappings.json and listing item ids mapped to another determines what the item will turn into, making it a trick weapon. A “replace” value needs to be specified first (like in tags) which is false by default, setting it to true will disable other trick weapons (including the default ones).
    *   An example file looks like this: {“replace”: false, “minecraft:apple”: “soulsweapons:chungus\_emerald”} This will map apples to chungus emeralds, turning apples into chungus emerald in game by pressing the switch trick weapon keybind.
    *   NB! If you want the custom trick weapons to give you the advancement, then they need to be included inside the trick\_weapons.json tag inside soulsweapons/tags/item folder
*   Added Simon’s Bowblade, a trickweapon that switches between a bow and blade form. They both deal increased damage against undead, while the bow has 5 ticks extra pull time (1.2 sec to pull fully) and applies 30 posture loss on entity hit too
*   Added Chungus Emerald blocks
*   Changed Chungus music disc recipe to be a circle instead of all slots filled
*   Moderately Sized Chunguses can now be in an “aggressive state” where their eyes are red, nothing will stop them from attacking you
*   Moderately Sized Chunguses can spawn everywhere as long as a monolith is within vicinity (no longer just forests)
*   Added Chungus bartering
    *   Use a Chungus Emerald on a Moderately Sized Chunguses, it will then consume it to give you an item based on a data pool
    *   The pool contains lots of useless items with high chances and overpowered items with extremely low chances
    *   The chunguses can also pick up the emeralds instead of you having to right click them
    *   Trading with them increases the chance for them to turn into Bosnian Big Chungus (based on the meme from r/comedynecrophilia), if in that state it will not give back an item when traded with, has 50 health and will drop 200 experience when killed (chunguses can also randomly spawn in this state with 1% chance)
    *   Chunguses in aggressive states will not be able to be bartered with
    *   Chunguses has a 5% chance to spawn as a Dream Chungus, they will only barter back between 1 and 8 (inclusive) Blaze Rods or Ender Pearls
*   Added new effect Chungus Tonic
    *   If the player has it, Moderately Sized Chunguses will no longer attack you given they are not in an aggressive state
    *   If any other entities has it, they will turn into a completely random entity (including projectiles, minecarts, etc) for 15 seconds, then turn back into a regular Moderately Sized Chungus
*   Added Chungus Tonic potion item, splash potion and lingering potion, all can be crafted with different recipes, the tonics will give Haste 3, Saturation and Chungus Tonic effect
    *   Potions are crafted with a Chungus Emerald into water bottles, you can right click any entity to make them instantly drink the potion instead of you
    *   Splash Potions are crafted with a Chungus Emerald Block into Chungus Tonic potions
    *   Lingering potions are crafted with dragons breath into Chungus Tonic Splash potions
*   Invading Forlorn no longer spawn on these blacklisted blocks to avoid overcrowding and awkward spawn positions (such as stuck in trees): Warped Wart, Warped Stems, Warped Nylium, Shroomlight, Crimson Stems, Nether Wart Block, Basalt blocks
*   Nerfed generation rate of Moonstone and Verglas ores
    *   Moonstone: Discard on air chance: 0 -> 0.75, min height -63 -> -80
    *   Verglas: Discard on air chance: 0 -> 0.75, max height 120 -> 300, ore count per chunk 48 -> 50
*   Added Chungus Staff, gives the user Chungus Tonic effect while in the inventory, using it gives the player speed and haste 3, use it on a Moderately Sized Chungus tames it. Can be acquired through chungus bartering
*   Translucent weapons can be right clicked to turn invisible in third person view
*   Advancements are now datagenned and some got changed to be goals instead of challenges or tasks (this might break progress for them), also added a few new ones:
    *   Infuse the Hallowheart
    *   Infuse the Arkenplate
    *   Craft all the different moonlight weapons (comes before all weapons advancement)
    *   Craft the Glaive of Hodir
    *   Kill the Warden after crafting the Excalibur
*   Added Frozen Lightning, it will now spawn when Mjolnir and the Leviathan Axe collide. It can be killed after 10 strikes, resulting in a big explosion and additional lighting
*   Soulbound weapons now drop to the ground if the owner dies mid-flight. The soulbound entity is also saved across death and respawn, meaning if you die while it travels back to you, calling it back at your respawn point/after respawning makes it travel to you (despite that link being previously broken in older versions)
*   Dragonslayer Swordspear and Heap of Raw Iron now have the “Dragon’s Scourge” ability, dealing bonus damage to dragons/entities inside the common tag “dragons.json” (damage is done past other calculations and is added flat regardless of attack speed)
*   Monarch of Chaos will now drop a “Recipe Page”, an item that details how to acquire Blood Vials
*   Holy Moonlight Pillars, Blackflame Explosions and Flame Pillars summon way fewer particles and now instead has an entity model

### Bug Fixes:

*   Chungus Monolith blocks now drop when mined
*   Soul harvesting weapons harvest dead mobs that died from the sweeping
*   Fixed a bug where this mod would overwrite damage bonuses from other mods
*   Fixed a bug where no cooldown was applied to Bluemoon Shortsword in creative
*   Fixed a bug where disabling parry ability on shields wouldn’t remove the tooltip
*   Night Prowlers Blackflame Snake attack now spawns particles on client instead of server, reducing packet info being sent which caused crashes
*   Fixed a bug where enchants (like smite and sharpness) did not increase the damage of moonlight projectiles
*   Fixed a bug where Mjolnir wouldn’t spawn it’s own share of special effects and lighting when colliding with the Leviathan Axe
*   Fixed a bug causing Soulbound weapons to enter the dead players inventory, therefore disappearing
*   Moonlight/Bluemoon Shortsword now work with controllers when using the Controllable mod
*   (Potentially) fixed EXCEPTION\_ACCESS\_VIOLATION crash caused by different bosses

### Config changes:

*   Added lines for Dark Moon Greatsword values (damage, attack speed, etc)
*   Added lines for Glaive of Hodir values (damage, attack speed, etc)
*   Added lines for Excalibur values (damage, attack speed, etc) and a line for if it should inform the player if no target is within range of the ability
*   Added lines for Simon’s Bowblade values (damage, attack speed, etc)
*   Added lines for how much Blood Vials should heal, duration and amplifier of regeneration
*   Added line for whether the ability on Translucent Weapons can be used or not
*   Added attack speed and damage lines for all Translucent Weapons
*   Added disable recipe lines for all Translucent Weapons
*   Added line for whether Chungus Tonics can be used or not
*   Added line for how many ticks should pass until the entity should turn into a Moderately Sized Chungus when affected by Chungus Tonic
*   Added blacklist for entities that other entities affected by Chungus Tonic CANNOT turn into
*   Added lines for Chungus Staff (damage, attack speed, can be disabled, is fireproof)
*   Added line to disable chungus bartering
*   Replaced righteous\_undead\_bonus\_damage line with similar lines for each unique undead bonus trick weapon with name in front instead, for example Ludwig’s Holy Blade has ludwigs\_holy\_greatsword\_righteous\_undead\_bonus\_damage (so each weapon has its own line)
*   Added lines for moonlight projectile damage for each moonlight weapon, in other words bluemoon, moonlight and pure swords no longer use the same config line, they have each their own
*   Changed config lines for \_enchant\_reduces\_cooldown\_id to \_ids and it will now take in an array of strings instead of just a singular string, meaning you can write as many enchant ids as you want, but only the highest level of them all will still be the only one that counts, not all combined. For example, if the list contains “damage” and “unbreaking” and the items has unbreaking 3 and smite 4, then the cooldown level is 4
*   Added lines for disabling respawn mechanics for bosses (mainly regarding blocks), for example using Demon Chunk on Blackstone Pedestal to respawn the Decaying King. To disable Day Stalker and Night Prowler, disable the use of the Chaos Orb item instead
*   Added line for “Dragon’s Scourge” bonus damage for Dragonslayer Swordspear and Heap of Raw Iron
*   Added lines to disable ground withering effect by the Monarch of Chaos and the Cape of Chaos (chaos\_cape\_wither\_ground and chaos\_monarch\_wither\_ground)