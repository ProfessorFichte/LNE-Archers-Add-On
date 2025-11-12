package com.lne_archers.client;

import com.lne_archers.client.effect.RangersFocusParticles;
import com.lne_archers.effects.Effects;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class LNE_ArchersClient{
    public static void init() {
        CustomModels.registerModelIds(List.of(
                Identifier.of(MOD_ID, "projectile/dragon_bolt"),
                Identifier.of(MOD_ID, "projectile/glacial_arrow")
        ));

        CustomParticleStatusEffect.register(Effects.RANGERS_FOCUS.effect, new RangersFocusParticles(15));
    }
}
