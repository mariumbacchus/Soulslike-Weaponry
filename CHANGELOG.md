# Changelog

## 1.3.1
- Fixed a bug preventing Chungus Potions from being crafted
  - Recipes for the Splash and Lingering has been changed due to this

## 1.3
### Important: All config files have been moved to "soulsweapons" folder inside config folder
- The original config is now inside config/soulsweapons/soulsweapons.json together with the other files like _client.json and _chungus_tonic_whitelist.json

### New additions
- Added Dragonbane, a new katana from Skyrim having the Chain Lightning ability builtin and does 20 bonus damage to dragon entities, can be made with Netherite ingots or Dragon Bones from Ice & Fire (accepted material is inside soulsweapons:dragonbane_material item tag)
- Added Supernova, a spiky mace that does bonus durability damage to armor, has a chance to inflict fire post hit and has passive firethorns effect while holding it
  - Using it spawns Flame Pillars a short path leaving behind pools of Molten Metal that sets the targets in it on fire, disables shields and does 100 durability damage to it
  - Calculated Fall detonation spawns Flame Pillar ripples outwards with the amount based on fall distance
- Added Mehrunes' Razor, a new dagger that has a chance to deal bonus damage equal to the missing health of the target, with an additional very small chance to instantly kill the target
  - There is a blacklist of entities in the config that will make the razor unable to instakill those entities
- Added Tonitrus, a mace from Bloodborne which you can use to give yourself the new Stormveil effect, making you apply Chain Lightning post hit and immune to Lightning damage, using it again with the effect makes you summon a lightning strike on the target post hit, then removing the Stormveil effect
- Added the Stormveil effect, making the wearer immune to Lightning damage and has a chance to inflict thorns lightning damage on targets hitting the wearer
- Added Bloodlust, a katana from Dark Souls 3 applying large amounts of bleed buildup and can be used to damage yourself, but also give you Strength 2 and Bloodthirsty effects, targets suffering Blood Loss around you while having this weapon in hand also gives you Strength
  - Bleed buildup is a part of the reworked bleed system, more on that later in the log
- Added the new Blight Carrier gun enchantment, making Silver Bullets apply stacking Blight effect on targets, increasing their damage taken based on their armor amount
- Added the new Frostsilver gun enchantment, making Silver Bullets apply strong Permafrost effect if the target Posture Breaks
  - Hitting a target with Posture Break effect while having Permafrost triggers the Ice Explosion early and removes the effect (together with the Posture Break since it triggers)
    - Note: Permafrost & other damage over time effects still trigger Posture Breaks!
- Added the new Phantom Trace gun enchantment, guns now fire additional copies of the main bullet that fly off after a delay, they do 50% damage, but has all other effects the main bullet has
  - Bullets with this enchant are rendered with a cyan overlay
- Added the new Tether gun enchantment, making Silver Bullets drag the target towards you if outside a certain range
- Added the new Ricochet gun enchantment, granting Silver Bullets the ability to bounce off of blocks with bounce amount based on enchant level
  - On contact with a block, the bullets will bounce off and:
    - have its speed reduced to max 1.5 speed
    - first try to target the mob the owner last attacked
    - if no mob was attacked recently, it will target the closest mob around it
    - if no mob is close, it will bounce off a random direction
  - A gun cannot have both Ethereal & Ricochet enchant

### Item changes
#### Guns:
- Guns can no longer have bow enchants, and Punch & Flame no longer affects guns and Silver Bullets (Power & Infinity still work)
- Infinity on guns makes them properly only need 1 bullet to fire and doesn't consume it either
- Changed the use animation of guns
- Adjusted the position of the Gatling Gun when using in first person (now centered more)
- Adjusted the particle spread of the guns
- Adjusted Chain Lightning enchantment to do damage scaling off of the damage to the projectile instead of a constant value
  - Due to this the config line ```chain_lightning_enchant_damage_per_level``` has been replaced with ```chain_lightning_enchant_damage_mod_per_level```
- Bullets with Ethereal enchant are now rendered translucent
- Bullets now spawn Soul Fire Flame particles upon de-spawn
- Explosive Rounds enchantment no longer breaks blocks around the target hit with the explosion
  - Added config line to toggle this (when on, the explosion counts as a mob, so gamerule doMobGriefing can also be used to turn off)
- Added config lines for the max enchant levels for all enchants (where it matters)
- The max age of bullets has been reduced from 100 -> 60 (ticks) (ethereal makes it 25 like before)
  - For cannonballs: 100 -> 60 (ticks) with Ethereal enchant, 300 -> 120 (ticks) otherwise
- Buffed Posture Loss applied by all guns
  - Blunderbuss: Base 25 -> 30 (per bullet), Bonus per enchant level 2 -> 3
  - Hunter Cannon: Base 60 -> 120, Bonus per enchant level 60 -> 50
  - Hunter Pistol: Base 30 -> 50, Bonus per enchant level 7 -> 10
  - Gatling Gun: Base 9 -> 10, Bonus per enchant level 1 -> 3

#### Armor:
- Hallowheart makes the wearer completely immune to Wither effect at all times instead of just under 50% health
- Arkenplate also gives Magic Resistance 2 under 50% health
- Enhanced Arkenplates Mirror ability triggers at under 50% health instead of 33%
- Soul Robes set gives Magic Resistance 4 instead of 2 (80% reduced magic damage)
- Armor that grants effects no longer display potion particles
- Some armor sets give posture and bleed resistances (more on what that entails in the "Reworked Mechanics" section)
  - Arkenplate & Enhanced Arkenplate: 
    - Bleed buildup resistance: 100
    - Bleed damage resistance: 60
  - Chaos Helmet: 
    - Bleed buildup resistance: 60
    - Bleed damage resistance: 40
  - Forlorn Armor (full set): 
    - Posture loss buildup resistance: 140
    - Base posture increase: 80
    - Bleed buildup resistance: 200
    - Bleed damage resistance: 100
  - Soul Ingot Armor (full set): 
    - Bleed buildup resistance: 25
    - Bleed damage resistance: 15
  - Hallowheart & Enhanced Hallowheart: 
    - Posture loss buildup resistance: 25
    - Bleed buildup resistance: 40

#### Weapons:
- Added Ice and Fire dragons to the tag Dragon's Scourge ability checks to apply bonus damage (Dragonslayer Swordspear and Heap of Raw Iron have this ability)
- Added new Withered Wabbajack on entity hit effects:
  - Chance to launch the target in the air with random launch power
  - Chance to apply Chungus Tonic effect on the target, turning it into a random entity
  - Adjusted how the chances for the effects are calculated with weights
- Calculated Fall ground explosion no longer damages mobs on the same team as the user (like tamed mobs)
- Kirkhammer now applies Slowness 2 and Posture Loss on targets hit in the Calculated Fall explosion
- Excalibur has a new ability called Lightbringer, making the wielder immune to Darkness and Blindness, while instead gaining Speed, Strength and Night Vision when applied with those effects
- Leviathan Axe has a smoother spin animation
- Added Szombie's models of Leviathan Axe & Mjölnir as a built-in resourcepack for those weapons
- Heap of Raw Iron only gives Bloodthirsty effect with max amplifier 3, and the ticks before damage is capped at each 20th tick instead of being lower and lower the higher amplifier is
  - Bloodthirsty effect now increases bleed applied by the items: bleed = amplifier * originalBleed * 0.75
- Ultra Heavy weapons now apply Posture Loss on their own without the need for Stagger enchant
  - Posture Loss bonus modifier for those weapons when having Stagger enchant has been reduced from 2 to 0.9 due to this
  - Kirkhammer applies 40
  - Darkin Blade applies 30
  - Heap of Raw Iron applies 30
  - Featherlight applies 20
  - Nightfall applies 40
  - Supernova applies 35

### Bosses
- Changed Night Prowler's Darkness Rise attack to spawn delayed Blackflame Snake explosions instead of applying continuous Decay effect
- Night Prowler will heal from projectiles at under 50% health instead of 16.7%
- Changed Day Stalker attacks
    - No longer heals when empowered, gains Speed II instead
    - Sky High ability summon waves of Flame Pillars on landing
    - Air Combustion attack has bigger radius
    - Getting hit by a projectile under 50% health makes Day Stalker retaliate by spawning an Air Combustion on the attacker
    - Buffed the range of Flames Edge attack
    - Flames Reach summons a Flame Pillar on the slam part of the attack if in phase two
    - Overheat attack will now summon Flame Pillar lines at the left & right of the original line 4 times with delay, resulting in 9 lines in total
    - Chaos Storm now spawns pools of Molten Metal if in phase two, damaging mobs in it and setting them on fire
- Changed Fallen Icon 2nd phase attacks
  - Thrust attack now has 3 other follow-up attacks: A ground slam, a sword swipe and a blinding light ground slam with his fist, spawning projectiles only with Hard difficulty on (unlike the real Blinding Light attack which always does this in phase 2)
  - Increased the trigger range for Blinding Light attack and expanded the hitbox slightly
  - Sword of Light now shoots wider projectiles that go through entities and walls, with the last slam & big projectile being replaced with the old Moonfall attack, summoning pillars of light along a path
  - Moonfall now summons 7 lines of light pillars every 45th degrees (except at 180 degrees) instead of just 1 ahead of the boss
  - Moonveil attack now starts with the Fallen Icon leaping towards its main target, then the AOE explosion triggers as he pierces the ground with his sword, with an additional delayed AOE coming after a short while like before
  - Core Beam has bigger range to trigger, higher explosion power and no longer destroys blocks
  - Heavy Swing attack summons a wider projectile that also goes through walls and entities

### Bugfixes
- Fixed a bug causing guns not to find stacks of Silver Bullets in inventory even though the player has enough, and now decrements correct amount as well
- Fixed a bug causing Posture Break to apply debuffs upon being cleared that don't ever wear off

### Reworked Mechanics
- Reworked Posture Loss mechanic
    - Instead of max posture loss being flat 200 per entity, it now scales off of the size of the entity with a base posture unit the entity has
    - Entities have a posture loss resistance, reducing the amount of posture loss applied, or increase it if the value is negative
    - The base posture unit is universal in the config, but you can override this by making an "entitystats" datapack json file
    - The higher the base posture unit, the higher the max posture for the entity is, the base posture unit attribute gotten from some armor items also increase this
    - Entity stats files contain values such as bleed & posture loss buildup resistance, max posture loss, base posture loss unit for that entity and so on
    - The files representing the entities must be named after the entity and must be placed inside the folder "entitystats" inside the namespace the mob is from
    - Example: for the entity returning_knight, it needs to be inside data/soulsweapons/entitystats/returning_knight.json, while for the zombie it would be in data/minecraft/entitystats/zombie.json
    - Additionally, Posture Loss is reduced at a slower rate, being each 8th tick instead of 4th
    - Hitting an entity now displays the targets posture loss as its own bar
    - Added a client config where you can disable HUD elements, such as the target posture loss bar, or the players bleed/posture loss bars
    - Some bosses are more easily posture broken, while others are harder to break
    - As listed above, the guns have their posture loss applied buffed
    - Some armor sets give posture resistances (can be changed in config)
    - Posture Break now deals 5% of the targets max health as bonus damage
      - Added config lines for Posture Break damage
      - Base value was reduced to 2 against players and 6 against other mobs
- Reworked Bleed effect
    - It now works as a buildup, dealing damage as soon as the bar is filled up (just like in the Souls series), max bleed is still 200
      - Max bleed can be universally changed in config, but can also be changed for just one entity in the entitystats file with the line max_bleed
    - Skofnung when empowered now applies 60 bleed value with the bleed effect
    - Whirligig Sawblade applies 25 bleed each tick
    - Old Champion's Remains applies 100 bleed on some of the attacks that previously only applied the effect
    - Bleed buildup resistance reduces the buildup and is universally at 0, but can be changed for each entity by making an entity stats file mentioned above
    - Moonveil applies 25 bleed post hit
    - The damage is universally 6 + 10% max hp of the target, but this amount also varies depending on the mob's bleed damage resistance
    - Some bosses in the mod are weaker to bleed, while others are stronger against it
    - Some armor sets give bleed damage and buildup resistances (can be changed in config)
- Bosses and some mobs are now weaker/stronger against bleed and posture loss
  - Decaying King: Higher posture loss resistance; Very high bleed buildup resistance, but very low bleed damage resistance
  - Returning Knight: Weak to posture loss; Extremely high bleed resistance (both buildup and damage)
  - Old Champion's Remains: Very weak to posture loss; Extremely high bleed resistance
  - Frenzied Shade: Weak to posture loss; Immune to bleed
  - Fallen Icon: Weak to posture loss; Immune to bleed
  - Monarch of Chaos: Slightly resistant to posture loss; Immune to bleed
  - Day Stalker: Very high resistance against posture loss; Weak to bleed
  - Night Prowler: Highly resistant to posture loss; Slightly stronger against bleed
  - Withered Demon: Mildly resistant to posture loss; Very weak to bleed


### Config changes
- Added velocity config lines for moonlight projectiles
- Added config lines for Lunar Herald amplifier and duration applied by the Lunar Ring (moonstone_ring)
  - Also changed config line ```moonlight_ring_projectile_cooldown``` to ```moonstone_ring_projectile_cooldown``` for consistency
- All int values in the config have been changed to floats
  - If you tried to change an int value to a float on a line that would not accept it (like 9 -> 9.5 on the Darkin Blade weapon damage line), the config would reset due to it thinking the value is invalid. To avoid confusion and reset, all previous int (whole number) lines has been changed to float, so decimals can be used for them. Most of them will still ignore the decimals like before.
- Added options to always see the item tooltip in the client config

### Keybind changes
- Switched a few keybinds, I still encourage to switch these to a button that fits better for you
  - Parry: RIGHT ALT -> R
  - Return Freyr Sword: R -> Z
  - Stationary Freyr Sword: Z -> RIGHT ALT
- Added keybinds for showing item tooltip info and lore, these are unbound by default and when unbound will by default use SHIFT and CONTROL to display like usual
  - NOTE: Binding them to keys WILL override other keybinds so for example setting either to TAB will override the "show player list" function



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