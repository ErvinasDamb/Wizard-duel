package wizardduel.mechanics.effects;

import wizardduel.model.Character;
import wizardduel.model.effects.ElementEffect;
import wizardduel.model.effects.SynergyEffect;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.SynergyType;

import java.util.Iterator;

/**
 * Applies all ongoing status effects at the start of the character's turn.
 */
public final class StatusEngine {

    private StatusEngine() {
    }

    public static void processTurnStart(Character c) {
        c.resetModifiers();
        c.tickBaseDot();
        applySoloElements(c);
        applySynergies(c);
        tickDurations(c);
    }

    /**
     * Solo element effects (FIRE, ICE, etc.).
     */
    private static void applySoloElements(Character c) {
        for (ElementEffect effect : c.getActiveElementEffects()) {
            switch (effect.getElement()) {
                case FIRE:
                    // ~3% HP per turn
                    c.takeDamage(30);
                    break;
                case WATER:
                    // Small mana regen
                    c.regenerateMana(8);
                    break;
                case ICE:
                    // Higher miss chance
                    c.addAccuracyModifier(-0.15);
                    break;
                case ELECTRIC:
                    // Crit chance up
                    c.addCritChanceBonus(0.15);
                    break;
                case LIGHT:
                    // Stronger shields
                    c.addShieldBonusMultiplier(0.3);
                    break;
                case BLOOD:
                    // More damage, small self damage
                    c.addDamageMultiplier(0.2);
                    c.takeDamage(25);
                    break;
                case POISON:
                    // Small dot + weaker heals
                    c.takeDamage(20);
                    c.multiplyHealReceived(0.5);
                    break;
                case NEUTRAL:
                default:
                    break;
            }
        }
    }

    /**
     * Synergy effects created from pairs of elements.
     */
    private static void applySynergies(Character c) {
        for (SynergyEffect effect : c.getActiveSynergyEffects()) {
            switch (effect.getType()) {
                case BLAZING_VENOM:
                    // Heavy DoT + big heal cut
                    c.takeDamage(60);
                    c.multiplyHealReceived(0.5);
                    break;
                case BLOODFIRE_FRENZY:
                    // Huge damage buff + self damage
                    c.addDamageMultiplier(0.4);
                    c.takeDamage(40);
                    break;
                case FROZEN_STATIC:
                    // Strong accuracy debuff
                    c.addAccuracyModifier(-0.3);
                    break;
                case CORRUPTED_VITALS:
                    // Heal block + chip damage
                    c.takeDamage(30);
                    c.setHealReceivedMultiplier(0.0);
                    break;
                case HOLY_SIPHON:
                    // Lifesteal on damage dealt
                    c.addLifestealPercent(0.3);
                    break;
                case TOXIC_FROSTBITE:
                    // Damage dealt reduced
                    c.addDamageMultiplier(-0.25);
                    break;
                case SHOCKING_CURRENT:
                    // Mana leak per turn
                    c.leakMana(12);
                    break;
            }
        }
    }

    /**
     * Ticks remaining duration and removes expired effects.
     */
    private static void tickDurations(Character c) {
        Iterator<ElementEffect> itEl = c.getActiveElementEffects().iterator();
        while (itEl.hasNext()) {
            ElementEffect e = itEl.next();
            e.setRemainingTurns(e.getRemainingTurns() - 1);
            if (e.getRemainingTurns() <= 0) {
                itEl.remove();
            }
        }

        Iterator<SynergyEffect> itSyn = c.getActiveSynergyEffects().iterator();
        while (itSyn.hasNext()) {
            SynergyEffect s = itSyn.next();
            s.setRemainingTurns(s.getRemainingTurns() - 1);
            if (s.getRemainingTurns() <= 0) {
                itSyn.remove();
            }
        }
    }
}
