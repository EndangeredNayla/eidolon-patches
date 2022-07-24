package elucent.eidolon.codex;

import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.Eidolon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityPage<T extends LivingEntity> extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_entity_page.png");

    EntityType<T> type;

    public EntityPage(EntityType<T> type) {
        super(BACKGROUND);
        this.type = type;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, PoseStack poseStack, int x, int y, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        assert level != null;

        T e = type.create(level);

        int xt = x + 64;
        int yt = y + 136;
        InventoryScreen.renderEntityInInventory(xt, yt, 30, xt - mouseX, yt - mouseY, e);
    }
}
