package wizardduel.model.spells;

import lombok.Getter;
import wizardduel.model.Spell;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;

/**
 * Base immutable implementation for all spells.
 * Holds all shared numeric stats and metadata.
 */
@Getter
public abstract class AbstractSpell implements Spell {

    protected final String id;
    protected final String name;
    protected final int manaCost;
    protected final int power;

    protected final int damageMin;
    protected final int damageMax;

    protected final double accuracy;
    protected final double critChance;
    protected final double critMultiplier;

    protected final int dotAmount;
    protected final int dotDuration;
    protected final double stunChance;
    protected final double confuseChance;
    protected final double slowStrength;

    protected final int shieldValue;
    protected final int manaDrainAmount;
    protected final double chargeMultiplier;

    protected final SpellType type;
    protected final Element element;

    protected final double elementApplyChance;
    protected final int elementDuration;

    protected final Rarity rarity;

    /**
     * Full spell definition constructor.
     */
    protected AbstractSpell(
            String id,
            String name,
            int manaCost,
            int power,
            int damageMin,
            int damageMax,
            double accuracy,
            double critChance,
            double critMultiplier,
            int dotAmount,
            int dotDuration,
            double stunChance,
            double confuseChance,
            double slowStrength,
            int shieldValue,
            int manaDrainAmount,
            double chargeMultiplier,
            SpellType type,
            Element element,
            double elementApplyChance,
            int elementDuration,
            Rarity rarity
    ) {
        this.id = id;
        this.name = name;
        this.manaCost = manaCost;
        this.power = power;
        this.damageMin = damageMin;
        this.damageMax = damageMax;
        this.accuracy = accuracy;
        this.critChance = critChance;
        this.critMultiplier = critMultiplier;
        this.dotAmount = dotAmount;
        this.dotDuration = dotDuration;
        this.stunChance = stunChance;
        this.confuseChance = confuseChance;
        this.slowStrength = slowStrength;
        this.shieldValue = shieldValue;
        this.manaDrainAmount = manaDrainAmount;
        this.chargeMultiplier = chargeMultiplier;
        this.type = type;
        this.element = element;
        this.elementApplyChance = elementApplyChance;
        this.elementDuration = elementDuration;
        this.rarity = rarity;
    }
}
