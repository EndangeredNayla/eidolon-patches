package elucent.eidolon.world.tree;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import elucent.eidolon.world.WorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class IllwoodTrunkPlacer extends TrunkPlacer {
    public static final Codec<IllwoodTrunkPlacer> CODEC = RecordCodecBuilder.create(
            (placerInstance) -> trunkPlacerParts(placerInstance)
                    .and(ExtraCodecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", 1).forGetter(placer -> placer.minHeightForLeaves))
                    .apply(placerInstance, IllwoodTrunkPlacer::new)
    );

    private final int minHeightForLeaves;

    public IllwoodTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB, int minHeightForLeaves) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
        this.minHeightForLeaves = minHeightForLeaves;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return WorldGen.ILLWOOD_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        int i = freeTreeHeight - 1;
        BlockPos.MutableBlockPos mutablePos = pos.mutable();
        setDirtAt(level, blockSetter, random, mutablePos.below(), config);
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();

        for (int j = 0; j <= i; ++j) {
            if (TreeFeature.validTreePos(level, mutablePos.above(j))) {
                this.placeLog(level, blockSetter, random, mutablePos.above(j), config);
            }

            if (j >= this.minHeightForLeaves) {
                list.add(new FoliagePlacer.FoliageAttachment(mutablePos.above(j).immutable(), 0, false));
            }
        }

        for (int j = 0; j <= random.nextInt(1, 5); ++j) {
            var rootRandom = mutablePos.relative(Direction.from2DDataValue(j-1), 1);
            if (TreeFeature.validTreePos(level, rootRandom)) {
                this.placeLog(level, blockSetter, random, rootRandom, config);
                if (random.nextFloat() < 0.5f && TreeFeature.validTreePos(level, rootRandom.above()))
                    this.placeLog(level, blockSetter, random, rootRandom.above(), config);
            }
        }

        return list;
    }
}
