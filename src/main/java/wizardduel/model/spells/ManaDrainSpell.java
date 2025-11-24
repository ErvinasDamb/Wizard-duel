package wizardduel.model.spells;

import wizardduel.model.Character;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;

/**
 * Spell that drains mana from the target and gives it to the caster.
 */
public class ManaDrainSpell extends AbstractSpell {

    public ManaDrainSpell(
            String id,
            String name,
            int manaCost,
            int power,
            int manaDrainAmount,
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
                1.0,    // accuracy
                0.0,    // critChance
                1.0,    // critMultiplier
                0,      // dotAmount
                0,      // dotDuration
                0.0,    // stunChance
                0.0,    // confuseChance
                0.0,    // slowStrength
                0,      // shieldValue
                manaDrainAmount,
                1.0,    // chargeMultiplier
                type,
                element,
                elementApplyChance,
                elementDuration,
                rarity
        );
    }

    /**
     * Drains mana from target and transfers it to caster.
     */
    @Override
    public void cast(Character caster, Character target) {
        if (!caster.useMana(manaCost)) {
            return;
        }

        int targetMana = target.getMana();
        if (targetMana <= 0 || manaDrainAmount <= 0) {
            return;
        }

        int before = target.getMana();
        target.useMana(getManaDrainAmount());
        int drained = before - target.getMana();
        caster.regenerateMana(drained);
    }
}
