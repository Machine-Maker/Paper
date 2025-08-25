package io.papermc.paper.plugin.entrypoint.classloader.bytecode.version;

import io.papermc.asm.rules.RewriteRule;
import io.papermc.asm.rules.RuleScanner;
import io.papermc.asm.rules.method.DirectStaticRewrite;
import io.papermc.paper.plugin.entrypoint.classloader.bytecode.VersionedClassloaderBytecodeModifier;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.event.entity.EntityCombustEvent;

public class V1_20_4 extends VersionedClassloaderBytecodeModifier {

    public static final ApiVersion VERSION = ApiVersion.getOrCreateVersion("1.20.4");

    public V1_20_4(final int api) {
        super(api);
    }

    @Override
    protected RewriteRule createRule() {
        return RuleScanner.scan(V1_20_4.class);
    }

    @DirectStaticRewrite.Wrapper(ownerClasses = EntityCombustEvent.class)
    public static int getDuration(final EntityCombustEvent event) {
        return (int) event.getDuration(); // TODO test
    }
}
