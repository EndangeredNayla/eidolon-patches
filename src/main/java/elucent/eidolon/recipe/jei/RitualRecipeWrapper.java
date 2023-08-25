package elucent.eidolon.recipe.jei;

import elucent.eidolon.ritual.Ritual;

//new wrapper to minimize confusion as none of the old wrappers w/ pages are used
public record RitualRecipeWrapper(Ritual ritual, Object sacrifice) {

}

