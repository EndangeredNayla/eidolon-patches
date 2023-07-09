package elucent.eidolon.item;

import elucent.eidolon.Registry;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.research.Research;
import elucent.eidolon.research.Researches;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotetakingToolsItem extends ItemBase {
    public NotetakingToolsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotID, boolean isSelected) {
        if (isSelected && level instanceof ClientLevel clientLevel && clientLevel.getGameTime() % 5 == 0) {
            List<Entity> entities = new ArrayList<>();
            List<BlockPos> blocks = new ArrayList<>();

            BlockPos.betweenClosed(entity.getOnPos().offset(-4, -4, -4), entity.getOnPos().offset(4, 4, 4)).forEach(pos -> {
                if (!Researches.getBlockResearches(clientLevel.getBlockState(pos).getBlock()).isEmpty())
                    blocks.add(pos.immutable());
            });

            clientLevel.entitiesForRendering().forEach(target -> {
                if (!Researches.getEntityResearches(target).isEmpty())
                    entities.add(target);
            });

            for (Entity target : entities) {
                Particles.create(elucent.eidolon.registries.Particles.SPARKLE_PARTICLE.get())
                        .setAlpha(0.4f, 0).setScale(0.125f, 0.0f).setLifetime(25)
                        .randomOffset(target.getBbWidth(), 0.4)
                        .setColor(0.33f,  0.38f,  0.91f)
                        .addVelocity(0, 0.1f, 0)
                        .setScale(0.15f)
                        .setSpin(0.1f)
                        .repeat(clientLevel, target.getX(), target.getY()+0.3, target.getZ(), 5);
            }

            for (BlockPos pos : blocks) {
                Particles.create(elucent.eidolon.registries.Particles.SPARKLE_PARTICLE.get())
                        .setAlpha(0.4f, 0).setScale(0.125f, 0.0f).setLifetime(20)
                        .randomOffset(1, 0.5)
                        .setColor(0.33f,  0.38f,  0.91f)
                        .addVelocity(0, 0.1f, 0)
                        .setScale(0.15f)
                        .setSpin(0.1f)
                        .repeat(clientLevel, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5,10);
            }
        }
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        Collection<Research> researches = Researches.getEntityResearches(entity);
        if (!researches.isEmpty()) {
            Research r = researches.iterator().next();
            if (!player.level.isClientSide && r != null) {
                ItemStack notes = new ItemStack(Registry.RESEARCH_NOTES.get(), 1);
                notes.getOrCreateTag().putString("research", r.getRegistryName().toString());
                notes.getTag().putInt("stepsDone", 0);
                stack.shrink(1);
                if (stack.getCount() == 0) player.setItemInHand(hand, notes);
                else if (!player.getInventory().add(notes)) {
                    player.drop(notes, false);
                }
            }
            return InteractionResult.PASS;
        }
        return super.interactLivingEntity(stack, player, entity, hand);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        Collection<Research> researches = Researches.getBlockResearches(state.getBlock());
        researches.removeIf((r) -> KnowledgeUtil.knowsResearch(ctx.getPlayer(), r.getRegistryName()));
        if (!researches.isEmpty()) {
            Research r = researches.iterator().next();
            ItemStack notes = new ItemStack(Registry.RESEARCH_NOTES.get(), 1);
            notes.getOrCreateTag().putString("research", r.getRegistryName().toString());
            notes.getTag().putInt("stepsDone", 0);
            ctx.getItemInHand().shrink(1);
            if (ctx.getItemInHand().getCount() == 0)
                ctx.getPlayer().setItemInHand(ctx.getHand(), ItemStack.EMPTY);
            if (!ctx.getPlayer().getInventory().add(notes.copy())) {
                ctx.getPlayer().drop(notes, false);
             }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(ctx);
    }
}
