package wizardduel.factory;

import wizardduel.model.Spell;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;
import wizardduel.model.spells.DamageSpell;
import wizardduel.model.spells.DotSpell;
import wizardduel.model.spells.ManaDrainSpell;
import wizardduel.model.spells.ShieldSpell;
import wizardduel.model.spells.ChargeSpell;



/**
 * Factory for creating predefined spells for decks and battles.
 * This is our creational design pattern usage.
 */
public final class SpellFactory {

    private SpellFactory() {
        // Utility class, no instances.
    }

    /**
     * Basic FIRE damage spell: mid cost, solid hit, decent accuracy.
     */
    public static Spell fireball() {
        return new DamageSpell(
                "fireball_common_01",
                "Fireball",
                5,              // manaCost
                10,             // power (AI heuristic)
                8,              // damageMin
                12,             // damageMax
                0.85,           // accuracy
                0.15,           // critChance
                1.7,            // critMultiplier
                SpellType.DAMAGE,
                Element.FIRE,
                0.30,           // elementApplyChance (30% to apply FIRE)
                2,              // elementDuration (2 turns)
                Rarity.COMMON
        );
    }

    /**
     * ICE shard: slightly lower damage, higher accuracy, applies ICE more reliably.
     * ICE solo effect: more miss chance for the target.
     */
    public static Spell iceShard() {
        return new DamageSpell(
                "ice_shard_common_01",
                "Ice Shard",
                4,              // manaCost
                8,              // power
                6,              // damageMin
                9,              // damageMax
                0.9,            // high accuracy
                0.10,           // critChance
                1.5,            // critMultiplier
                SpellType.DAMAGE,
                Element.ICE,
                0.4,            // higher chance to apply ICE
                2,              // elementDuration
                Rarity.COMMON
        );
    }

    /**
     * Poison bolt: weaker upfront damage, but enables POISON element.
     * POISON solo effect: slow DOT and heal reduction (will be handled in element system).
     */
    public static Spell poisonBolt() {
        return new DamageSpell(
                "poison_bolt_common_01",
                "Poison Bolt",
                4,              // manaCost
                7,              // power
                5,              // damageMin
                8,              // damageMax
                0.85,           // accuracy
                0.10,           // critChance
                1.5,            // critMultiplier
                SpellType.DAMAGE,
                Element.POISON,
                0.5,            // 50% to apply POISON
                3,              // elementDuration (longer poison)
                Rarity.COMMON
        );
    }

    /**
     * Basic magic shield spell: grants a moderate shield to the caster.
     */
    public static Spell magicShield() {
        return new ShieldSpell(
                "magic_shield_common_01",
                "Magic Shield",
                4,              // manaCost
                6,              // power
                10,             // shieldValue
                SpellType.SHIELD,
                Element.LIGHT,  // light buffina shield sinergijoj ateity
                0.3,            // elementApplyChance
                2,              // elementDuration
                Rarity.COMMON
        );
    }

    /**
     * Simple burning curse: applies a FIRE DoT effect.
     */
    public static Spell burningCurse() {
        return new DotSpell(
                "burning_curse_common_01",
                "Burning Curse",
                5,              // manaCost
                9,              // power
                4,              // dotAmount per turn
                2,              // dotDuration
                SpellType.DOT,
                Element.FIRE,
                0.35,           // elementApplyChance
                2,              // elementDuration
                Rarity.COMMON
        );
    }

    /**
     * Basic mana burn spell.
     */
    public static Spell manaDrain() {
        return new ManaDrainSpell(
                "mana_drain_common_01",
                "Mana Drain",
                5,              // manaCost
                8,              // power
                6,              // manaDrainAmount
                SpellType.MANADRAIN,
                Element.ELECTRIC,
                0.3,            // elementApplyChance
                2,              // elementDuration
                Rarity.COMMON
        );
    }

    public static Spell chargedStrike() {
        return new ChargeSpell(
                "charged_strike_common_01",
                "Charged Strike",
                6,              // manaCost
                12,             // power
                7,              // damageMin
                11,             // damageMax
                0.8,            // accuracy
                0.2,            // critChance
                1.8,            // critMultiplier
                1.8,            // chargeMultiplier (skalina žiauriai)
                Element.ELECTRIC,
                0.25,           // elementApplyChance
                2,              // elementDuration
                Rarity.RARE
        );
    }



}
