# SupremeMC

SupremeMC is a content-expansion mod built around a broad vanilla-inspired progression system. It adds custom ores, biomes, crops, equipment tiers, enchantments, and mob variants while keeping the overall feel of Minecraft recognizable and familiar.

This page summarizes the current feature set in a wiki-style format.

## Overview

SupremeMC introduces a large set of new content across several major systems:

- custom ore and mineral progression, including Aquamarine, Abyssalite, Emerald, Amber, and Anthracite-based building materials
- new biome generation and terrain variation, including Florida Plains and Cays
- custom mob variants, including Fire Creepers, Snow Creepers, and Ender Spiders
- custom enchantments and potion behaviors
- expanded crop, food, and small-world content such as cotton, coconut, palm trees, tomatoes, grapes, and corn
- compatibility across both Fabric and NeoForge loaders with generated data resources shared between them
- Lavender endspar behaves like a nylium block on end stone: it reverts to end stone in darkness and bonemeal spreads it across nearby exposed end stone.
- Bonemealing lavender endspar spreads lavender roots and lavender fungus across nearby endspar, using the same vegetation pattern as warped and crimson nylium. Bonemealing a lavender fungus planted on lavender endspar grows it into a huge fungus with a lavender stem trunk, a lavender wart block cap, and embedded shroomlights, just like huge warped and crimson fungi.
- Lavender adds a wart block, crafting table, and bookshelf using its lavender wood family.
- Crimson and warped boats and chest boats can be crafted from their corresponding Nether wood planks.
- Crimson and warped boats and chest boats float on lava the same way ordinary boats float on water, letting you sail across Nether lava lakes.
- The End's main island and its surrounding void gap (within 1,024 blocks of the origin) generate exactly like vanilla; Lavender biomes and their vanilla counterparts only appear on the outer islands beyond that radius.

## World Generation and Biomes

### Ice Caves

Ice Caves replace Dripstone Caves in cold regions underground. Instead of dripstone clusters and pointed dripstone, these caves use packed ice clusters and icicles. Giant dripstone-block pillars do not generate in this biome variant.

Icicles follow the same placement, growth, breaking, and fluid-transfer behavior as pointed dripstone, using custom textures and behaviors to match the cold-cave theme.

### Florida Plains

Florida Plains is a warm inland biome that occupies the drier portion of the climate slot normally used by Mangrove Swamp. It is flat, lightly watered, and features swamp-like vegetation with a tropical green tint.

Features include:

- patchy shallow water and swamp-like flat terrain
- jungle-tinted grass and foliage
- palm trees with coconuts mixed with swamp oak and jungle bushes
- tomato patches, dead bushes, mushrooms, sugar cane, firefly bushes, pumpkins, and seagrass
- swamp-style mob spawns and the ability for witch huts to generate

### Cays

Cays is a small island biome that replaces the warm side of Mushroom Field's isolated deep-ocean edge generation. It is a flat, sandy, island-based biome with frequent palm trees and warm-ocean-style water coloration.

Features include:

- sea-level sand terrain with sandstone beneath
- shoreline palm generation with coconuts
- tropical green foliage and turquoise warm-ocean water tint
- warm-ocean vegetation such as seagrass, sea pickles, and coral/vegetation clusters
- beach-like mob spawns, including turtles and hostile night mobs
- extremely high freshwater spring density, allowing water holes and seepage pools across the island
- bone meal spreading Beach Grass across nearby sand, with Tall Beach Grass as the grown variant

### Drowned Spawn Adjustments

Drowned spawn weights are tuned to match Bedrock-style behavior in key aquatic areas, including rivers, Dripstone Caves, Ice Caves, ocean biomes, and Frozen Rivers.

### Nether Mineshafts

Abandoned mineshafts can now generate in any Nether biome, with the same layout, rooms, and loot as an ordinary overworld mineshaft, except its oak planks and oak fences are replaced with warped planks and warped fences.

### End Mineshafts

Abandoned mineshafts can also generate buried inside the end stone of the End's outer islands (never the central main island), with the same layout, rooms, and loot as an ordinary overworld mineshaft, except its oak planks and oak fences are replaced with lavender planks and lavender fences.

## Blocks, Ores, and Building Materials

### Aquamarine and Abyssalite

Aquamarine and Abyssalite form the primary high-tier mineral progression.

Aquamarine ore spawns in ocean biomes and can be smelted or blasted into Aquamarine. The material supports Fortune, can be crafted into blocks, and is used in diamond-tier equipment that grants Water Breathing when fully equipped. Aquamarine tools and armor include custom textures and special behavior, including mud conversion and wet farmland conversion.

Abyssalite follows the endgame progression line. Atlantis Debris generates in ocean biomes and smelts or blasts into Abyssalite Scrap. Four scrap and four Prismarine Crystals craft an Abyssalite Ingot, which is used to create blocks and upgrade gear. Abyssalite tools and armor have higher durability and combat stats, are upgradeable from Aquamarine equipment through a smithing template, and retain underwater mining performance while preventing Mining Fatigue from being applied when a full set is worn.

### Amber and Burning Diamond

Amber introduces a marine-adjacent material line with ores, blocks, tools, armor, and recipes. A full Amber armor set grants Water Breathing, and its tools provide wet-farmland and mud conversion behavior.

Burning Diamond is a diamond-tier material line with custom tools and armor. A complete Burning Diamond armor set continuously grants Fire Resistance.

### Glendstone

Glendstone is a glowstone-style building block that emits maximum light, is immune to Ender Dragon damage, and generates naturally in hanging clusters under End terrain.

### Xylium

Xylium ore and Xylium blocks use End Stone's block properties and are immune to Ender Dragon damage. Mining Xylium ore drops 4-5 Xylium Dust before Fortune bonuses, while Silk Touch drops the ore itself.

### Emerald and Iron-Style Building Blocks

Emerald gear acts as a craftable intermediate tier between Iron and Diamond. Emerald tools and armor use iron-equivalent mining characteristics with higher durability and a custom craft progression. Chainmail armor is also craftable using chain links.

The mod also adds a wide set of decorative and utility blocks derived from common materials, including stairs, slabs, and walls for stone variants and material-based building pieces. Nether Anthracite ore generates in the Nether and behaves like coal ore with Silk Touch and Fortune support.

Andesite, diorite, and granite bricks also have mossy variants. Each mossy family includes the full block, stairs, slabs, and walls; the base block is crafted shapelessly from matching bricks and a vine, while the shaped and stonecutter recipes mirror the regular brick families.

Cracked andesite, diorite, and granite bricks are made by smelting their matching SupremeMC bricks. Cracked end stone bricks and cracked quartz bricks are made by smelting the corresponding vanilla bricks.

### Palm and Cotton

Palm trees generate naturally on beaches and support custom coconut growth. Palm logs, palm saplings, palm leaves, and coconuts participate in composting and custom farming behavior.

Cotton grows as a bush crop in Plains and Sunflower Plains. It can be harvested, planted, and crafted into cotton armor. Cotton, coconuts, palm materials, tomatoes, grapes, and corn are all included in the composting system.

## Plants, Crops, and Food

### Crops and harvestables

The mod adds several functional crops and plant systems:

- Coconut and Coconut Seeds, including hanging coconut growth on palm logs
- Cotton bushes with four growth stages and Fortune-compatible harvesting behavior
- Corn stalks that grow like twisting vines and always drop corn when broken
- Grape vines that hang from leaves or moss and grow downward in a weeping-vine style
- Tomato bushes found in Florida Plains and other warm, lush areas

### Food items

Food additions include:

- Calamari and Cooked Calamari, with cooking times similar to other food sources
- Grapes, Tomatoes, and Corn, each restoring hunger and saturation
- Coconut, which clears all status effects like milk but is consumed entirely
- Milk bottles, filled from source milk or milk cauldrons and emptied back into milk cauldrons one level at a time

### Brewing and potions

The brewing system introduces several custom potion families:

- Experience Dust brews Splash Awkward Potions into Bottles o' Enchanting
- Clover brews into Luck potions and variants, including Bad Luck conversion and tipped-arrow compatibility
- Rotten Flesh brews Hunger potions, with long and strong variants supported
- Wither Roses brew Decay potions that apply Wither effects, with splash, lingering, and tipped-arrow support

## Enchantments and Item Mechanics

### Custom enchantments

SupremeMC adds a set of custom enchantments with their own data-driven definitions, weights, costs, equipment targets, and incompatibility rules:

- Bounty
- Venom
- Decay
- Wisdom
- Smelting
- Tension
- Curse of Mass
- Curse of Sloth

Several of these have explicit gameplay effects:

- Smelting auto-smelts mined drops from supported blocks and materials, stacking with Fortune and conflicting with Silk Touch.
- Venom applies Poison I after a direct melee hit, with durations scaling by enchantment level.
- Decay applies lethal Wither I after a direct melee hit, with stronger durations at higher levels.
- Tension is a bow-only enchantment that reduces draw time while preserving the effective power of a full vanilla draw. It cannot be combined with Punch.
- Super Channeling is a treasure-only trident enchantment that calls lightning on valid targets and lightning rods regardless of weather. It works with vanilla and Abyssalite tridents and cannot be combined with Channeling or Riptide.
- Wisdon multiplies experience levels.
- Curse of Mass increases Slowness while cursed armor is worn or a cursed item is held, scaling with the number of affected armor pieces.
- Curse of Sloth applies a matching escalating Weakness effect.

### Stacking and utility rules

The mod also adds a number of practical stack and behavior adjustments:

- Ender pearls, snowballs, buckets, signs, hanging signs, and cakes stack to 64
- Potions stack to 16
- whole, unbroken cakes drop as items when broken with Silk Touch, while partially eaten cakes do not

## Mobs and Creature Variants

### Hostile mob equipment

Hostile mobs receive upgraded equipment logic:

- Zombies, Drowned, Husks, Skeletons, Strays, Piglins, and Wither Skeletons can spawn with armor scaled to difficulty
- swords and weapon tiering are also difficulty-adjusted
- armor sets become fuller and stronger at higher difficulties
- enchantment chances and simulated levels increase with difficulty
- durability wear is preserved so gear remains consistent with vanilla drop expectations

### Custom hostile variants

The mod adds and modifies several custom hostile creatures:

#### Fire Creeper

The Fire Creeper behaves like a vanilla Creeper but always causes its blast to ignite the area. Charged Fire Creepers keep the doubled blast radius and ignite the blast area as well.

#### Snow Creeper

Snow Creepers replace vanilla Creeper spawns in cold land biomes. Their explosions freeze affected blocks into Powder Snow or Ice, and they drop extra snowballs.

#### Snow TNT

Snow TNT explodes like vanilla TNT, replacing affected non-air, non-water blocks with Powder Snow. While primed, it uses the Snow TNT texture and emits snowflake particles.

#### Fire TNT

Fire TNT explodes like vanilla TNT and ignites blocks in the blast area. While primed, it uses the Fire TNT texture and emits flame and smoke particles.

#### Ender Spider

Ender Spiders use spider-like hostile behavior with Enderman-style teleportation, portal particles, and teleport sounds. They spawn in the End and drop normal spider loot plus a chance of Ender Pearls.

## Custom Creatures and Special Features

- Drowned spawn weights are adjusted across rivers, caves, oceans, and frozen water biomes.
- Capped item stacking and custom potion conversion behaviors are implemented for gameplay convenience.
- The mod includes custom creative tab organization and structured generated data resources shared between Fabric and NeoForge.

## Loader Support and Data Generation

Content is placed in its own SupremeMC creative tab rather than the vanilla Ingredients tab. Models, blockstates, recipes, loot tables, language data, and tags are generated from data providers, and Fabric packages the same generated JSON resources so both loaders stay aligned.

## Notes

This page is a summary of the implemented content in the current build. The goal is to present the system in a readable, wiki-like format rather than as a raw checklist or changelog.

