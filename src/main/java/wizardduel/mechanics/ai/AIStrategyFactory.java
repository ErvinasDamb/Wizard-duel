package wizardduel.mechanics.ai;

import wizardduel.model.enums.AiDifficulty;

import static wizardduel.model.enums.AiDifficulty.EASY;
import static wizardduel.model.enums.AiDifficulty.MEDIUM;
import static wizardduel.model.enums.AiDifficulty.HARD;

/**
 * Parenka tinkamą AI strategiją pagal pasirinktą sunkumo lygį.
 */
public final class AIStrategyFactory {

    private AIStrategyFactory() {
        // utility class
    }

    public static AIStrategy create(AiDifficulty difficulty) {
        if (difficulty == null) {
            difficulty = AiDifficulty.EASY;
        }

        return switch (difficulty) {
            case EASY -> new AIRandomStrategy();
            case MEDIUM -> new AIDefensiveStrategy();
            case HARD -> new AIAgressiveStrategy();
        };
    }
}
