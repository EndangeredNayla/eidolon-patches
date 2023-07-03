package elucent.eidolon.spell;

import java.util.*;

import elucent.eidolon.Eidolon;
import net.minecraft.resources.ResourceLocation;

public class Runes {
    static final Map<ResourceLocation, Rune> runes = new HashMap<>();

    public static void register(Rune rune) {
        runes.put(rune.getRegistryName(), rune);
    }

    public static Rune find(ResourceLocation rl) {
        return runes.getOrDefault(rl, null);
    }

    public static Collection<Rune> getRunes() {
        return runes.values();
    }

    public static void init() {
        register(new Rune(new ResourceLocation(Eidolon.MODID, "sin")) {
            @Override
            public RuneResult doEffect(SignSequence seq) {
                seq.addRight(Signs.WICKED_SIGN);
                return RuneResult.PASS;
            }
        });
        register(new Rune(new ResourceLocation(Eidolon.MODID, "crimson_rose")) {
            @Override
            public RuneResult doEffect(SignSequence seq) {
                if (seq.removeRightmostN(Signs.WICKED_SIGN, 2)) {
                    seq.addRight(Signs.BLOOD_SIGN);
                    return RuneResult.PASS;
                }
                return RuneResult.FAIL;
            }
        });
        register(new Rune(new ResourceLocation(Eidolon.MODID, "ascend")) {
            @Override
            public RuneResult doEffect(SignSequence seq) {
                if (seq.removeRightmost(Signs.BLOOD_SIGN)) {
                    seq.addRight(Signs.SOUL_SIGN);
                    return RuneResult.PASS;
                }
                return RuneResult.FAIL;
            }
        });
    }
}
