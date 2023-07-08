package elucent.eidolon.world;

import elucent.eidolon.Eidolon;
import elucent.eidolon.world.tree.IllwoodTrunkPlacer;
import net.minecraft.core.Registry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WorldGen {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Eidolon.MODID);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registry.TRUNK_PLACER_TYPE_REGISTRY, Eidolon.MODID);

    public static final RegistryObject<TrunkPlacerType<IllwoodTrunkPlacer>> ILLWOOD_TRUNK_PLACER = TRUNK_PLACERS.register("illwood_trunk_placer",
            () -> new TrunkPlacerType<>(IllwoodTrunkPlacer.CODEC));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ILLWOOD_TREE_CONFIGURED = CONFIGURED_FEATURES.register("illwood_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(elucent.eidolon.Registry.ILLWOOD_LOG.get()),
                    new IllwoodTrunkPlacer(6, 2, 2, 5),
                    BlockStateProvider.simple(elucent.eidolon.Registry.ILLWOOD_LEAVES.get()),
                    new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 60),
                    new TwoLayersFeatureSize(1, 0, 1)).build()));

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
        TRUNK_PLACERS.register(eventBus);
    }
}
