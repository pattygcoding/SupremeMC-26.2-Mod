package com.suprememc.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Mob.class)
public abstract class MixinMobEquipment {
    private static final float VANILLA_EQUIPMENT_DROP_CHANCE = 0.085F;
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void suprememc$populateEquipment(RandomSource random, DifficultyInstance difficulty, CallbackInfo callback) {
        Mob mob = (Mob) (Object) this;
        if (!isEligible(mob)) {
            return;
        }

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            mob.setItemSlot(slot, ItemStack.EMPTY);
        }
        Difficulty level = difficulty.getDifficulty();
        if (random.nextFloat() < armorChance(level)) {
            Item[] armor = armorSet(selectTier(level, random), random);
            EquipmentSlot[] slots = selectedSlots(level, random);
            for (int index = 0; index < slots.length; index++) {
                ItemStack stack = new ItemStack(armor[slotIndex(slots[index])]);
                damageForDropProtection(stack, random);
                mob.setItemSlot(slots[index], stack);
                mob.setDropChance(slots[index], VANILLA_EQUIPMENT_DROP_CHANCE);
            }
        }

        if (swordEligible(mob) && random.nextFloat() < swordChance(level)) {
            ItemStack stack = new ItemStack(swordSet(selectTier(level, random)));
            damageForDropProtection(stack, random);
            mob.setItemSlot(EquipmentSlot.MAINHAND, stack);
            mob.setDropChance(EquipmentSlot.MAINHAND, VANILLA_EQUIPMENT_DROP_CHANCE);
        }
    }

    @Inject(method = "populateDefaultEquipmentEnchantments", at = @At("HEAD"), cancellable = true)
    private void suprememc$populateEnchantments(
            ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty, CallbackInfo callback) {
        Mob mob = (Mob) (Object) this;
        if (!isEligible(mob)) {
            return;
        }

        callback.cancel();
        Difficulty currentDifficulty = difficulty.getDifficulty();
        float chance = enchantmentChance(currentDifficulty);
        int minimumLevel = minimumEnchantmentLevel(currentDifficulty);
        int maximumLevel = maximumEnchantmentLevel(currentDifficulty);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = mob.getItemBySlot(slot);
            if (stack.isEmpty() || random.nextFloat() >= chance) {
                continue;
            }

            int enchantmentLevel = minimumLevel + random.nextInt(maximumLevel - minimumLevel + 1);
            ItemStack enchanted = EnchantmentHelper.enchantItem(
                    random, stack, enchantmentLevel, level.registryAccess(), Optional.empty());
            mob.setItemSlot(slot, enchanted);
        }
    }

    private static boolean isEligible(Mob mob) {
        return mob instanceof Zombie || mob instanceof AbstractSkeleton || mob instanceof Piglin;
    }

    private static boolean swordEligible(Mob mob) {
        return mob instanceof Zombie || mob instanceof WitherSkeleton;
    }

    private static float armorChance(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 0.25F;
            case NORMAL -> 0.55F;
            case HARD -> 0.85F;
            default -> 0.0F;
        };
    }

    private static float swordChance(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 0.50F;
            case NORMAL -> 0.75F;
            case HARD -> 0.90F;
            default -> 0.0F;
        };
    }

    private static float enchantmentChance(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 0.20F;
            case NORMAL -> 0.50F;
            case HARD -> 0.80F;
            default -> 0.0F;
        };
    }

    private static int minimumEnchantmentLevel(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 8;
            case NORMAL -> 15;
            case HARD -> 25;
            default -> 0;
        };
    }

    private static int maximumEnchantmentLevel(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 15;
            case NORMAL -> 25;
            case HARD -> 35;
            default -> 0;
        };
    }

    private static int selectTier(Difficulty difficulty, RandomSource random) {
        int roll = random.nextInt(100);
        int tier = switch (difficulty) {
            case EASY -> roll < 60 ? 0 : roll < 85 ? 1 : roll < 95 ? 2 : roll < 99 ? 3 : 4;
            case NORMAL -> roll < 25 ? 0 : roll < 45 ? 1 : roll < 70 ? 2 : roll < 96 ? 3 : 4;
            case HARD -> roll < 5 ? 0 : roll < 20 ? 1 : roll < 45 ? 2 : roll < 90 ? 3 : 4;
            default -> 0;
        };
        return difficulty == Difficulty.HARD && tier == 4 && random.nextFloat() < 0.05F ? 5 : tier;
    }

    private static Item[] armorSet(int tier, RandomSource random) {
        if (tier == 0) {
            return new Item[]{Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS};
        }
        if (tier == 1) {
            return new Item[]{Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS};
        }
        if (tier == 2) {
            return new Item[]{Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS};
        }
        if (tier == 3) {
            return new Item[]{Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS};
        }
        if (tier == 4) {
            return new Item[]{Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS};
        }
        return new Item[]{Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS};
    }

    private static Item swordSet(int tier) {
        if (tier == 0) {
            return Items.WOODEN_SWORD;
        }
        if (tier == 1) {
            return Items.GOLDEN_SWORD;
        }
        if (tier == 2) {
            return Items.STONE_SWORD;
        }
        if (tier == 3) {
            return Items.IRON_SWORD;
        }
        if (tier == 4) {
            return Items.DIAMOND_SWORD;
        }
        return Items.NETHERITE_SWORD;
    }

    private static EquipmentSlot[] selectedSlots(Difficulty difficulty, RandomSource random) {
        int count = switch (difficulty) {
            case EASY -> 1 + random.nextInt(2);
            case NORMAL -> random.nextFloat() < 0.40F ? 4 : 2 + random.nextInt(2);
            case HARD -> random.nextFloat() < 0.80F ? 4 : 3;
            default -> 0;
        };
        for (int index = ARMOR_SLOTS.length - 1; index > 0; index--) {
            int swap = random.nextInt(index + 1);
            EquipmentSlot slot = ARMOR_SLOTS[index];
            ARMOR_SLOTS[index] = ARMOR_SLOTS[swap];
            ARMOR_SLOTS[swap] = slot;
        }
        EquipmentSlot[] result = new EquipmentSlot[count];
        System.arraycopy(ARMOR_SLOTS, 0, result, 0, count);
        return result;
    }

    private static int slotIndex(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> throw new IllegalArgumentException("Not an armor slot: " + slot);
        };
    }

    private static void damageForDropProtection(ItemStack stack, RandomSource random) {
        int maxDamage = stack.getMaxDamage();
        if (maxDamage > 0) {
            stack.setDamageValue(Math.max(1, (int) (maxDamage * (0.15F + random.nextFloat() * 0.25F))));
        }
    }
}