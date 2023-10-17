package elucent.eidolon.recipe.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.recipe.CrucibleRecipe;
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
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CrucibleRecipeCategory implements IRecipeCategory<CrucibleRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Eidolon.MODID, "crucible");
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/jei_page_bg.png");
    public static final ResourceLocation PAGE_TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_crucible_page.png");


    //theres probably a better way of doing this
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable resultIcon;
    private final IDrawable stirIcon;
    private final IDrawable itemIcon;
    private final IDrawable stepIcon;



    public CrucibleRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BACKGROUND, 0, 0, 138, 172);
        this.icon = helper.createDrawableItemStack(new ItemStack(Registry.CRUCIBLE.get()));


        this.resultIcon = helper.createDrawable(PAGE_TEXTURE, 128, 64, 128, 64);
        this.stirIcon = helper.createDrawable(PAGE_TEXTURE, 192, 32, 16, 16);
        this.itemIcon = helper.createDrawable(PAGE_TEXTURE, 176, 33, 16, 16);
        this.stepIcon = helper.createDrawable(PAGE_TEXTURE, 128, 0, 128, 20);
    }


    @Override
    public RecipeType<CrucibleRecipe> getRecipeType() {
        return EidolonJEIPlugin.CRUCIBLE_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.eidolon.crucible");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrucibleRecipe recipe, IFocusGroup focuses) {

        List<CrucibleRecipe.Step> steps = recipe.getSteps();
        int h = steps.size() * 20 + 32;
        int yoff = 80 - h / 2;
        for (int i = 0; i < steps.size(); i ++) {
            int tx = 4, ty = 3 + yoff + i * 20;
            tx += 24;

            //bug: tags only display as the first item. probably would have to make an IngredientWithCount type for it to work properly
            List<CrucibleRecipeCategory.StackIngredient> stepInputs = new ArrayList<>();
            for (Ingredient o : steps.get(i).matches) {
                ItemStack stack = o.getItems().length > 0 ? o.getItems()[0].copy() : ItemStack.EMPTY.copy();
                if (!stack.isEmpty()) stepInputs.add(new StackIngredient(stack, Ingredient.EMPTY));
            }
            condense(stepInputs);

            for (int j = 0; j < stepInputs.size(); j ++) {
                builder.addSlot(RecipeIngredientRole.INPUT, tx, ty).addItemStack(stepInputs.get(j).getStack());
                tx += 17;
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 60, yoff + steps.size() * 20 + 14).addItemStack(recipe.getResultItem());


    }

    @Override
    public void draw(CrucibleRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {

        List<CrucibleRecipe.Step> steps = recipe.getSteps();
        int h = steps.size() * 20 + 32;
        int yoff = 80 - h / 2;

        //result
        resultIcon.draw(poseStack, 4, (yoff + steps.size() * 20 + 14) - 10);

        //steps
        for (int i = 0; i < steps.size(); i ++) {

            int tx = 4, ty = 3 + yoff + i * 20;

            stepIcon.draw(poseStack, tx, ty);

            tx += 24;

            List<CrucibleRecipeCategory.StackIngredient> stepInputs = new ArrayList<>();
            for (Ingredient o : steps.get(i).matches) {
                ItemStack stack = o.getItems().length > 0 ? o.getItems()[0].copy() : ItemStack.EMPTY.copy();
                if (!stack.isEmpty()) stepInputs.add(new StackIngredient(stack, Ingredient.EMPTY));
            }
            condense(stepInputs);

            for (int j = 0; j < stepInputs.size(); j ++) {

                itemIcon.draw(poseStack, tx, ty + 1);
                tx += 17;
            }
            for (int j = 0; j < steps.get(i).stirs; j++) {
                stirIcon.draw(poseStack, tx, ty + 1);
                tx += 17;
            }

        }
        //text
        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < steps.size(); i ++) {
            int tx = 4, ty = 3 + yoff + i * 20;
            font.draw(poseStack, I18n.get("enchantment.level." + (i + 1)) + ".", tx + 7, ty + 17 - font.lineHeight, 0xFF404040);
        }


    }

    //this is probably redundant but i dont want to figure out how condense() works so
    protected static class StackIngredient {
        ItemStack stack;
        Ingredient ingredient;

        public StackIngredient(ItemStack stack, Ingredient ingredient) {
            this.stack = stack;
            this.ingredient = ingredient;
        }

        public ItemStack getStack() {
            return stack;
        }

        public Ingredient getIngredient() {
            return ingredient;
        }
    }

    public static void condense(List<StackIngredient> stacks) {
        Iterator<StackIngredient> iter = stacks.iterator();
        StackIngredient last = new StackIngredient(ItemStack.EMPTY, Ingredient.EMPTY);
        while (iter.hasNext()) {
            StackIngredient i = iter.next();
            if (!ItemStack.isSame(i.stack, last.stack) || !ItemStack.tagMatches(i.stack, last.stack) || last.stack.getCount() + i.stack.getCount() > last.stack.getMaxStackSize()) {
                last = i;
            }
            else {
                last.stack.grow(i.stack.getCount());
                iter.remove();
            }
        }
    }
}
