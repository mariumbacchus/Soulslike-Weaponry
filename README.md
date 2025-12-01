<p style="text-align: center;">
    <img src="https://i.imgur.com/IYq2Xse.png" alt="Logo"/>
</p>

Welcome to Soulslike Weaponry, a mod that started with me wondering how fun it would be to add the Moonlight Greatsword to Minecraft, which turned out to be a bigger project than I anticipated.

The mod adds 60+ legendary weapons and plenty of new armor sets, all with new and unique abilities! You can test them out against the new challenging boss fights which will reward you with Lord Souls, the items you need to create most of the weapons. If you ever felt like the weapons Minecraft provides aren’t enough and want cool new boss fights, then this mod is for you.

The wiki here may not be up-to-date, but the mod supports recipe mods like Roughly Enough Items, so I suggest installing that while you're at it. **You can download the mod on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/mariums-soulslike-weaponry) or [Modrinth](https://modrinth.com/mod/mariums-soulslike-weaponry), any other place is not safe.**

The mod is highly configurable, you can always edit boss & weapon attributes, such as damage, cooldowns, and so on.
The config folder is named `soulsweapons` and will contain all the config files you need, with the main one being `soulsweapons.json`.

---

# Other features

## Upgrading System
As of version 1.4, the mod adds **Twinkling Titanite**. This item can be used together with a **Netherite Upgrade Smithing Template**
inside the **Smithing Table** to upgrade your item. This will add a bonus attribute based on the item type, such as damage & 
attack speed for melee weapons, pull time & range damage for bows/crossbows, mining efficiency for mining tools, and 
armor values for armor. It will also boost the existing abilities on your legendary weapon!

<p style="text-align: center;">
    <img src="https://i.imgur.com/YL7NJer.png" alt="Upgrade Gear Example"/>
</p>

The max upgrade level is 5 by default. This can be changed in the config, but be aware that the weapons are balanced around the max level being 5, so some number changes may be needed if you feel like changing this value.

The upgrading stats are based off of new recipes with the `_upgrade` after the name that has the type `soulsweapons:smithing_item_upgrade`, so you can easily make datapacks if you want to change the values. These files are in the recipe folder and contain the info for the recipe itself as well as the values added to the weapon per level. 
An example is the `axes_upgrade.json` recipe:
```
{
  "type": "soulsweapons:smithing_item_upgrade",
  "addition": {
    "item": "soulsweapons:twinkling_titanite"
  },
  "base": {
    "tag": "minecraft:axes"
  },
  "fallback": true,
  "primaryBonus": 1.25,
  "secondaryBonus": 5.0,
  "template": {
    "item": "minecraft:netherite_upgrade_smithing_template"
  }
}
```
The `primaryBonus` and `secondaryBonus` represent different things based on the item.
- Mining tools: `primaryBonus = attack damage`, `secondaryBonus = mining efficiency`
- Bows/Crossbows: `primaryBonus = projectile damage`, `secondaryBonus = draw speed`
- Armor: `primaryBonus = armor`, `secondaryBonus = armor toughness`
- Guns: `primaryBonus = projectile damage`, `secondaryBonus = nothing`
- Default upgrade (like swords): `primaryBonus = attack damage`, `secondaryBonus = attack speed`

In the example file it is defined that all axes will gain **1.25 attack damage** and **5 mining efficiency** per upgrade level.
Another thing to note is that `fallback = true` here. This means that unless another axe has its own upgrade file, it will
default to this one that applies this recipe to all axes within the axes tag. If a file is made for the diamond axe for example,
as long as `fallback = false` or **not defined at all** it will correctly override the tag upgrade and add the correct values
defined in its own file.

An example of this is the `gatling_gun_upgrade.json`. The gatling gun is inside the `minecraft:bows` tag minecraft provides so it can
use the correct animation when using. Since the tag `soulsweapons:ranged_item_upgradables`
(which is used inside `ranged_item_upgradables_upgrade.json` recipe) contains both `minecraft:bows` and `minecraft:crossbows`,
the gatling gun bonuses would default to those declared in `ranged_item_upgradables_upgrade.json`. By creating `gatling_gun_upgrade.json`,
where `fallback` is not declared or false, this new file will now override the fallback one and add correct values, those being
**1.0** as primary bonus instead of **0.4** (like how it is in `ranged_item_upgradables_upgrade.json`).
```
{
  "type": "soulsweapons:smithing_item_upgrade",
  "addition": {
    "item": "soulsweapons:twinkling_titanite"
  },
  "base": {
    "item": "soulsweapons:gatling_gun"
  },
  "primaryBonus": 1.0,
  "secondaryBonus": 0.0,
  "template": {
    "item": "minecraft:netherite_upgrade_smithing_template"
  }
}
```

## Trickweapons
The mod adds several trickweapons, items that while you hold them, you can press **B** (by default) to switch them out for another
weapon. All data such as enchants, attributes or stack damage will get ported over. It is also possible to create a **datapack**
if you want to add your own trickweapons.

Inside `data/soulsweapons` there exists a folder named `trickweapons`. This contains the `item_mappings.json` file, a json
with all the weapons and what the weapon will turn into. By default, the file will look like this:
```
{
  "replace": false,
  "soulsweapons:kirkhammer": "soulsweapons:silver_sword",
  "soulsweapons:silver_sword": "soulsweapons:kirkhammer",
  "soulsweapons:holy_greatsword": "soulsweapons:silver_sword",
  "soulsweapons:holy_moonlight_greatsword": "soulsweapons:holy_moonlight_sword",
  "soulsweapons:holy_moonlight_sword": "soulsweapons:holy_moonlight_greatsword",
  "soulsweapons:simons_bowblade": "soulsweapons:simons_blade",
  "soulsweapons:simons_blade": "soulsweapons:simons_bowblade"
}
```
If you want to add a custom mapping, simply make a datapack overriding this file. If you want to completely override the
old mappings, then you can set `replace` to `true`, if `false` it will just add your new mappings to the existing ones.
To add a mapping, you simply need to add the item id's. For example, adding:
```
"minecraft:diamond_sword": "minecraft:apple",
"minecraft:apple": "minecraft:diamond_sword"
```
will make it so Diamond Swords can turn into apples, and vice versa.
