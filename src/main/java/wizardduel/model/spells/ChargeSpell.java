package wizardduel.model.spells;

import wizardduel.model.Character;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;

import java.util.concurrent.ThreadLocalRandom;

/**
 * High-impact damage spell that scales with chargeMultiplier.
 */
public class ChargeSpell extends AbstractSpell {

    public ChargeSpell(
            String id,
            String name,
            int manaCost,
            int power,
            int damageMin,
            int damageMax,
            double accuracy,
            double critChance,
            double critMultiplier,
            double chargeMultiplier,
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
                damageMin,
                damageMax,
                accuracy,
                critChance,
                critMultiplier,
                0,          // dotAmount
                0,          // dotDuration
                0.0,        // stunChance
                0.0,        // confuseChance
                0.0,        // slowStrength
                0,          // shieldValue
                0,          // manaDrainAmount
                chargeMultiplier,
                SpellType.CHARGE,
                element,
                elementApplyChance,
                elementDuration,
                rarity
        );
    }

    @Override
    public void cast(Character caster, Character target) {
        if (!caster.useMana(manaCost)) {
            return;
        }

        ThreadLocalRandom rng = ThreadLocalRandom.current();

        double hitRoll = rng.nextDouble();
        if (hitRoll > accuracy) {
            return; // pramovė
        }

        int baseDamage = damageMin;
        if (damageMax > damageMin) {
            baseDamage += rng.nextInt(damageMax - damageMin + 1);
        }

        // crit
        if (rng.nextDouble() < critChance) {
            baseDamage = (int) Math.round(baseDamage * critMultiplier);
        }

        if (baseDamage <= 0) {
            return;
        }

        // štai čia pagaliau naudojam chargeMultiplier
        int finalDamage = (int) Math.round(baseDamage * chargeMultiplier);

        target.takeDamage(finalDamage);
    }
}
