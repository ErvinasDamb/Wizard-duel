package wizardduel.model;

import lombok.Getter;
import wizardduel.model.Spell;
import wizardduel.model.potions.Potion;

import java.util.ArrayList;
import java.util.List;

/**
 * Base wizard class with a spell deck.
 * HP, mana, shield, effects paveldimi iš Character.
 */
@Getter
public abstract class Wizard extends Character {

    protected final List<Spell> deck = new ArrayList<>();

    protected Wizard(String name) {
        super(name);
    }
    protected Potion equippedPotion;

    protected Wizard(String name, List<Spell> initialSpells) {
        super(name);
        if (initialSpells != null) {
            deck.addAll(initialSpells);
        }
    }

    public void equipPotion(Potion potion) {
        this.equippedPotion = potion;
    }

    public Potion getEquippedPotion() {
        return equippedPotion;
    }

    public boolean hasPotion() {
        return equippedPotion != null && equippedPotion.canUse();
    }

    public boolean useEquippedPotion() {
        if (!hasPotion()) {
            return false;
        }
        equippedPotion.use(this);
        return true;
    }

    public String getPotionSummary() {
        if (equippedPotion == null) {
            return "None";
        }
        return equippedPotion.getName() + " (" +
                equippedPotion.getRemainingUses() + "/" +
                equippedPotion.getMaxUses() + ")";
    }


    // Alias'ai jei senam kode dar naudojami šitie pavadinimai:
    public int getHealth() {
        return getHp();
    }

    public int getMaxHealth() {
        return getMaxHp();
    }

    public List<Spell> getSpells() {
        return deck;
    }
}
