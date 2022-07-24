package elucent.eidolon.setup;

import elucent.eidolon.Registry;
import elucent.eidolon.client.models.ModelRegistry;
import elucent.eidolon.client.models.entity.NecromancerModel;
import elucent.eidolon.client.models.entity.WraithModel;
import elucent.eidolon.client.models.entity.ZombieBruteModel;
import elucent.eidolon.client.renderer.blockentity.*;
import elucent.eidolon.client.renderer.entity.NecromancerRenderer;
import elucent.eidolon.client.renderer.entity.WraithRenderer;
import elucent.eidolon.client.renderer.entity.ZombieBruteRenderer;
import elucent.eidolon.entity.EmptyRenderer;
import elucent.eidolon.gui.SoulEnchanterScreen;
import elucent.eidolon.gui.WoodenBrewingStandScreen;
import elucent.eidolon.gui.WorktableScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author DustW
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event){
        EntityRenderers.register(Registry.ZOMBIE_BRUTE.get(), (erm) -> new ZombieBruteRenderer(erm, new ZombieBruteModel(erm.bakeLayer(ModelRegistry.ZOMBIE_BURTE)), 0.6f));
        EntityRenderers.register(Registry.WRAITH.get(), (erm) -> new WraithRenderer(erm, new WraithModel(erm.bakeLayer(ModelRegistry.WRAITH)), 0.6f));
        EntityRenderers.register(Registry.NECROMANCER.get(), (erm) -> new NecromancerRenderer(erm, new NecromancerModel(erm.bakeLayer(ModelRegistry.NECROMANCER)), 0.6f));
        EntityRenderers.register(Registry.SOULFIRE_PROJECTILE.get(), EmptyRenderer::new);
        EntityRenderers.register(Registry.BONECHILL_PROJECTILE.get(), EmptyRenderer::new);
        EntityRenderers.register(Registry.NECROMANCER_SPELL.get(), EmptyRenderer::new);
        EntityRenderers.register(Registry.CHANT_CASTER.get(), EmptyRenderer::new);
        BlockEntityRenderers.register(Registry.HAND_TILE_ENTITY.get(), (trd) -> new HandTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.BRAZIER_TILE_ENTITY.get(), (trd) -> new BrazierTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.NECROTIC_FOCUS_TILE_ENTITY.get(), (trd) -> new NecroticFocusTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.CRUCIBLE_TILE_ENTITY.get(), (trd) -> new CrucibleTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.SOUL_ENCHANTER_TILE_ENTITY.get(), (trd) -> new SoulEnchanterTileRenderer(trd.getBlockEntityRenderDispatcher()));
        BlockEntityRenderers.register(Registry.GOBLET_TILE_ENTITY.get(), (trd) -> new GobletTileRenderer(trd.getBlockEntityRenderDispatcher()));

        ItemBlockRenderTypes.setRenderLayer(Registry.ENCHANTED_ASH.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.WOODEN_STAND.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.GOBLET.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.UNHOLY_EFFIGY.get(), RenderType.cutoutMipped());

        event.enqueueWork(() -> {
            MenuScreens.register(Registry.WORKTABLE_CONTAINER.get(), WorktableScreen::new);
            MenuScreens.register(Registry.SOUL_ENCHANTER_CONTAINER.get(), SoulEnchanterScreen::new);
            MenuScreens.register(Registry.WOODEN_STAND_CONTAINER.get(), WoodenBrewingStandScreen::new);
        });
    }
}
