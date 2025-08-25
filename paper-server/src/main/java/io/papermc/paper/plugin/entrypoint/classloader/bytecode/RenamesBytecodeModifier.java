package io.papermc.paper.plugin.entrypoint.classloader.bytecode;

import com.google.common.collect.ImmutableMap;
import io.papermc.asm.rules.builder.matcher.method.MethodType;
import io.papermc.asm.rules.method.DirectStaticRewrite;
import io.papermc.asm.rules.rename.EnumRenameBuilder;
import io.papermc.asm.rules.rename.RenameRule;
import io.papermc.asm.versioned.CachingVersionedRuleFactory;
import io.papermc.asm.versioned.MergingVersionRuleFactory;
import io.papermc.asm.versioned.Version;
import io.papermc.asm.versioned.VersionedRuleFactory;
import io.papermc.asm.versioned.VersionedRuleScanner;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Art;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockType;
import org.bukkit.block.Orientation;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.data.type.Wall;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractCow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemType;
import org.bukkit.loot.LootTables;
import org.bukkit.map.MapCursor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.jspecify.annotations.Nullable;

public final class RenamesBytecodeModifier extends CachingVersionedRuleFactory {

    private static final List<String> SLOT_COLORS = List.of("BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW", "WHITE");

    //<editor-fold desc="enchantments" defaultstate="collapsed">
    private static final Map<String, String> FIELD_PARITY_ENCHANTMENTS = ImmutableMap.<String, String>builder()
        .put("PROTECTION_ENVIRONMENTAL", "PROTECTION")
        .put("PROTECTION_FIRE", "FIRE_PROTECTION")
        .put("PROTECTION_FALL", "FEATHER_FALLING")
        .put("PROTECTION_EXPLOSIONS", "BLAST_PROTECTION")
        .put("PROTECTION_PROJECTILE", "PROJECTILE_PROTECTION")
        .put("OXYGEN", "RESPIRATION")
        .put("WATER_WORKER", "AQUA_AFFINITY")
        .put("DAMAGE_ALL", "SHARPNESS")
        .put("DAMAGE_UNDEAD", "SMITE")
        .put("DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPODS")
        .put("LOOT_BONUS_MOBS", "LOOTING")
        .put("DIG_SPEED", "EFFICIENCY")
        .put("DURABILITY", "UNBREAKING")
        .put("LOOT_BONUS_BLOCKS", "FORTUNE")
        .put("ARROW_DAMAGE", "POWER")
        .put("ARROW_KNOCKBACK", "PUNCH")
        .put("ARROW_FIRE", "FLAME")
        .put("ARROW_INFINITE", "INFINITY")
        .put("LUCK", "LUCK_OF_THE_SEA")
        // skip sweeping from commodore, just for key, the field was always correct
        .build();
    //</editor-fold>

    //<editor-fold desc="entity types" defaultstate="collapsed">
    private static final Map<String, String> FIELD_PARITY_ENTITY_TYPES = ImmutableMap.<String, String>builder()
        .put("DROPPED_ITEM", "ITEM")
        .put("LEASH_HITCH", "LEASH_KNOT")
        .put("ENDER_SIGNAL", "EYE_OF_ENDER")
        .put("SPLASH_POTION", "POTION")
        .put("THROWN_EXP_BOTTLE", "EXPERIENCE_BOTTLE")
        .put("PRIMED_TNT", "TNT")
        .put("FIREWORK", "FIREWORK_ROCKET")
        .put("MINECART_COMMAND", "COMMAND_BLOCK_MINECART")
        .put("MINECART_CHEST", "CHEST_MINECART")
        .put("MINECART_FURNACE", "FURNACE_MINECART")
        .put("MINECART_TNT", "TNT_MINECART")
        .put("MINECART_HOPPER", "HOPPER_MINECART")
        .put("MINECART_MOB_SPAWNER", "SPAWNER_MINECART")
        .put("MUSHROOM_COW", "MOOSHROOM")
        .put("SNOWMAN", "SNOW_GOLEM")
        .put("ENDER_CRYSTAL", "END_CRYSTAL")
        .put("FISHING_HOOK", "FISHING_BOBBER")
        .put("LIGHTNING", "LIGHTNING_BOLT")
        .build();
    //</editor-fold>

    //<editor-fold desc="potion effect types" defaultstate="collapsed">
    private static final Map<String, String> FIELD_PARITY_POTION_EFFECT_TYPES = ImmutableMap.<String, String>builder()
        .put("SLOW", "SLOWNESS")
        .put("FAST_DIGGING", "HASTE")
        .put("SLOW_DIGGING", "MINING_FATIGUE")
        .put("INCREASE_DAMAGE", "STRENGTH")
        .put("HEAL", "INSTANT_HEALTH")
        .put("HARM", "INSTANT_DAMAGE")
        .put("JUMP", "JUMP_BOOST")
        .put("CONFUSION", "NAUSEA")
        .put("DAMAGE_RESISTANCE", "RESISTANCE")
        .build();
    //</editor-fold>

    private static final Map<String, String> FIELD_PARITY_POTION_TYPES = Map.of(
        "JUMP", "LEAPING",
        "SPEED", "SWIFTNESS",
        "INSTANT_HEAL", "HEALING",
        "INSTANT_DAMAGE", "HARMING",
        "REGEN", "REGENERATION"
    );

    private static final Map<String, String> FIELD_PARITY_MUSIC_INSTRUMENTS = ImmutableMap.<String, String>builder()
        .put("PONDER", "PONDER_GOAT_HORN")
        .put("SING", "SING_GOAT_HORN")
        .put("SEEK", "SEEK_GOAT_HORN")
        .put("FEEL", "FEEL_GOAT_HORN")
        .put("ADMIRE", "ADMIRE_GOAT_HORN")
        .put("CALL", "CALL_GOAT_HORN")
        .put("YEARN", "YEARN_GOAT_HORN")
        .put("DREAM", "DREAM_GOAT_HORN")
        .build();

    //<editor-fold desc="particles" defaultstate="collapsed">
    private static final Map<String, String> FIELD_PARITY_PARTICLES = ImmutableMap.<String, String>builder()
        .put("EXPLOSION_NORMAL", "POOF")
        .put("EXPLOSION_LARGE", "EXPLOSION")
        .put("EXPLOSION_HUGE", "EXPLOSION_EMITTER")
        .put("FIREWORKS_SPARK", "FIREWORK")
        .put("WATER_BUBBLE", "BUBBLE")
        .put("WATER_SPLASH", "SPLASH")
        .put("WATER_WAKE", "FISHING")
        .put("SUSPENDED", "UNDERWATER")
        .put("SUSPENDED_DEPTH", "UNDERWATER")
        .put("CRIT_MAGIC", "ENCHANTED_HIT")
        .put("SMOKE_NORMAL", "SMOKE")
        .put("SMOKE_LARGE", "LARGE_SMOKE")
        .put("SPELL", "EFFECT")
        .put("SPELL_INSTANT", "INSTANT_EFFECT")
        .put("SPELL_MOB", "ENTITY_EFFECT")
        .put("SPELL_WITCH", "WITCH")
        .put("DRIP_WATER", "DRIPPING_WATER")
        .put("DRIP_LAVA", "DRIPPING_LAVA")
        .put("VILLAGER_ANGRY", "ANGRY_VILLAGER")
        .put("VILLAGER_HAPPY", "HAPPY_VILLAGER")
        .put("TOWN_AURA", "MYCELIUM")
        .put("ENCHANTMENT_TABLE", "ENCHANT")
        .put("REDSTONE", "DUST")
        .put("SNOWBALL", "ITEM_SNOWBALL")
        .put("SNOW_SHOVEL", "ITEM_SNOWBALL")
        .put("SLIME", "ITEM_SLIME")
        .put("ITEM_CRACK", "ITEM")
        .put("BLOCK_CRACK", "BLOCK")
        .put("BLOCK_DUST", "BLOCK")
        .put("WATER_DROP", "RAIN")
        .put("MOB_APPEARANCE", "ELDER_GUARDIAN")
        .put("TOTEM", "TOTEM_OF_UNDYING")
        .put("GUST_EMITTER", "GUST_EMITTER_LARGE")
        .build();
    //</editor-fold>

    //<editor-fold desc="attributes" defaultstate="collapsed">
    private static final Map<String, String> FIELD_PARITY_ATTRIBUTES = ImmutableMap.<String, String>builder()
        .put("GENERIC_MAX_HEALTH", "MAX_HEALTH")
        .put("GENERIC_FOLLOW_RANGE", "FOLLOW_RANGE")
        .put("GENERIC_KNOCKBACK_RESISTANCE", "KNOCKBACK_RESISTANCE")
        .put("GENERIC_MOVEMENT_SPEED", "MOVEMENT_SPEED")
        .put("GENERIC_FLYING_SPEED", "FLYING_SPEED")
        .put("GENERIC_ATTACK_DAMAGE", "ATTACK_DAMAGE")
        .put("GENERIC_ATTACK_KNOCKBACK", "ATTACK_KNOCKBACK")
        .put("GENERIC_ATTACK_SPEED", "ATTACK_SPEED")
        .put("GENERIC_ARMOR", "ARMOR")
        .put("GENERIC_ARMOR_TOUGHNESS", "ARMOR_TOUGHNESS")
        .put("GENERIC_FALL_DAMAGE_MULTIPLIER", "FALL_DAMAGE_MULTIPLIER")
        .put("GENERIC_LUCK", "LUCK")
        .put("GENERIC_MAX_ABSORPTION", "MAX_ABSORPTION")
        .put("GENERIC_SAFE_FALL_DISTANCE", "SAFE_FALL_DISTANCE")
        .put("GENERIC_SCALE", "SCALE")
        .put("GENERIC_STEP_HEIGHT", "STEP_HEIGHT")
        .put("GENERIC_GRAVITY", "GRAVITY")
        .put("GENERIC_JUMP_STRENGTH", "JUMP_STRENGTH")
        .put("GENERIC_BURNING_TIME", "BURNING_TIME")
        .put("GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE", "EXPLOSION_KNOCKBACK_RESISTANCE")
        .put("GENERIC_MOVEMENT_EFFICIENCY", "MOVEMENT_EFFICIENCY")
        .put("GENERIC_OXYGEN_BONUS", "OXYGEN_BONUS")
        .put("GENERIC_WATER_MOVEMENT_EFFICIENCY", "WATER_MOVEMENT_EFFICIENCY")
        .put("GENERIC_TEMPT_RANGE", "TEMPT_RANGE")
        .put("PLAYER_BLOCK_INTERACTION_RANGE", "BLOCK_INTERACTION_RANGE")
        .put("PLAYER_ENTITY_INTERACTION_RANGE", "ENTITY_INTERACTION_RANGE")
        .put("PLAYER_BLOCK_BREAK_SPEED", "BLOCK_BREAK_SPEED")
        .put("PLAYER_MINING_EFFICIENCY", "MINING_EFFICIENCY")
        .put("PLAYER_SNEAKING_SPEED", "SNEAKING_SPEED")
        .put("PLAYER_SUBMERGED_MINING_SPEED", "SUBMERGED_MINING_SPEED")
        .put("PLAYER_SWEEPING_DAMAGE_RATIO", "SWEEPING_DAMAGE_RATIO")
        .put("ZOMBIE_SPAWN_REINFORCEMENTS", "SPAWN_REINFORCEMENTS")
        .build();
    //</editor-fold>

    //<editor-fold desc="map cursor types" defaultstate="collapsed">
    private static final Map<String, String> FIELD_PARITY_MAP_CURSOR_TYPE = ImmutableMap.<String, String>builder()
        .put("RED_MARKER", "TARGET_POINT")
        .put("WHITE_POINTER", "PLAYER")
        .put("GREEN_POINTER", "FRAME")
        .put("RED_POINTER", "RED_MARKER")
        .put("BLUE_POINTER", "BLUE_MARKER")
        .put("WHITE_CROSS", "TARGET_X")
        .put("WHITE_CIRCLE", "PLAYER_OFF_MAP")
        .put("SMALL_WHITE_CIRCLE", "PLAYER_OFF_LIMITS")
        .put("TEMPLE", "MONUMENT")
        .put("DESERT_VILLAGE", "VILLAGE_DESERT")
        .put("PLAINS_VILLAGE", "VILLAGE_PLAINS")
        .put("SAVANNA_VILLAGE", "VILLAGE_SAVANNA")
        .put("SNOWY_VILLAGE", "VILLAGE_SNOWY")
        .put("TAIGA_VILLAGE", "VILLAGE_TAIGA")
        .build();
    //</editor-fold>

    private static Consumer<EnumRenameBuilder> fromMap(final Map<String, String> renames) {
        return b -> {
            for (final Map.Entry<String, String> entry : renames.entrySet()) {
                b.rename(entry.getKey(), entry.getValue());
            }
        };
    }

    private static VersionedRuleFactory createRenameRules() {
        final Map<ApiVersion, RenameRule> versions = ImmutableMap.<ApiVersion, RenameRule>builder()
            .put(ApiVersion.NONE, RenameRule.builder() // runs if < 1.13
                .editEnum(Art.class, fromMap(Map.of(
                    "BURNINGSKULL", "BURNING_SKULL",
                    "DONKEYKONG", "DONKEY_KONG"
                )))
                .editEnum(DyeColor.class, b -> b.rename("SILVER", "LIGHT_GRAY"))
                .build())
            .put(ApiVersion.getOrCreateVersion("1.13.99"), RenameRule.builder() // runs if < 1.14
                .editEnum(Material.class, fromMap(Map.of(
                    "CACTUS_GREEN", "GREEN_DYE",
                    "DANDELION_YELLOW", "YELLOW_DYE",
                    "ROSE_RED", "RED_DYE",
                    "SIGN", "OAK_SIGN",
                    "WALL_SIGN", "OAK_WALL_SIGN"
                )))
                .build())
            .put(ApiVersion.getOrCreateVersion("1.15.99"), RenameRule.builder() // < 1.16(.1)
                .editEnum(Material.class, b -> b.rename("ZOMBIE_PIGMAN_SPAWN_EGG", "ZOMBIFIED_PIGLIN_SPAWN_EGG"))
                .editEnum(Biome.class, b -> b.rename("NETHER", "NETHER_WASTES"))
                .editEnum(EntityType.class, b -> b.rename("PIG_ZOMBIE", "ZOMBIFIED_PIGLIN"))
                .editEnum(LootTables.class, b -> b.rename("ZOMBIE_PIGMAN", "ZOMBIFIED_PIGLIN"))
                .build())
            .put(ApiVersion.getOrCreateVersion("1.16.99"), RenameRule.builder() // < 1.17
                .editEnum(Material.class, b -> b.rename("GRASS_PATH", "DIRT_PATH"))
                .build())
            .put(ApiVersion.getOrCreateVersion("1.17.99"), RenameRule.builder() // < 1.18
                .editEnum(Biome.class, b -> b
                    .rename("TALL_BIRCH_FOREST", "OLD_GROWTH_BIRCH_FOREST")
                    .rename("GIANT_TREE_TAIGA", "OLD_GROWTH_PINE_TAIGA")
                    .rename("GIANT_SPRUCE_TAIGA", "OLD_GROWTH_SPRUCE_TAIGA")
                    .rename("SNOWY_TUNDRA", "SNOWY_PLAINS")
                    .rename("JUNGLE_EDGE", "SPARSE_JUNGLE")
                    .rename("STONE_SHORE", "STONY_SHORE")
                    .rename("MOUNTAINS", "WINDSWEPT_HILLS")
                    .rename("WOODED_MOUNTAINS", "WINDSWEPT_FOREST")
                    .rename("GRAVELLY_MOUNTAINS", "WINDSWEPT_GRAVELLY_HILLS")
                    .rename("SHATTERED_SAVANNA", "WINDSWEPT_SAVANNA")
                    .rename("WOODED_BADLANDS_PLATEAU", "WOODED_BADLANDS")
                )
                .build())
            .put(ApiVersion.getOrCreateVersion("1.19.2"), RenameRule.builder() // < 1.19.3
                .editEnum(DisplaySlot.class, b -> { // TODO test
                    for (final String color : SLOT_COLORS) {
                        b.rename("SIDEBAR_" + color, "SIDEBAR_TEAM_" + color);
                    }
                })
                .build())
            .put(ApiVersion.getOrCreateVersion("1.19.99"), RenameRule.builder() // < 1.20
                .type("org/bukkit/entity/TextDisplay$TextAligment" /*sic*/, TextDisplay.TextAlignment.class)
                .build())
            .put(ApiVersion.getOrCreateVersion("1.20.2"), RenameRule.builder() // < 1.20.3
                .editEnum(Material.class, b -> b.rename("GRASS", "SHORT_GRASS"))
                .build())
            .put(ApiVersion.getOrCreateVersion("1.20.4"), RenameRule.builder() // < 1.20.5 (big rename here, happened after commit to update, but at the same time as the 1.20.5 release)
                .editEnum(Material.class, b -> b.rename("SCUTE", "TURTLE_SCUTE"))
                .editEnum(PatternType.class, b -> b // remember that these types were enums in 1.20.4, but now aren't.
                    .rename("DIAGONAL_RIGHT", "DIAGONAL_UP_RIGHT")
                    .rename("STRIPE_SMALL", "SMALL_STRIPES")
                    .rename("DIAGONAL_LEFT_MIRROR", "DIAGONAL_UP_LEFT")
                    .rename("DIAGONAL_RIGHT_MIRROR", "DIAGONAL_RIGHT")
                    .rename("CIRCLE_MIDDLE", "CIRCLE")
                    .rename("RHOMBUS_MIDDLE", "RHOMBUS")
                    .rename("HALF_VERTICAL_MIRROR", "HALF_VERTICAL_RIGHT")
                    .rename("HALF_HORIZONTAL_MIRROR", "HALF_HORIZONTAL_MIRROR")
                )
                .editEnum(EntityType.class, b -> FIELD_PARITY_ENTITY_TYPES.forEach(b::rename))
                .editEnum(PotionType.class, b -> FIELD_PARITY_POTION_TYPES.forEach(b::rename))
                .editEnum(Particle.class, b -> FIELD_PARITY_PARTICLES.forEach(b::rename))
                .fieldsByClass(Enchantment.class, FIELD_PARITY_ENCHANTMENTS)
                .fieldsByClass(PotionEffectType.class, FIELD_PARITY_POTION_EFFECT_TYPES)
                .fieldsByClass(MusicInstrument.class, FIELD_PARITY_MUSIC_INSTRUMENTS)
                .editEnum(Attribute.class, b -> b.rename("HORSE_JUMP_STRENGTH", "GENERIC_JUMP_STRENGTH")) // not field parity commit
                .editEnum(MapCursor.Type.class, b -> FIELD_PARITY_MAP_CURSOR_TYPE.forEach(b::rename))
                .editEnum(ItemFlag.class, b -> b
                    .rename("HIDE_POTION_EFFECTS", "HIDE_ADDITIONAL_TOOLTIP")
                    .rename("HIDE_ITEM_SPECIFICS", "HIDE_ADDITIONAL_TOOLTIP")
                )
                .type("org/spigotmc/event/entity/EntityMountEvent", EntityMountEvent.class)
                .type("org/spigotmc/event/entity/EntityDismountEvent", EntityDismountEvent.class)
                .build())
            .put(ApiVersion.getOrCreateVersion("1.21.1"), RenameRule.builder()
                .editEnum(EntityType.class, b -> b
                    .rename("BOAT", "OAK_BOAT")
                    .rename("CHEST_BOAT", "OAK_CHEST_BOAT")
                )
                .build())
            .put(ApiVersion.getOrCreateVersion("1.21.3"), RenameRule.builder() // < 1.21.4
                .editEnum(Attribute.class, b -> FIELD_PARITY_ATTRIBUTES.forEach(b::rename)) // this also changed to interface at same time
                .build())
            .put(ApiVersion.getOrCreateVersion("1.21.4"), RenameRule.builder() // < 1.21.5
                .editEnum(EntityType.class, b -> b.rename("POTION", "SPLASH_POTION"))
                .type("org/bukkit/block/data/type/Crafter$Orientation", Orientation.class)
                .type("org/bukkit/block/data/type/Jigsaw$Orientation", Orientation.class)
                .type("org/bukkit/block/data/type/MossyCarpet$Height", Wall.Height.class)
                .type("org/bukkit/entity/Cow", AbstractCow.class)
                .build())
            .put(ApiVersion.getOrCreateVersion("1.21.8"), RenameRule.builder() // < 1.21.9
                .editEnum(Material.class, b -> b.rename("CHAIN", "IRON_CHAIN"))
                .build())
            .put(ApiVersion.getOrCreateVersion("1.21.10"), RenameRule.builder() // < 1.21.11
                .fieldsByClass(
                    Sound.class,
                    Map.of(
                        "ENTITY_LEASH_KNOT_PLACE", "ITEM_LEAD_TIED",
                        "ENTITY_LEASH_KNOT_BREAK", "ITEM_LEAD_BREAK"
                    )
                )
                .fieldsByClass(
                    ItemType.class,
                    Map.of(
                        "CHAIN", "IRON_CHAIN"
                    )
                )
                .fieldsByClass(
                    BlockType.class,
                    Map.of(
                        "CHAIN", "IRON_CHAIN"
                    )
                )
                .build())
            .build();
        return MergingVersionRuleFactory.mergeable(new TreeMap<>(versions));
    }

    @Override
    public VersionedRuleFactory createRootFactory() {
        final VersionedRuleScanner scanner = new VersionedRuleScanner(ApiVersion::getOrCreateVersion);
        final VersionedRuleFactory renames = createRenameRules();
        final VersionedRuleFactory renamedAdditional = scanner.scan(RenamesBytecodeModifier.class);


        return VersionedRuleFactory.chain(renames, renamedAdditional);
    }

    @Version("1.20.4")
    @SuppressWarnings("deprecation")
    @DirectStaticRewrite.Wrapper(owners = "org.bukkit.enchantments.Enchantment", type = MethodType.STATIC, methodName = "getByName")
    public static @Nullable Enchantment getByName_Enchantment(final String name) { // TODO test
        return Enchantment.getByName(FieldRename.convertEnchantmentName(ApiVersion.CURRENT, name));
    }

    @Version("1.20.4")
    @SuppressWarnings("deprecation")
    @DirectStaticRewrite.Wrapper(owners = "org.bukkit.entity.EntityType", type = MethodType.STATIC, methodName = "fromName")
    public static @Nullable EntityType fromName_EntityType(final String name) { // TODO test
        return EntityType.fromName(FieldRename.convertEntityTypeName(ApiVersion.CURRENT, name));
    }

    @Version("1.20.4")
    @SuppressWarnings("deprecation")
    @DirectStaticRewrite.Wrapper(owners = "org.bukkit.potion.PotionEffectType", type = MethodType.STATIC, methodName = "getByName")
    public static @Nullable PotionEffectType getByName_PotionEffectType(final String name) { // TODO test
        return PotionEffectType.getByName(FieldRename.convertPotionEffectTypeName(ApiVersion.CURRENT, name));
    }
}
