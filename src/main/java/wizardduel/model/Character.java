package wizardduel.model;

import lombok.Getter;
import wizardduel.mechanics.effects.StatusEngine;
import wizardduel.model.effects.ElementEffect;
import wizardduel.model.effects.SynergyEffect;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.SynergyType;

import java.util.ArrayList;
import java.util.List;

/**
 * Core combat character: stats, resources, effects.
 * All effect logic is in ElementEngine.
 */
@Getter
public class Character {

    private final String name;

    // Primary resources
    private final int maxHp = 1000;
    private int hp = maxHp;

    private final int maxMana = 100;
    private int mana = 100; // start mana

    // Simple shield & base DoT (for spells)
    private int shield;
    private int dotDamagePerTurn;
    private int dotRemainingTurns;

    // Combat modifiers (used during spells)
    private double damageMultiplier = 1.0;
    private double accuracyModifier = 0.0;
    private double critChanceBonus = 0.0;
    private double shieldBonusMultiplier = 1.0;
    private double healReceivedMultiplier = 1.0;
    private double lifestealPercent = 0.0;

    // Active effects
    private final List<ElementEffect> activeElementEffects = new ArrayList<>();
    private final List<SynergyEffect> activeSynergyEffects = new ArrayList<>();

    public Character(String name) {
        this.name = name;
    }

    // ------------ Basic state ------------

    public boolean isAlive() {
        return hp > 0;
    }

    // ------------ Mana / HP / shield ------------

    public boolean useMana(int amount) {
        if (amount <= 0) return true;
        if (mana < amount) return false;
        mana -= amount;
        return true;
    }

    public void regenerateMana(int amount) {
        if (amount <= 0) return;
        mana = Math.min(maxMana, mana + amount);
    }

    public void takeDamage(int amount) {
        if (amount <= 0) return;

        int dmg = amount;

        if (shield > 0) {
            int absorbed = Math.min(shield, dmg);
            shield -= absorbed;
            dmg -= absorbed;
        }
        if (dmg <= 0) return;

        hp = Math.max(0, hp - dmg);
    }

    public void heal(int amount) {
        if (amount <= 0) return;
        if (healReceivedMultiplier <= 0.0) return;

        int effective = (int) Math.round(amount * healReceivedMultiplier);
        if (effective <= 0) return;

        hp = Math.min(maxHp, hp + effective);
    }

    public void applyShield(int amount) {
        if (amount <= 0) return;
        int effective = (int) Math.round(amount * shieldBonusMultiplier);
        if (effective <= 0) return;
        shield += effective;
    }

    public void applyDot(int damagePerTurn, int durationTurns) {
        if (damagePerTurn <= 0 || durationTurns <= 0) return;
        this.dotDamagePerTurn = damagePerTurn;
        this.dotRemainingTurns = durationTurns;
    }

    public void tickBaseDot() {
        if (dotRemainingTurns <= 0 || dotDamagePerTurn <= 0) return;
        takeDamage(dotDamagePerTurn);
        dotRemainingTurns--;
        if (dotRemainingTurns <= 0) {
            dotDamagePerTurn = 0;
        }
    }

    public void applyLifesteal(int damageDealt) {
        if (damageDealt <= 0 || lifestealPercent <= 0.0) return;
        int healAmount = (int) Math.round(damageDealt * lifestealPercent);
        if (healAmount > 0) heal(healAmount);
    }

    // ------------ Effects lists ------------

    public void addElementEffect(Element element, int durationTurns) {
        if (element == null || element == Element.NEUTRAL || durationTurns <= 0) return;

        for (ElementEffect effect : activeElementEffects) {
            if (effect.getElement() == element) {
                int newDuration = Math.max(effect.getRemainingTurns(), durationTurns);
                effect.setRemainingTurns(newDuration);
                return;
            }
        }
        activeElementEffects.add(new ElementEffect(element, durationTurns));
    }

    public void addSynergyEffect(SynergyType type, int durationTurns) {
        if (type == null || durationTurns <= 0) return;

        for (SynergyEffect effect : activeSynergyEffects) {
            if (effect.getType() == type) {
                int newDuration = Math.max(effect.getRemainingTurns(), durationTurns);
                effect.setRemainingTurns(newDuration);
                return;
            }
        }
        activeSynergyEffects.add(new SynergyEffect(type, durationTurns));
    }

    // ------------ Turn entry ------------

    /**
     * Kviesk šį metodą kiekvieno savo ėjimo pradžioj.
     * Tikras efektų taikymas daromas StatusEngine'e.
     */
    public void onTurnStart() {
        StatusEngine.processTurnStart(this);
    }

    // ------------ Helpers for StatusEngine ------------

    public void resetModifiers() {
        damageMultiplier = 1.0;
        accuracyModifier = 0.0;
        critChanceBonus = 0.0;
        shieldBonusMultiplier = 1.0;
        healReceivedMultiplier = 1.0;
        lifestealPercent = 0.0;
    }

    public void addDamageMultiplier(double delta) {
        damageMultiplier += delta;
    }

    public void addAccuracyModifier(double delta) {
        accuracyModifier += delta;
    }

    public void addCritChanceBonus(double delta) {
        critChanceBonus += delta;
    }

    public void addShieldBonusMultiplier(double delta) {
        shieldBonusMultiplier += delta;
    }

    public void multiplyHealReceived(double factor) {
        healReceivedMultiplier *= factor;
    }

    public void setHealReceivedMultiplier(double value) {
        healReceivedMultiplier = value;
    }

    public void addLifestealPercent(double delta) {
        lifestealPercent += delta;
    }

    public void leakMana(int amount) {
        if (amount <= 0 || mana <= 0) return;
        mana = Math.max(0, mana - amount);
    }

    /**
     * Short string with active element effects and remaining turns.
     */
    public String getElementEffectsSummary() {
        if (activeElementEffects.isEmpty()) {
            return "None";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < activeElementEffects.size(); i++) {
            ElementEffect e = activeElementEffects.get(i);
            sb.append(e.getElement().name())
                    .append("(")
                    .append(e.getRemainingTurns())
                    .append(")");
            if (i < activeElementEffects.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Short string with active synergy effects and remaining turns.
     */
    public String getSynergyEffectsSummary() {
        if (activeSynergyEffects.isEmpty()) {
            return "None";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < activeSynergyEffects.size(); i++) {
            SynergyEffect s = activeSynergyEffects.get(i);
            sb.append(s.getType().name())
                    .append("(")
                    .append(s.getRemainingTurns())
                    .append(")");
            if (i < activeSynergyEffects.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return name + " HP: " + hp + "/" + maxHp +
                " Mana: " + mana + "/" + maxMana +
                " Shield: " + shield;
    }
}
