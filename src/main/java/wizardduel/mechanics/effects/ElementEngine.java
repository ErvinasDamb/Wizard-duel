package wizardduel.mechanics.effects;

import wizardduel.model.Character;
import wizardduel.model.Spell;
import wizardduel.model.enums.Element;

/**
 * Responsible for applying elemental effects when a spell lands,
 * and triggering synergy checks afterwards.
 */
public class ElementEngine {

    /**
     * Applies element effect from spell to target if chance succeeds.
     */
    public static void applyElementIfNeeded(Spell spell, Character caster, Character target) {
        Element element = spell.getElement();
        double chance = spell.getElementApplyChance();
        int duration = spell.getElementDuration();

        // No element or duration → skip
        if (element == null || element == Element.NEUTRAL || duration <= 0) {
            return;
        }

        // Roll for application
        if (Math.random() > chance) {
            return; // nepavyko uždėti elemento
        }

        // Add the effect on target
        target.addElementEffect(element, duration);

        // Check for possible synergy on this target
        SynergyEngine.checkForSynergy(target);
    }
}
