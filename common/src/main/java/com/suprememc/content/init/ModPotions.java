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
