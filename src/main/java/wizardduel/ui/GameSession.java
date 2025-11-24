package wizardduel.ui;

import lombok.Getter;
import lombok.Setter;
import wizardduel.model.enums.AiDifficulty;
import wizardduel.model.Spell;
import wizardduel.model.potions.Potion;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameSession {

    private List<Spell> playerDeck = new ArrayList<>();
    private Potion selectedPotion;
    private AiDifficulty difficulty = AiDifficulty.EASY;

    public boolean hasValidDeck() {
        return playerDeck != null && playerDeck.size() == 7;
    }

    public void setDeck(List<Spell> deck) {
        this.playerDeck = new ArrayList<>(deck);
    }

    public void clear() {
        playerDeck.clear();
        selectedPotion = null;
        difficulty = AiDifficulty.EASY;
    }
}
