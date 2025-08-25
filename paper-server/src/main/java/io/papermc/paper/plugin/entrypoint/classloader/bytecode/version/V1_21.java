package io.papermc.paper.plugin.entrypoint.classloader.bytecode.version;

import io.papermc.asm.rules.RewriteRule;
import io.papermc.asm.rules.classes.ClassToInterfaceRule;
import io.papermc.paper.plugin.entrypoint.classloader.bytecode.VersionedClassloaderBytecodeModifier;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.inventory.CraftAbstractInventoryView;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.InventoryView;
import org.bukkit.map.MapCursor;

public class V1_21 extends VersionedClassloaderBytecodeModifier {

    public static final ApiVersion VERSION = ApiVersion.getOrCreateVersion("1.21");

    public V1_21(final int api) {
        super(api);
    }

    private static final List<Class<?>> REGISTRY_TYPES_TO_INTERFACES = List.of(
        Villager.Type.class,
        Villager.Profession.class,
        Frog.Variant.class,
        Cat.Type.class,
        MapCursor.Type.class,
        PatternType.class
    );

    @Override
    protected RewriteRule createRule() {
        final List<RewriteRule> rules = new ArrayList<>();
        rules.add(new ClassToInterfaceRule(InventoryView.class, CraftAbstractInventoryView.class));
        for (final Class<?> iType : REGISTRY_TYPES_TO_INTERFACES) {
            rules.add(new ClassToInterfaceRule(iType, null));
        }
        return RewriteRule.chain(rules);
    }
}
