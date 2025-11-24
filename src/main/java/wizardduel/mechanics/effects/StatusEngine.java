package wizardduel.mechanics.effects;

import wizardduel.model.Character;
import wizardduel.model.effects.ElementEffect;
import wizardduel.model.effects.SynergyEffect;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.SynergyType;

import java.util.Iterator;

/**
 * Tvarko visus ongoing efektus ėjimo pradžioje.
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

    // --- SOLO ELEMENT EFFECTS ---

    private static void applySoloElements(Character c) {
        for (ElementEffect effect : c.getActiveElementEffects()) {
            Element e = effect.getElement();
            switch (e) {
                case FIRE:
                    c.takeDamage(30);               // ~3% HP
                    break;
                case WATER:
                    c.regenerateMana(8);
                    break;
                case ICE:
                    c.addAccuracyModifier(-0.15);
                    break;
                case ELECTRIC:
                    c.addCritChanceBonus(0.15);
                    break;
                case LIGHT:
                    c.addShieldBonusMultiplier(0.3);
                    break;
                case BLOOD:
                    c.addDamageMultiplier(0.2);
                    c.takeDamage(25);
                    break;
                case POISON:
                    c.takeDamage(20);
                    c.multiplyHealReceived(0.5);
                    break;
                case NEUTRAL:
                default:
                    break;
            }
        }
    }

    // --- SYNERGY EFFECTS ---

    private static void applySynergies(Character c) {
        for (SynergyEffect effect : c.getActiveSynergyEffects()) {
            SynergyType type = effect.getType();
            switch (type) {
                case BLAZING_VENOM:
                    c.takeDamage(60);
                    c.multiplyHealReceived(0.5);
                    break;
                case BLOODFIRE_FRENZY:
                    c.addDamageMultiplier(0.4);
                    c.takeDamage(40);
                    break;
                case FROZEN_STATIC:
                    c.addAccuracyModifier(-0.3);
                    break;
                case CORRUPTED_VITALS:
                    c.takeDamage(30);
                    c.setHealReceivedMultiplier(0.0);
                    break;
                case HOLY_SIPHON:
                    c.addLifestealPercent(0.3);
                    break;
                case TOXIC_FROSTBITE:
                    c.addDamageMultiplier(-0.25);
                    break;
                case SHOCKING_CURRENT:
                    c.leakMana(12);
                    break;
            }
        }
    }

    // --- DURATION TICK & CLEANUP ---

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
