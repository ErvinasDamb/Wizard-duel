package wizardduel.model;

import lombok.Getter;
import wizardduel.model.Spell;

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

    protected Wizard(String name, List<Spell> initialSpells) {
        super(name);
        if (initialSpells != null) {
            deck.addAll(initialSpells);
        }
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
