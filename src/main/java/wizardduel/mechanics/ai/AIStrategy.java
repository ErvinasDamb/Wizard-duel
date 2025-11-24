package wizardduel.mechanics.ai;

import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;

/**
 * Base AI decision-making interface.
 */
public interface AIStrategy {
    Spell chooseSpell(EnemyWizard enemy, PlayerWizard player);
}
