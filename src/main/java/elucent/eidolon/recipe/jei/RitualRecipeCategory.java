package elucent.eidolon.recipe.jei;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import elucent.eidolon.ClientRegistry;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.ritual.*;
import elucent.eidolon.util.RecipeUtil;
import elucent.eidolon.util.RenderUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RitualRecipeCategory implements IRecipeCategory<RitualRecipeWrapper> {
    public static final ResourceLocation UID = new ResourceLocation(Eidolon.MODID, "ritual");
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/jei_page_bg.png");
    public static final ResourceLocation PAGE_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_ritual_page.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable overlay;
    private final IDrawable itemIcon;
    private final IDrawable focusIcon;

    public RitualRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BACKGROUND, 0, 0, 138, 172);
        this.icon = helper.createDrawableItemStack(new ItemStack(Registry.BRAZIER.get()));
        this.overlay = helper.createDrawable(PAGE_TEXTURE, 0, 0, 128, 160);
        this.itemIcon = helper.createDrawable(PAGE_TEXTURE, 154, 0, 16, 16);
        this.focusIcon = helper.createDrawable(PAGE_TEXTURE,128, 0, 26, 24);
    }

    @Override
    public RecipeType<RitualRecipeWrapper> getRecipeType() {
        return EidolonJEIPlugin.RITUAL_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.eidolon.ritual");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RitualRecipeWrapper recipe, IFocusGroup focuses) {
        Ritual ritual = recipe.ritual();
        Object sacrifice = recipe.sacrifice();

        List<Ingredient> ingredients = new ArrayList<>();
        ritual.getRequirements().forEach((req) -> {
            if(req instanceof ItemRequirement) {
                ingredients.add(RecipeUtil.ingredientFromObject(((ItemRequirement)req).getMatch()));
            }
        });
        for (int i = 0; i < ritual.getContinuousRequirements().size(); i++) {
            IRequirement req = ritual.getContinuousRequirements().get(i);
            if (req instanceof FocusItemRequirement) {
                ingredients.add(ingredients.size() / 2 + i, RecipeUtil.ingredientFromObject(((FocusItemRequirement) req).getMatch()));
            }
        }
        ingredients.add(RecipeUtil.ingredientFromObject(sacrifice instanceof MultiItemSacrifice ? ((MultiItemSacrifice)sacrifice).main : sacrifice));

        float angleStep = Math.min(30, 180 / (ingredients.size() - 1));
        double rootAngle = 90 - (ingredients.size() - 2) * angleStep / 2;
        for (int i = 0; i < ingredients.size() - 1; i ++) {
            double a = Math.toRadians(rootAngle + angleStep * i);
            int dx = (int)(68 + 48 * Math.cos(a));
            int dy = (int)(91 + 48 * Math.sin(a));
            builder.addSlot(RecipeIngredientRole.INPUT, dx - 8, dy - 8).addIngredients(ingredients.get(i));
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 60, 83).addIngredients(ingredients.get(ingredients.size()-1));
    }

    @Override
    public void draw(RitualRecipeWrapper recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        Ritual ritual = recipe.ritual();
        //overlay
        overlay.draw(poseStack, 4, 3);

        //ritual ingredients
        List<RitualIngredient> ingredients = getRitualIngredients(recipe);

        //temp
        int x = 4, y = 3;

        float angleStep = Math.min(30, 180 / ingredients.size());
        double rootAngle = 90 - (ingredients.size() - 1) * angleStep / 2;
        for (int i = 0; i < ingredients.size(); i ++) {
            double a = Math.toRadians(rootAngle + angleStep * i);
            int dx = (int)(64 + 48 * Math.cos(a));
            int dy = (int)(88 + 48 * Math.sin(a));
            if (ingredients.get(i).isFocus) {
                focusIcon.draw(poseStack, x + dx - 13, y + dy - 13);
            }
            else {
                itemIcon.draw(poseStack, x + dx - 8, y + dy - 8);
            }
        }


        //glyph thing
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        Tesselator tess = Tesselator.getInstance();
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(ClientRegistry::getGlowingShader);
        RenderUtil.dragon(poseStack, MultiBufferSource.immediate(tess.getBuilder()), x + 64, y + 48, 20, 20, ritual.getRed(), ritual.getGreen(), ritual.getBlue());
        tess.end();
        RenderSystem.enableTexture();
        RenderSystem.setShader(ClientRegistry::getGlowingSpriteShader);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        for (int j = 0; j < 2; j++) {
            RenderUtil.litQuad(poseStack, MultiBufferSource.immediate(tess.getBuilder()), x + 52, y + 36, 24, 24,
                    ritual.getRed(), ritual.getGreen(), ritual.getBlue(), Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ritual.getSymbol()));
            tess.end();
        }
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        //text!
        Font font = Minecraft.getInstance().font;
        String text = I18n.get("ritual." + ritual.getRegistryName().getNamespace() + "." + ritual.getRegistryName().getPath());
        font.draw(poseStack, text, x + 63 - font.width(text) / 2, y + 13 - font.lineHeight, 0xFF404040);

    }

    private List<RitualIngredient> getRitualIngredients(RitualRecipeWrapper recipe) {
        Ritual ritual = recipe.ritual();
        Object sacrifice = recipe.sacrifice();
        List<RitualIngredient> inputs = new ArrayList<>();
        List<ItemStack> foci = new ArrayList<>();
        if (sacrifice instanceof MultiItemSacrifice) for (Object o : ((MultiItemSacrifice)sacrifice).items) {
            foci.add(RecipeUtil.stackFromObject(o));
        }
        for (IRequirement req : ritual.getRequirements()) {
            if (req instanceof ItemRequirement)
                inputs.add(new RitualIngredient(RecipeUtil.stackFromObject(((ItemRequirement)req).getMatch()), false));
        }
        for (int i = 0; i < ritual.getContinuousRequirements().size(); i++) {
            IRequirement req = ritual.getContinuousRequirements().get(i);
            if (req instanceof FocusItemRequirement) {
                inputs.add(inputs.size() / 2 + i, new RitualIngredient(RecipeUtil.stackFromObject(((FocusItemRequirement)req).getMatch()), true));
            }
        }


        Iterator<ItemStack> iter = foci.iterator();
        while (iter.hasNext()) {
            ItemStack focus = iter.next();
            for (RitualIngredient input : inputs) {
                if (ItemStack.isSame(focus, input.stack) && ItemStack.tagMatches(focus, input.stack)
                        && !input.isFocus) {
                    input.isFocus = true;
                    iter.remove();
                    break;
                }
            }
        }

        return inputs;
    }


    static class RitualIngredient {
        public final ItemStack stack;
        public boolean isFocus;

        public RitualIngredient(ItemStack stack, boolean isFocus) {
            this.stack = stack;
            this.isFocus = isFocus;
        }
    }
}
