package elucent.eidolon.recipe.jei;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.recipe.WorktableRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class EidolonJEIPlugin implements IModPlugin {
    public static RecipeType<WorktableRecipe> WORKTABLE_RECIPE =
            new RecipeType<>(WorktableRecipeCategory.UID, WorktableRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Eidolon.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WorktableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Registry.WORKTABLE.get()), WORKTABLE_RECIPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<WorktableRecipe> worktableRecipes = manager.getAllRecipesFor(WorktableRecipe.Type.INSTANCE);

        // add recipes
        registration.addRecipes(WORKTABLE_RECIPE, worktableRecipes);
    }
}
