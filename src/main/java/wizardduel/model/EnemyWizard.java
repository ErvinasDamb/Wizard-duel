package wizardduel.model;

import wizardduel.mechanics.ai.AIStrategy;


import java.util.List;

/**
 * AI-controlled wizard.
 */
public class EnemyWizard extends Wizard {

    public EnemyWizard(String name) {
        super(name);
    }
    private AIStrategy aiStrategy;

    public EnemyWizard(String name, List<Spell> initialSpells) {
        super(name, initialSpells);
    }

    public void setAiStrategy(AIStrategy aiStrategy) {
        this.aiStrategy = aiStrategy;
    }

    public AIStrategy getAiStrategy() {
        return aiStrategy;
    }

}
