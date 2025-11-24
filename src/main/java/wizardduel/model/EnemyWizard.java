package wizardduel.model;

import wizardduel.model.Spell;

import java.util.List;

/**
 * AI-controlled wizard.
 */
public class EnemyWizard extends Wizard {

    public EnemyWizard(String name) {
        super(name);
    }

    public EnemyWizard(String name, List<Spell> initialSpells) {
        super(name, initialSpells);
    }

    // Čia gali būti AI strategijos, target pasirinkimas ir pan.
}
