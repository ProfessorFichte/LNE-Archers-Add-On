package com.lne_archers.client;

import com.lne_archers.client.effect.RangersFocusParticles;
import com.lne_archers.effects.Effects;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static com.lne_archers.LNE_ArchersMod.MOD_ID;

public class LNE_ArchersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomModels.registerModelIds(List.of(
                new Identifier(MOD_ID, "projectile/dragon_bolt")
        ));

        CustomParticleStatusEffect.register(Effects.RANGERS_FOCUS, new RangersFocusParticles(15));
    }
}
