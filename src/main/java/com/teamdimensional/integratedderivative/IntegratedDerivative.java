package com.teamdimensional.integratedderivative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = Tags.DEPENDENCIES)
public class IntegratedDerivative extends ModBaseVersionable {

    @SidedProxy(clientSide = "com.teamdimensional.integratedderivative.ClientProxy", serverSide = "com.teamdimensional.integratedderivative.CommonProxy")
    public static ICommonProxy proxy;

    @Mod.Instance(value = Tags.MOD_ID)
    public static IntegratedDerivative INSTANCE;

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public IntegratedDerivative() {
        super(Tags.MOD_ID, Tags.MOD_NAME, Tags.VERSION);
    }

    @Override
    protected RecipeHandler constructRecipeHandler() {
        return null;
    }

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ICommonProxy proxy = this.getProxy();
        if (proxy != null) {
            proxy.registerEventHooks();
        }
        DerivativeOperators.load();
        LOGGER.info("Hello From {}!", Tags.MOD_NAME);
    }

    @Mod.EventHandler
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public CreativeTabs constructDefaultCreativeTab() {
        return null;
    }

    @Override
    public ICommonProxy getProxy() {
        return proxy;
    }

}
