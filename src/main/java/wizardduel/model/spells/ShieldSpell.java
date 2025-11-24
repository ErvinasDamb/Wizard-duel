package wizardduel.model.spells;

import wizardduel.model.Character;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;

/**
 * Defensive spell that grants shield to the caster.
 */
public class ShieldSpell extends AbstractSpell {

    public ShieldSpell(
            String id,
            String name,
            int manaCost,
            int power,
            int shieldValue,
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
                0,      // damageMin
                0,      // damageMax
                1.0,    // accuracy (irrelevant here)
                0.0,    // critChance
                1.0,    // critMultiplier
                0,      // dotAmount
                0,      // dotDuration
                0.0,    // stunChance
                0.0,    // confuseChance
                0.0,    // slowStrength
                shieldValue,
                0,      // manaDrainAmount
                1.0,    // chargeMultiplier
                type,
                element,
                elementApplyChance,
                elementDuration,
                rarity
        );
    }

    /**
     * Uses mana and applies shield to the caster.
     */
    @Override
    public void cast(Character caster, Character target) {
        if (!caster.useMana(manaCost)) {
            return;
        }
        if (shieldValue <= 0) {
            return;
        }
        caster.applyShield(shieldValue);
    }
}

