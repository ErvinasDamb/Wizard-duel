package wizardduel.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for any wizard character.
 */
@Getter
@Setter
public abstract class Character {
    protected String name;
    protected int hp;
    protected int mana;
    protected Stats stats;
    protected int shield;             // absorbs next incoming damage
    protected int dotDamagePerTurn;   // simple DoT
    protected int dotRemainingTurns;  // how many turns DoT will tick


    /**
     * Tries to spend given amount of mana.
     * @return true if mana was spent, false if there was not enough.
     */
    public boolean useMana(int amount) {
        if (amount <= 0) {
            return true; // nieko nereikia, laikom kaip sėkmę
        }
        if (mana < amount) {
            return false;
        }
        mana -= amount;
        return true;
    }

    /**
     * Applies raw damage to this character (no shield/status yet).
     * HP cannot go below 0.
     */
    public void takeDamage(int amount) {
        if (amount <= 0) {
            return;
        }

        int dmg = amount;

        // first consume shield
        if (shield > 0) {
            int absorbed = Math.min(shield, dmg);
            shield -= absorbed;
            dmg -= absorbed;
        }

        if (dmg <= 0) {
            return;
        }

        hp = Math.max(0, hp - dmg);
    }

    /**
     * Adds shield value that will absorb future damage.
     */
    public void applyShield(int amount) {
        if (amount <= 0) {
            return;
        }
        shield += amount;
    }

    /**
     * Applies or overrides a simple damage-over-time effect.
     */
    public void applyDot(int damagePerTurn, int durationTurns) {
        if (damagePerTurn <= 0 || durationTurns <= 0) {
            return;
        }
        this.dotDamagePerTurn = damagePerTurn;
        this.dotRemainingTurns = durationTurns;
    }

    /**
     * Should be called at the start of this character's turn.
     * Processes DoT damage.
     */
    public void onTurnStart() {
        if (dotRemainingTurns > 0 && dotDamagePerTurn > 0) {
            hp = Math.max(0, hp - dotDamagePerTurn);
            dotRemainingTurns--;
            if (dotRemainingTurns == 0) {
                dotDamagePerTurn = 0;
            }
        }
    }



}

