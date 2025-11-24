package wizardduel.model.spells;

import wizardduel.model.Character;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Generic direct-damage spell that uses accuracy, crit and damage range.
 * Element application and synergy are handled by higher-level battle logic.
 */
public class DamageSpell extends AbstractSpell {

    /**
     * Constructor for a direct damage spell.
     * Unused control / shield fields are filled with safe defaults.
     */
    public DamageSpell(
            String id,
            String name,
            int manaCost,
            int power,
            int damageMin,
            int damageMax,
            double accuracy,
            double critChance,
            double critMultiplier,
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
                damageMin,
                damageMax,
                accuracy,
                critChance,
                critMultiplier,
                0,            // dotAmount
                0,            // dotDuration
                0.0,          // stunChance
                0.0,          // confuseChance
                0.0,          // slowStrength
                0,            // shieldValue
                0,            // manaDrainAmount
                1.0,          // chargeMultiplier
                type,
                element,
                elementApplyChance,
                elementDuration,
                rarity
        );
    }

    /**
     * Casts this damage spell from caster to target.
     * - Checks mana
     * - Rolls hit based on accuracy
     * - Calculates damage in [damageMin, damageMax]
     * - Applies critical hit if critChance succeeds
     * - Applies damage to target via Character.takeDamage(...)
     *
     * NOTE: element application & synergy will be handled outside this method.
     */
    @Override
    public void cast(Character caster, Character target) {
        // Not enough mana → spell fizzles.
        if (!caster.useMana(manaCost)) {
            return;
        }

        ThreadLocalRandom rng = ThreadLocalRandom.current();

        // Roll hit/miss based on accuracy.
        double roll = rng.nextDouble();
        if (roll > accuracy) {
            // Miss: no damage dealt.
            return;
        }

        // Base damage in [damageMin, damageMax].
        int baseDamage = damageMin;
        if (damageMax > damageMin) {
            baseDamage += rng.nextInt(damageMax - damageMin + 1);
        }

        // Critical hit check.
        double critRoll = rng.nextDouble();
        if (critRoll < critChance) {
            baseDamage = (int) Math.round(baseDamage * critMultiplier);
        }

        if (baseDamage <= 0) {
            return;
        }

        target.takeDamage(baseDamage);
        // Element effect & synergy are handled by the battle/element system later.
    }
}
