package com.suprememc.content.init;

import com.suprememc.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public final class ModPotions {
	public static Holder<Potion> LUCK_POTION;
	public static Holder<Potion> LONG_LUCK_POTION;
	public static Holder<Potion> STRONG_LUCK_POTION;
	public static Holder<Potion> BAD_LUCK_POTION;
	public static Holder<Potion> LONG_BAD_LUCK_POTION;
	public static Holder<Potion> STRONG_BAD_LUCK_POTION;
	public static Holder<Potion> HUNGER_POTION;
	public static Holder<Potion> LONG_HUNGER_POTION;
	public static Holder<Potion> STRONG_HUNGER_POTION;
	public static Holder<Potion> DECAY_POTION;
	public static Holder<Potion> LONG_DECAY_POTION;
	public static Holder<Potion> STRONG_DECAY_POTION;
	public static Holder<Potion> ADVANCEMENT_COCKTAIL;

	private static boolean registered;

	public static void bootstrap() {
		if (registered) {
			return;
		}
		registered = true;

		LUCK_POTION = registerPotion("luck", 6000, 0, MobEffects.LUCK);
		LONG_LUCK_POTION = registerPotion("long_luck", 9600, 0, MobEffects.LUCK);
		STRONG_LUCK_POTION = registerPotion("strong_luck", 1800, 1, MobEffects.LUCK);
		BAD_LUCK_POTION = registerPotion("bad_luck", 6000, 0, MobEffects.UNLUCK);
		LONG_BAD_LUCK_POTION = registerPotion("long_bad_luck", 9600, 0, MobEffects.UNLUCK);
		STRONG_BAD_LUCK_POTION = registerPotion("strong_bad_luck", 1800, 1, MobEffects.UNLUCK);
		HUNGER_POTION = registerPotion("hunger", 2700, 0, MobEffects.HUNGER);
		LONG_HUNGER_POTION = registerPotion("long_hunger", 5400, 0, MobEffects.HUNGER);
		STRONG_HUNGER_POTION = registerPotion("strong_hunger", 1200, 1, MobEffects.HUNGER);
		DECAY_POTION = registerPotion("decay", 2400, 0, MobEffects.WITHER);
		LONG_DECAY_POTION = registerPotion("long_decay", 3600, 0, MobEffects.WITHER);
		STRONG_DECAY_POTION = registerPotion("strong_decay", 1200, 1, MobEffects.WITHER);

		ADVANCEMENT_COCKTAIL = registerAdvancementCocktail();
	}

	// Every single vanilla mob effect, applied for exactly 1 tick.
	private static Holder<Potion> registerAdvancementCocktail() {
		MobEffectInstance[] effects = java.util.stream.Stream.of(
			MobEffects.SPEED, MobEffects.SLOWNESS, MobEffects.HASTE, MobEffects.MINING_FATIGUE,
			MobEffects.STRENGTH, MobEffects.INSTANT_HEALTH, MobEffects.INSTANT_DAMAGE, MobEffects.JUMP_BOOST,
			MobEffects.NAUSEA, MobEffects.REGENERATION, MobEffects.RESISTANCE, MobEffects.FIRE_RESISTANCE,
			MobEffects.WATER_BREATHING, MobEffects.INVISIBILITY, MobEffects.BLINDNESS, MobEffects.NIGHT_VISION,
			MobEffects.HUNGER, MobEffects.WEAKNESS, MobEffects.POISON, MobEffects.WITHER,
			MobEffects.HEALTH_BOOST, MobEffects.ABSORPTION, MobEffects.SATURATION, MobEffects.GLOWING,
			MobEffects.LEVITATION, MobEffects.LUCK, MobEffects.UNLUCK, MobEffects.SLOW_FALLING,
			MobEffects.CONDUIT_POWER, MobEffects.DOLPHINS_GRACE, MobEffects.BAD_OMEN, MobEffects.HERO_OF_THE_VILLAGE,
			MobEffects.DARKNESS, MobEffects.TRIAL_OMEN, MobEffects.RAID_OMEN, MobEffects.WIND_CHARGED,
			MobEffects.WEAVING, MobEffects.OOZING, MobEffects.INFESTED, MobEffects.BREATH_OF_THE_NAUTILUS
		).map(effect -> new MobEffectInstance(effect, 1, 0)).toArray(MobEffectInstance[]::new);

		return Registry.registerForHolder(
			BuiltInRegistries.POTION,
			Identifier.fromNamespaceAndPath(Constants.MOD_ID, "advancement_cocktail"),
			new Potion("advancement_cocktail", effects)
		);
	}

	private static Holder<Potion> registerPotion(String id, int duration, int amplifier,
												   Holder<net.minecraft.world.effect.MobEffect> effect) {
		return Registry.registerForHolder(
			BuiltInRegistries.POTION,
			Identifier.fromNamespaceAndPath(Constants.MOD_ID, id),
			new Potion(id, new MobEffectInstance(effect, duration, amplifier))
		);
	}
}
