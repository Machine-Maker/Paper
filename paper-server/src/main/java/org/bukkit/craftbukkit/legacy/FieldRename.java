package org.bukkit.craftbukkit.legacy;

import io.papermc.paper.plugin.ApiVersion;
import java.util.function.BiFunction;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.legacy.fieldrename.FieldRenameData;
import org.bukkit.craftbukkit.legacy.reroute.DoNotReroute;
import org.bukkit.craftbukkit.util.ApiVersion;

public class FieldRename {

    // Enchantment
    private static final FieldRenameData ENCHANTMENT_DATA = FieldRenameData.Builder.newBuilder()
            .forAllVersions()
            .change("PROTECTION_ENVIRONMENTAL", "PROTECTION")
            .change("PROTECTION_FIRE", "FIRE_PROTECTION")
            .change("PROTECTION_FALL", "FEATHER_FALLING")
            .change("PROTECTION_EXPLOSIONS", "BLAST_PROTECTION")
            .change("PROTECTION_PROJECTILE", "PROJECTILE_PROTECTION")
            .change("OXYGEN", "RESPIRATION")
            .change("WATER_WORKER", "AQUA_AFFINITY")
            .change("DAMAGE_ALL", "SHARPNESS")
            .change("DAMAGE_UNDEAD", "SMITE")
            .change("DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPODS")
            .change("LOOT_BONUS_MOBS", "LOOTING")
            .change("DIG_SPEED", "EFFICIENCY")
            .change("DURABILITY", "UNBREAKING")
            .change("LOOT_BONUS_BLOCKS", "FORTUNE")
            .change("ARROW_DAMAGE", "POWER")
            .change("ARROW_KNOCKBACK", "PUNCH")
            .change("ARROW_FIRE", "FLAME")
            .change("ARROW_INFINITE", "INFINITY")
            .change("LUCK", "LUCK_OF_THE_SEA")
            .withKeyRename()
            .change("SWEEPING", "SWEEPING_EDGE")
            .build();

    public static final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> ENCHANTMENT_RENAME = ENCHANTMENT_DATA::getReplacement;

    @DoNotReroute
    public static String convertEnchantmentName(ApiVersion version, String from) {
        return FieldRename.ENCHANTMENT_DATA.getReplacement(version, from);
    }

    // EntityType
    private static final FieldRenameData ENTITY_TYPE_DATA = FieldRenameData.Builder.newBuilder()
            .forAllVersions()
            .change("PIG_ZOMBIE", "ZOMBIFIED_PIGLIN")
            .change("DROPPED_ITEM", "ITEM")
            .change("LEASH_HITCH", "LEASH_KNOT")
            .change("ENDER_SIGNAL", "EYE_OF_ENDER")
            .change("POTION", "SPLASH_POTION")
            .change("THROWN_EXP_BOTTLE", "EXPERIENCE_BOTTLE")
            .change("PRIMED_TNT", "TNT")
            .change("FIREWORK", "FIREWORK_ROCKET")
            .change("MINECART_COMMAND", "COMMAND_BLOCK_MINECART")
            .change("MINECART_CHEST", "CHEST_MINECART")
            .change("MINECART_FURNACE", "FURNACE_MINECART")
            .change("MINECART_TNT", "TNT_MINECART")
            .change("MINECART_HOPPER", "HOPPER_MINECART")
            .change("MINECART_MOB_SPAWNER", "SPAWNER_MINECART")
            .change("MUSHROOM_COW", "MOOSHROOM")
            .change("SNOWMAN", "SNOW_GOLEM")
            .change("FISHING_HOOK", "FISHING_BOBBER")
            .change("LIGHTNING", "LIGHTNING_BOLT")
            .withKeyRename()
            .change("XP_ORB", "EXPERIENCE_ORB")
            .change("EYE_OF_ENDER_SIGNAL", "EYE_OF_ENDER")
            .change("XP_BOTTLE", "EXPERIENCE_BOTTLE")
            .change("FIREWORKS_ROCKET", "FIREWORK_ROCKET")
            .change("EVOCATION_FANGS", "EVOKER_FANGS")
            .change("EVOCATION_ILLAGER", "EVOKER")
            .change("VINDICATION_ILLAGER", "VINDICATOR")
            .change("ILLUSION_ILLAGER", "ILLUSIONER")
            .change("COMMANDBLOCK_MINECART", "COMMAND_BLOCK_MINECART")
            .change("SNOWMAN", "SNOW_GOLEM")
            .change("VILLAGER_GOLEM", "IRON_GOLEM")
            .change("ENDER_CRYSTAL", "END_CRYSTAL")
            .change("ZOMBIE_PIGMAN", "ZOMBIFIED_PIGLIN")
            .change("BOAT", "OAK_BOAT")
            .change("CHEST_BOAT", "OAK_CHEST_BOAT")
            .build();

    public static final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> ENTITY_TYPE_RENAME = ENTITY_TYPE_DATA::getReplacement;

    @DoNotReroute
    public static String convertEntityTypeName(ApiVersion version, String from) {
        return FieldRename.ENTITY_TYPE_DATA.getReplacement(version, from);
    }

    // PotionEffectType
    private static final FieldRenameData POTION_EFFECT_TYPE_DATA = FieldRenameData.Builder.newBuilder()
            .forAllVersions()
            .change("SLOW", "SLOWNESS")
            .change("FAST_DIGGING", "HASTE")
            .change("SLOW_DIGGING", "MINING_FATIGUE")
            .change("INCREASE_DAMAGE", "STRENGTH")
            .change("HEAL", "INSTANT_HEALTH")
            .change("HARM", "INSTANT_DAMAGE")
            .change("JUMP", "JUMP_BOOST")
            .change("CONFUSION", "NAUSEA")
            .change("DAMAGE_RESISTANCE", "RESISTANCE")
            .build();

    @DoNotReroute
    public static String convertPotionEffectTypeName(ApiVersion version, String from) {
        return FieldRename.POTION_EFFECT_TYPE_DATA.getReplacement(version, from);
    }

    // PotionType
    private static final FieldRenameData POTION_TYPE_DATA = FieldRenameData.Builder.newBuilder()
            .forAllVersions()
            .change("JUMP", "LEAPING")
            .change("SPEED", "SWIFTNESS")
            .change("INSTANT_HEAL", "HEALING")
            .change("INSTANT_DAMAGE", "HARMING")
            .change("REGEN", "REGENERATION")
            .build();

    @DoNotReroute
    public static String convertPotionTypeName(ApiVersion version, String from) {
        return FieldRename.POTION_TYPE_DATA.getReplacement(version, from);
    }

    // Particle
    private static final FieldRenameData PARTICLE_DATA = FieldRenameData.Builder.newBuilder()
            .forAllVersions()
            .change("EXPLOSION_NORMAL", "POOF")
            .change("EXPLOSION_LARGE", "EXPLOSION")
            .change("EXPLOSION_HUGE", "EXPLOSION_EMITTER")
            .change("FIREWORKS_SPARK", "FIREWORK")
            .change("WATER_BUBBLE", "BUBBLE")
            .change("WATER_SPLASH", "SPLASH")
            .change("WATER_WAKE", "FISHING")
            .change("SUSPENDED", "UNDERWATER")
            .change("SUSPENDED_DEPTH", "UNDERWATER")
            .change("CRIT_MAGIC", "ENCHANTED_HIT")
            .change("SMOKE_NORMAL", "SMOKE")
            .change("SMOKE_LARGE", "LARGE_SMOKE")
            .change("SPELL", "EFFECT")
            .change("SPELL_INSTANT", "INSTANT_EFFECT")
            .change("SPELL_MOB", "ENTITY_EFFECT")
            .change("SPELL_WITCH", "WITCH")
            .change("DRIP_WATER", "DRIPPING_WATER")
            .change("DRIP_LAVA", "DRIPPING_LAVA")
            .change("VILLAGER_ANGRY", "ANGRY_VILLAGER")
            .change("VILLAGER_HAPPY", "HAPPY_VILLAGER")
            .change("TOWN_AURA", "MYCELIUM")
            .change("ENCHANTMENT_TABLE", "ENCHANT")
            .change("REDSTONE", "DUST")
            .change("SNOWBALL", "ITEM_SNOWBALL")
            .change("SNOW_SHOVEL", "ITEM_SNOWBALL")
            .change("SLIME", "ITEM_SLIME")
            .change("ITEM_CRACK", "ITEM")
            .change("BLOCK_CRACK", "BLOCK")
            .change("BLOCK_DUST", "BLOCK")
            .change("WATER_DROP", "RAIN")
            .change("MOB_APPEARANCE", "ELDER_GUARDIAN")
            .change("TOTEM", "TOTEM_OF_UNDYING")
            .withKeyRename()
            .change("GUST_EMITTER", "GUST_EMITTER_LARGE")
            .build();

    public static final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> PARTICLE_TYPE_RENAME = PARTICLE_DATA::getReplacement;

    // Attribute
    private static final FieldRenameData ATTRIBUTE_DATA = FieldRenameData.Builder.newBuilder()
            .forAllVersions()
            .withKeyRename()
            .change("HORSE.JUMP_STRENGTH", "JUMP_STRENGTH")
            .change("GENERIC.MAX_HEALTH", "MAX_HEALTH")
            .change("GENERIC.FOLLOW_RANGE", "FOLLOW_RANGE")
            .change("GENERIC.KNOCKBACK_RESISTANCE", "KNOCKBACK_RESISTANCE")
            .change("GENERIC.MOVEMENT_SPEED", "MOVEMENT_SPEED")
            .change("GENERIC.FLYING_SPEED", "FLYING_SPEED")
            .change("GENERIC.ATTACK_DAMAGE", "ATTACK_DAMAGE")
            .change("GENERIC.ATTACK_KNOCKBACK", "ATTACK_KNOCKBACK")
            .change("GENERIC.ATTACK_SPEED", "ATTACK_SPEED")
            .change("GENERIC.ARMOR", "ARMOR")
            .change("GENERIC.ARMOR_TOUGHNESS", "ARMOR_TOUGHNESS")
            .change("GENERIC.FALL_DAMAGE_MULTIPLIER", "FALL_DAMAGE_MULTIPLIER")
            .change("GENERIC.LUCK", "LUCK")
            .change("GENERIC.MAX_ABSORPTION", "MAX_ABSORPTION")
            .change("GENERIC.SAFE_FALL_DISTANCE", "SAFE_FALL_DISTANCE")
            .change("GENERIC.SCALE", "SCALE")
            .change("GENERIC.STEP_HEIGHT", "STEP_HEIGHT")
            .change("GENERIC.GRAVITY", "GRAVITY")
            .change("GENERIC.JUMP_STRENGTH", "JUMP_STRENGTH")
            .change("GENERIC.BURNING_TIME", "BURNING_TIME")
            .change("GENERIC.EXPLOSION_KNOCKBACK_RESISTANCE", "EXPLOSION_KNOCKBACK_RESISTANCE")
            .change("GENERIC.MOVEMENT_EFFICIENCY", "MOVEMENT_EFFICIENCY")
            .change("GENERIC.OXYGEN_BONUS", "OXYGEN_BONUS")
            .change("GENERIC.WATER_MOVEMENT_EFFICIENCY", "WATER_MOVEMENT_EFFICIENCY")
            .change("GENERIC.TEMPT_RANGE", "TEMPT_RANGE")
            .change("PLAYER.BLOCK_INTERACTION_RANGE", "BLOCK_INTERACTION_RANGE")
            .change("PLAYER.ENTITY_INTERACTION_RANGE", "ENTITY_INTERACTION_RANGE")
            .change("PLAYER.BLOCK_BREAK_SPEED", "BLOCK_BREAK_SPEED")
            .change("PLAYER.MINING_EFFICIENCY", "MINING_EFFICIENCY")
            .change("PLAYER.SNEAKING_SPEED", "SNEAKING_SPEED")
            .change("PLAYER.SUBMERGED_MINING_SPEED", "SUBMERGED_MINING_SPEED")
            .change("PLAYER.SWEEPING_DAMAGE_RATIO", "SWEEPING_DAMAGE_RATIO")
            .change("ZOMBIE.SPAWN_REINFORCEMENTS", "SPAWN_REINFORCEMENTS")
            .build();

    public static final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> ATTRIBUTE_RENAME = ATTRIBUTE_DATA::getReplacement;

    @DoNotReroute
    public static String convertAttributeName(ApiVersion version, String from) {
        return FieldRename.ATTRIBUTE_DATA.getReplacement(version, from);
    }

    // ItemFlag
    private static final FieldRenameData ITEM_FLAG_DATA = FieldRenameData.Builder.newBuilder()
            .forAllVersions()
            .change("HIDE_POTION_EFFECTS", "HIDE_ADDITIONAL_TOOLTIP")
            .change("HIDE_ITEM_SPECIFICS", "HIDE_ADDITIONAL_TOOLTIP")
            .build();

    @DoNotReroute
    public static String convertItemFlagName(ApiVersion version, String from) {
        return FieldRename.ITEM_FLAG_DATA.getReplacement(version, from);
    }
}
