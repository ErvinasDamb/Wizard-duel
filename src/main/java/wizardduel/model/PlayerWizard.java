package wizardduel.model;

import wizardduel.model.Spell;

import java.util.ArrayList;
import java.util.List;

/**
 * Player-controlled wizard.
 */
public class PlayerWizard extends Wizard {

    public PlayerWizard(String name) {
        super(name);
    }

    public PlayerWizard(String name, List<Spell> initialSpells) {
        super(name, initialSpells);
    }

    public List<Spell> getHand() {
        int size = Math.min(3, deck.size());
        return new ArrayList<>(deck.subList(0, size));
    }
}
