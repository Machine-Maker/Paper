package io.papermc.paper.plugin.entrypoint.classloader.bytecode.version;

import io.papermc.asm.rules.RewriteRule;
import io.papermc.asm.rules.classes.ClassToInterfaceRule;
import io.papermc.paper.plugin.entrypoint.classloader.bytecode.VersionedClassloaderBytecodeModifier;
import java.util.List;
import org.bukkit.Art;
import org.bukkit.Fluid;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.util.ApiVersion;

public class V1_21_3 extends VersionedClassloaderBytecodeModifier {

    public static final ApiVersion VERSION = ApiVersion.getOrCreateVersion("1.21.3");
    private static final List<Class<?>> REGISTRY_TYPES_TO_INTERFACES = List.of(
        Attribute.class,
        Fluid.class,
        Biome.class,
        Sound.class,
        Art.class
    );

    public V1_21_3(final int api) {
        super(api);
    }

    @Override
    protected RewriteRule createRule() {
        return RewriteRule.chain(REGISTRY_TYPES_TO_INTERFACES.stream().map(c -> new ClassToInterfaceRule(c, null)).toList());
    }
}
