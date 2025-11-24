package wizardduel.mechanics.ai;

import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;
import wizardduel.model.enums.SpellType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Medium AI: kiek protingesnis – gina save, finishina jei gali.
 */
public class AIDefensiveStrategy implements AIStrategy {

    @Override
    public boolean shouldUsePotion(EnemyWizard self, PlayerWizard opponent) {
        // Jei HP < 40% – heal prioritetas
        return self.getHealth() < self.getMaxHealth() * 0.4;
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

        Spell best = candidates.get(0);
        double bestScore = evaluate(slf(self), opponent, best);

        for (int i = 1; i < candidates.size(); i++) {
            Spell s = candidates.get(i);
            double score = evaluate(slf(self), opponent, s);
            if (score > bestScore) {
                best = s;
                bestScore = score;
            }
        }

        return best;
    }

    private EnemyWizard slf(EnemyWizard self) {
        return self;
    }

    private double evaluate(EnemyWizard self, PlayerWizard opponent, Spell s) {
        double score = 0.0;

        // bazinis damage vs mana
        score += s.getPower();
        score -= s.getManaCost() * 0.5;

        // jei žaidėjo HP mažai – damage ir DOT svarbesni
        if (opponent.getHealth() < opponent.getMaxHealth() * 0.35 &&
                (s.getType() == SpellType.DAMAGE || s.getType() == SpellType.DOT)) {
            score += 25;
        }

        // jei AI HP mažai – skydas ar mana drain naudingi
        if (self.getHealth() < self.getMaxHealth() * 0.5) {
            if (s.getType() == SpellType.SHIELD) {
                score += 25;
            }
            if (s.getType() == SpellType.MANA_DRAIN && opponent.getMana() > 20) {
                score += 15;
            }
        }

        // šiek tiek random, kad ne visad tas pats
        score += Math.random() * 5.0;

        return score;
    }
}
