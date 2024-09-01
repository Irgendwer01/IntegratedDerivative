package com.teamdimensional.integratedderivative.mixins;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;
import java.util.stream.Collectors;

public class LateMixin implements ILateMixinLoader {

    public static final List<String> modMixins = ImmutableList.of(
        "integratedterminals",
        "integrateddynamics",
        "integratedtunnels",
        "forestry",
        "mousetweaks"
    );

    @Override
    public List<String> getMixinConfigs() {
        return modMixins.stream()
            .filter(Loader::isModLoaded)
            .map(mod -> "mixins/mixins.integratedderivative." + mod + ".json")
            .collect(Collectors.toList());
    }
}
