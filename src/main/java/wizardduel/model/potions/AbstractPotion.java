package wizardduel.model.potions;

import lombok.Getter;
import wizardduel.model.enums.PotionType;

@Getter
public abstract class AbstractPotion implements Potion {

    protected final String id;
    protected final String name;
    protected final PotionType type;
    protected final int maxUses;
    protected int remainingUses;

    protected AbstractPotion(String id, String name, PotionType type, int maxUses) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.maxUses = maxUses;
        this.remainingUses = maxUses;
    }

    @Override
    public int getRemainingUses() {
        return remainingUses;
    }

    protected void consumeOneUse() {
        if (remainingUses > 0) {
            remainingUses--;
        }
    }
}
