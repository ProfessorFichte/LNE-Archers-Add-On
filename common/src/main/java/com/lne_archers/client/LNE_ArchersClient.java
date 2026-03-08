package com.lne_archers.client;

import com.lne_archers.client.effect.RangersFocusParticles;
import com.lne_archers.effects.Effects;
import net.spell_engine.api.effect.CustomParticleStatusEffect;

public class LNE_ArchersClient{
    public static void init() {
        CustomParticleStatusEffect.register(Effects.RANGERS_FOCUS.effect, new RangersFocusParticles(15));
    }
}
