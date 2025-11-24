package wizardduel.mechanics.ai;

import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;

/**
 * AI: prioritizes survival (heals/shields).
 */
public class AIDefensiveStrategy implements AIStrategy {

    @Override
    public Spell chooseSpell(EnemyWizard enemy, PlayerWizard player) {
        return null;
    }
}
