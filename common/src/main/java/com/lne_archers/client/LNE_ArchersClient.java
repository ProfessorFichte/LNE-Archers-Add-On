package com.lne_archers.client;

import com.lne_archers.client.effect.RangersFocusParticles;
import com.lne_archers.effects.Effects;
import com.lne_archers.spells.ArchersSpells;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.client.gui.SpellTooltip;

public class LNE_ArchersClient{
    public static void init() {
        for (var entry : ArchersSpells.entries) {
            if (entry.mutator() != null) {
                SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
            }
        }
        CustomParticleStatusEffect.register(Effects.RANGERS_FOCUS.effect, new RangersFocusParticles(15));
    }
}
