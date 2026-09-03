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

Use PNG files with transparent backgrounds where appropriate. A 16x16 pixel base texture is recommended; larger textures must use a multiple of 16 for correct pixel scaling.

The three block textures are used by their generated block models. Wet farmland inherits Minecraft's moist farmland model and texture, so it does not need `wet_farmland.png`. The ore and block inventory items reuse their respective block textures, so they do not need separate item PNGs. The armour inventory icons use the ten item textures above.

## Equipped Armour Appearance

Aquamarine armour uses a SupremeMC equipment asset when worn. Supply these three PNGs to render its custom appearance: `entity/equipment/humanoid/aquamarine.png`, `entity/equipment/humanoid_baby/aquamarine.png`, and `entity/equipment/humanoid_leggings/aquamarine.png`. The first two use the humanoid armour layer layout; the leggings PNG uses the leggings layer layout.

Generated JSON resources remain in `neoforge/src/generated/resources` and are included by both loaders. Textures belong in `common/src/main/resources`, so they are natively shared by Fabric and NeoForge rather than copied between generated directories.
