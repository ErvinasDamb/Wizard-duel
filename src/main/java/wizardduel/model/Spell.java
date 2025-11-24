package wizardduel.model;

import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;

/**
 * Immutable spell definition with all stats needed for damage, control and elements.
 */
public interface Spell {

    /**
     * Executes this spell's effect from caster to target.
     * Tiksli logika bus konkrečiose spell klasėse.
     */
    void cast(Character caster, Character target);

    String getId();
    String getName();
    int getManaCost();
    int getPower();

    int getDamageMin();
    int getDamageMax();

    double getAccuracy();
    double getCritChance();
    double getCritMultiplier();

    int getDotAmount();
    int getDotDuration();
    double getStunChance();
    double getConfuseChance();
    double getSlowStrength();

    int getShieldValue();
    int getManaDrainAmount();
    double getChargeMultiplier();

    SpellType getType();
    Element getElement();

    /**
     * Chance to apply this spell's element as a status effect on the target/caster.
     */
    double getElementApplyChance();

    /**
     * How many turns the element effect should last if applied.
     */
    int getElementDuration();

    Rarity getRarity();
}
