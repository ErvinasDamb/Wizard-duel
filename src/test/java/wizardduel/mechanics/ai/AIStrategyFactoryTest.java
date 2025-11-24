package wizardduel.mechanics.ai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import wizardduel.model.enums.AiDifficulty;

class AIStrategyFactoryTest {

    @Test
    void factoryReturnsCorrectStrategyForEachDifficulty() {
        AIStrategy easy = AIStrategyFactory.create(AiDifficulty.EASY);
        AIStrategy normal = AIStrategyFactory.create(AiDifficulty.MEDIUM);
        AIStrategy hard = AIStrategyFactory.create(AiDifficulty.HARD);

        assertTrue(easy instanceof AIRandomStrategy,
                "EASY difficulty should use AIRandomStrategy");
        assertTrue(normal instanceof AIDefensiveStrategy,
                "NORMAL difficulty should use AIDefensiveStrategy");
        assertTrue(hard instanceof AIAgressiveStrategy,
                "HARD difficulty should use AIAgressiveStrategy");
    }
}
