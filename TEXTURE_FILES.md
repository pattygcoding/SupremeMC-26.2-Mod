Icicle block textures are supplied under `common/src/main/resources/assets/suprememc/textures/block/`: `icicle_down_base.png`, `icicle_down_frustum.png`, `icicle_down_middle.png`, `icicle_down_tip.png`, `icicle_down_tip_merge.png`, `icicle_up_base.png`, `icicle_up_frustum.png`, `icicle_up_middle.png`, `icicle_up_tip.png`, and `icicle_up_tip_merge.png`.
# Required Texture Files

Place all texture PNGs in the shared common resources directory. Both Fabric and NeoForge load these files from the same location:

```text
common/src/main/resources/assets/suprememc/textures/
├── block/
│   ├── aquamarine_block.png
│   ├── aquamarine_ore.png
│   └── deepslate_aquamarine_ore.png
└── item/
	├── aquamarine.png
	├── aquamarine_axe.png
	├── aquamarine_boots.png
	├── aquamarine_chestplate.png
	├── aquamarine_helmet.png
	├── aquamarine_hoe.png
	├── aquamarine_leggings.png
	├── aquamarine_pickaxe.png
	├── aquamarine_shovel.png
	└── aquamarine_sword.png
└── entity/equipment/
	├── humanoid/aquamarine.png
	├── humanoid_baby/aquamarine.png
	└── humanoid_leggings/aquamarine.png

Anthracite textures:

- `common/src/main/resources/assets/suprememc/textures/block/nether_anthracite_ore.png`
- `common/src/main/resources/assets/suprememc/textures/block/anthracite_block.png`
- `common/src/main/resources/assets/suprememc/textures/item/anthracite.png`

### Abyssalite

Add these PNGs to the same shared directory for US-003 and US-004:

```text
common/src/main/resources/assets/suprememc/textures/
├── block/
│   ├── abyssalite_block.png
│   └── atlantis_debris.png
├── item/
│   ├── abyssalite_axe.png
│   ├── abyssalite_boots.png
│   ├── abyssalite_chestplate.png
│   ├── abyssalite_helmet.png
│   ├── abyssalite_hoe.png
│   ├── abyssalite_ingot.png
│   ├── abyssalite_leggings.png
│   ├── abyssalite_pickaxe.png
│   ├── abyssalite_scrap.png
│   ├── abyssalite_shovel.png
│   ├── abyssalite_sword.png
│   ├── abyssalite_trident.png
│   └── abyssalite_upgrade_smithing_template.png
└── entity/equipment/
    ├── humanoid/abyssalite.png
    ├── humanoid_baby/abyssalite.png
    └── humanoid_leggings/abyssalite.png
```

The block-item models reuse `block/abyssalite_block.png` and `block/atlantis_debris.png`; separate item PNGs for those blocks are not required. The exact `abyssalite_trident.png` filename is the source of truth for the trident item ID.
```

### Palm

The palm wood set needs these on top of the block textures already in the repository:

```text
common/src/main/resources/assets/suprememc/textures/item/palm_door.png
common/src/main/resources/assets/minecraft/textures/gui/signs/palm.png
common/src/main/resources/assets/minecraft/textures/gui/hanging_signs/palm.png
```

`item/palm_door.png` is the flat inventory icon for the door and currently holds a placeholder copied from
`block/palm_door_top.png`. The two GUI files back the sign editing screen; Minecraft resolves that texture as
`minecraft:textures/gui/signs/<wood type name>.png` with a hardcoded namespace, so they must live under the
`minecraft` namespace rather than `suprememc`. Both are now painted with the finished palm artwork.

Use PNG files with transparent backgrounds where appropriate. A 16x16 pixel base texture is recommended; larger textures must use a multiple of 16 for correct pixel scaling.

### Cotton

Cotton uses these shared textures:

```text
common/src/main/resources/assets/suprememc/textures/item/cotton.png
common/src/main/resources/assets/suprememc/textures/block/cotton_bush_stage0.png
common/src/main/resources/assets/suprememc/textures/block/cotton_bush_stage1.png
common/src/main/resources/assets/suprememc/textures/block/cotton_bush_stage2.png
common/src/main/resources/assets/suprememc/textures/block/cotton_bush_stage3.png
```

The three block textures are used by their generated block models. Wet farmland inherits Minecraft's moist farmland model and texture, so it does not need `wet_farmland.png`. The ore and block inventory items reuse their respective block textures, so they do not need separate item PNGs. The armour inventory icons use the ten item textures above.

## Equipped Armour Appearance

## Calamari

Supply these shared item textures for the Calamari pipeline:

```text
common/src/main/resources/assets/suprememc/textures/item/calamari.png
common/src/main/resources/assets/suprememc/textures/item/cooked_calamari.png
```

## Fruit and Vegetable Food

Supply these shared item textures for the food items:

```text
common/src/main/resources/assets/suprememc/textures/item/grapes.png
common/src/main/resources/assets/suprememc/textures/item/tomato.png
common/src/main/resources/assets/suprememc/textures/item/corn.png
common/src/main/resources/assets/suprememc/textures/block/corn_stalk.png
common/src/main/resources/assets/suprememc/textures/block/corn_stalk_plant.png
common/src/main/resources/assets/suprememc/textures/block/grape_vine.png
common/src/main/resources/assets/suprememc/textures/block/grape_vine_plant.png
```

Aquamarine armour uses a SupremeMC equipment asset when worn. Supply these three PNGs to render its custom appearance: `entity/equipment/humanoid/aquamarine.png`, `entity/equipment/humanoid_baby/aquamarine.png`, and `entity/equipment/humanoid_leggings/aquamarine.png`. The first two use the humanoid armour layer layout; the leggings PNG uses the leggings layer layout.

Generated JSON resources remain in `neoforge/src/generated/resources` and are included by both loaders. Textures belong in `common/src/main/resources`, so they are natively shared by Fabric and NeoForge rather than copied between generated directories.

## Fire Creeper

The Fire Creeper entity and its spawn egg use these shared textures (already supplied):

```text
common/src/main/resources/assets/suprememc/textures/entity/fire_creeper/fire_creeper.png
common/src/main/resources/assets/suprememc/textures/entity/fire_creeper/fire_creeper_armor.png
common/src/main/resources/assets/suprememc/textures/item/fire_creeper_spawn_egg.png
```

`fire_creeper.png` follows the vanilla creeper texture layout, and `fire_creeper_armor.png` is the charged energy-swirl overlay shown when the Fire Creeper is struck by lightning (same layout as vanilla's `creeper_armor.png`).

## Snow Creeper

The Snow Creeper entity and its spawn egg use these shared textures (already supplied):

```text
common/src/main/resources/assets/suprememc/textures/entity/snow_creeper/snow_creeper.png
common/src/main/resources/assets/suprememc/textures/entity/snow_creeper/snow_creeper_armor.png
common/src/main/resources/assets/suprememc/textures/item/snow_creeper_spawn_egg.png
```

`snow_creeper.png` follows the vanilla creeper texture layout, and `snow_creeper_armor.png` is the charged energy-swirl overlay shown when the Snow Creeper is struck by lightning (same layout as vanilla's `creeper_armor.png`).
