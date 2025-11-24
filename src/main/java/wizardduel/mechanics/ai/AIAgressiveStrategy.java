package wizardduel.mechanics.ai;

import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;
import wizardduel.model.effects.ElementEffect;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.SpellType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hard AI: agresyvus, supranta synergy, finisherius ir debuffus.
 */
public class AIAgressiveStrategy implements AIStrategy {

    @Override
    public boolean shouldUsePotion(EnemyWizard self, PlayerWizard opponent) {
        return self.getHealth() < self.getMaxHealth() * 0.5;
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
        double bestScore = evaluateSpell(self, opponent, best, rng);

        for (int i = 1; i < candidates.size(); i++) {
            Spell s = candidates.get(i);
            double score = evaluateSpell(self, opponent, s, rng);
            if (score > bestScore) {
                best = s;
                bestScore = score;
            }
        }

        return best;
    }

    private double evaluateSpell(EnemyWizard self,
                                 PlayerWizard opponent,
                                 Spell s,
                                 Random rng) {

        double score = 0.0;

        // bazinis naudingumas
        score += s.getPower();
        score -= s.getManaCost() * 0.4;

        // tipų prioritetai
        if (s.getType() == SpellType.DAMAGE || s.getType() == SpellType.DOT) {
            score += 25;
        }
        if (s.getType() == SpellType.MANA_DRAIN && opponent.getMana() > 25) {
            score += 30;
        }
        if (s.getType() == SpellType.SHIELD && self.getHealth() < self.getMaxHealth() * 0.5) {
            score += 20;
        }

        // synergy bonus
        score += synergyBonusForSpell(opponent, s);

        // finisher logika
        if (opponent.getHealth() < opponent.getMaxHealth() * 0.35 &&
                (s.getType() == SpellType.DAMAGE || s.getType() == SpellType.DOT)) {
            score += 30;
        }

        // jei pats po ICE – vengia netikslių spelų
        if (hasElement(self, Element.ICE) && s.getAccuracy() < 0.8) {
            score -= 20;
        }

        // šiek tiek random
        score += rng.nextDouble() * 5.0;

        return score;
    }

    private boolean hasElement(EnemyWizard w, Element e) {
        for (ElementEffect effect : w.getActiveElementEffects()) {
            if (effect.getElement() == e) {
                return true;
            }
        }
        return false;
    }

    private boolean targetHasElement(PlayerWizard w, Element e) {
        for (ElementEffect effect : w.getActiveElementEffects()) {
            if (effect.getElement() == e) {
                return true;
            }
        }
        return false;
    }

    private double synergyBonusForSpell(PlayerWizard opponent, Spell s) {
        Element e = s.getElement();
        double bonus = 0.0;

        // FIRE + POISON
        if (e == Element.FIRE && targetHasElement(opponent, Element.POISON)) bonus += 70;
        if (e == Element.POISON && targetHasElement(opponent, Element.FIRE)) bonus += 70;

        // FIRE + BLOOD
        if (e == Element.FIRE && targetHasElement(opponent, Element.BLOOD)) bonus += 60;
        if (e == Element.BLOOD && targetHasElement(opponent, Element.FIRE)) bonus += 60;

        // ICE + ELECTRIC
        if (e == Element.ICE && targetHasElement(opponent, Element.ELECTRIC)) bonus += 50;
        if (e == Element.ELECTRIC && targetHasElement(opponent, Element.ICE)) bonus += 50;

        // BLOOD + POISON
        if (e == Element.BLOOD && targetHasElement(opponent, Element.POISON)) bonus += 65;
        if (e == Element.POISON && targetHasElement(opponent, Element.BLOOD)) bonus += 65;

        // ICE + POISON
        if (e == Element.ICE && targetHasElement(opponent, Element.POISON)) bonus += 40;
        if (e == Element.POISON && targetHasElement(opponent, Element.ICE)) bonus += 40;

        // ELECTRIC + WATER
        if (e == Element.ELECTRIC && targetHasElement(opponent, Element.WATER)) bonus += 55;
        if (e == Element.WATER && targetHasElement(opponent, Element.ELECTRIC)) bonus += 55;

        return bonus;
    }
}
