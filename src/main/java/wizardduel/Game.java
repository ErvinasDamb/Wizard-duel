package wizardduel;

import wizardduel.factory.SpellFactory;
import wizardduel.mechanics.Battle;
import wizardduel.model.PlayerWizard;
import wizardduel.model.EnemyWizard;

/**
 * Entry point of the application.
 */
public class Game {
    public static void main(String[] args) {
        PlayerWizard player = new PlayerWizard("Player", SpellFactory.createDefaultPlayerDeck());
        EnemyWizard enemy = new EnemyWizard("Enemy", SpellFactory.createDefaultEnemyDeck());

        Battle battle = new Battle(player, enemy);
        battle.start();
    }
}

