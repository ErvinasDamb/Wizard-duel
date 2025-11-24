package wizardduel.model;

import wizardduel.model.Spell;

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

    // Čia gali likti player-specifiniai metodai (input, deck management, t.t.)
}
