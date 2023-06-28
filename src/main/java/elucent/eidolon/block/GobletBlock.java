package elucent.eidolon.block;

import elucent.eidolon.tile.GobletTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GobletBlock extends BlockBase implements EntityBlock {
    public GobletBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GobletTileEntity(pos, state);
    }
}
