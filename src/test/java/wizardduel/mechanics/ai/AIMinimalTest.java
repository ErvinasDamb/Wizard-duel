package wizardduel.mechanics.ai;

import org.junit.jupiter.api.Test;
import wizardduel.factory.SpellFactory;
import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;

import static org.junit.jupiter.api.Assertions.*;

class AIMinimalTest {

    @Test
    void randomStrategyUsesPotionWhenLowHp() {
        EnemyWizard enemy = new EnemyWizard("Enemy", SpellFactory.createDefaultEnemyDeck());
        PlayerWizard player = new PlayerWizard("Player", SpellFactory.createDefaultPlayerDeck());

        // numušam enemy HP
        enemy.takeDamage(800); // 1000 -> 200 (20%)

        AIStrategy strategy = new AIRandomStrategy();
        assertTrue(strategy.shouldUsePotion(enemy, player),
                "AI should want to use potion when HP is very low");
    }
}
