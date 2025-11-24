package wizardduel.model.spells;

import wizardduel.model.Character;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;

/**
 * Applies damage-over-time to the target.
 */
public class DotSpell extends AbstractSpell {

    public DotSpell(
            String id,
            String name,
            int manaCost,
            int power,
            int dotAmount,
            int dotDuration,
            SpellType type,
            Element element,
            double elementApplyChance,
            int elementDuration,
            Rarity rarity
    ) {
        super(
                id,
                name,
                manaCost,
                power,
                0,          // damageMin
                0,          // damageMax
                1.0,        // accuracy
                0.0,        // critChance
                1.0,        // critMultiplier
                dotAmount,
                dotDuration,
                0.0,        // stunChance
                0.0,        // confuseChance
                0.0,        // slowStrength
                0,          // shieldValue
                0,          // manaDrainAmount
                1.0,        // chargeMultiplier
                type,
                element,
                elementApplyChance,
                elementDuration,
                rarity
        );
    }

    /**
     * Uses mana and applies a DoT effect on the target.
     */
    @Override
    public void cast(Character caster, Character target) {
        if (!caster.useMana(manaCost)) {
            return;
        }
        if (dotAmount <= 0 || dotDuration <= 0) {
            return;
        }
        target.applyDot(dotAmount, dotDuration);
        // Momentinio damage nedarom – šitas spellas yra DoT-only.
    }
}
