package wizardduel.mechanics.ai;

import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;

import java.util.List;
import java.util.Random;

public interface AIStrategy {

    /**
     * Should AI use its equipped potion this turn?
     * Battle jau tikrins ar potion realiai yra.
     */
    boolean shouldUsePotion(EnemyWizard self, PlayerWizard opponent);

    /**
     * Chooses which spell to cast from the current hand (top 3 deck cards).
     * @param self AI-controlled wizard
     * @param opponent player wizard
     * @param hand top 3 active spells AI can consider
     * @param rng randomness source
     * @return chosen spell, or null if AI prefers to skip
     */
    Spell chooseSpell(EnemyWizard self,
                      PlayerWizard opponent,
                      List<Spell> hand,
                      Random rng);
}
