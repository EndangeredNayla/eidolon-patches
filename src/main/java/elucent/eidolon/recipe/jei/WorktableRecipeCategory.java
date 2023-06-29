package elucent.eidolon.recipe.jei;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.recipe.WorktableRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WorktableRecipeCategory implements IRecipeCategory<WorktableRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Eidolon.MODID, "worktable");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Eidolon.MODID, "textures/gui/jei_worktable.png");

    private final IDrawable background;
    private final IDrawable icon;

    public WorktableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 190, 139);
        this.icon = helper.createDrawableItemStack(new ItemStack(Registry.WORKTABLE.get()));
    }

    @Override
    public RecipeType<WorktableRecipe> getRecipeType() {
        return EidolonJEIPlugin.WORKTABLE_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.eidolon_worktable");
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
    public void setRecipe(IRecipeLayoutBuilder builder, WorktableRecipe recipe, IFocusGroup focuses) {
        // loop? never heard of it
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 40).addIngredients(recipe.getCore()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT, 58, 40).addIngredients(recipe.getCore()[1]);
        builder.addSlot(RecipeIngredientRole.INPUT, 76, 40).addIngredients(recipe.getCore()[2]);
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 58).addIngredients(recipe.getCore()[3]);
        builder.addSlot(RecipeIngredientRole.INPUT, 58, 58).addIngredients(recipe.getCore()[4]);
        builder.addSlot(RecipeIngredientRole.INPUT, 76, 58).addIngredients(recipe.getCore()[5]);
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 76).addIngredients(recipe.getCore()[6]);
        builder.addSlot(RecipeIngredientRole.INPUT, 58, 76).addIngredients(recipe.getCore()[7]);
        builder.addSlot(RecipeIngredientRole.INPUT, 76, 76).addIngredients(recipe.getCore()[8]);

        builder.addSlot(RecipeIngredientRole.INPUT, 58, 18).addIngredients(recipe.getOuter()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 58).addIngredients(recipe.getOuter()[1]);
        builder.addSlot(RecipeIngredientRole.INPUT, 58, 98).addIngredients(recipe.getOuter()[2]);
        builder.addSlot(RecipeIngredientRole.INPUT, 18, 58).addIngredients(recipe.getOuter()[3]);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 163, 58).addItemStack(recipe.getResultItem());
    }
}
