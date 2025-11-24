package wizardduel.mechanics.ai;

import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Easy AI: random from playable spells, potion tik kai labai mažai HP.
 */
public class AIRandomStrategy implements AIStrategy {

    @Override
    public boolean shouldUsePotion(EnemyWizard self, PlayerWizard opponent) {
        // Naudoja potion tik kai labai mažai HP
        return self.getHealth() < self.getMaxHealth() * 0.3;
    }

    @Override
    public Spell chooseSpell(EnemyWizard self,
                             PlayerWizard opponent,
                             List<Spell> hand,
                             Random rng) {

        List<Spell> candidates = new ArrayList<>();
        for (Spell s : hand) {
            if (!s.isActive()) continue;
            if (s.getManaCost() > self.getMana()) continue;
            candidates.add(s);
        }

        if (candidates.isEmpty()) {
            return null;
        }

        int idx = rng.nextInt(candidates.size());
        return candidates.get(idx);
    }
}
