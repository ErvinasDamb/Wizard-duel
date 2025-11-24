package wizardduel.model.potions;

import wizardduel.model.Character;
import wizardduel.model.enums.PotionType;

public interface Potion {

    String getId();
    String getName();
    PotionType getType();

    /**
     * How many uses are left for this potion.
     */
    int getRemainingUses();

    int getMaxUses();

    /**
     * @return true if this potion can still be used.
     */
    default boolean canUse() {
        return getRemainingUses() > 0;
    }

    /**
     * Applies the potion effect to the user.
     * Implementations should also decrease remaining uses.
     */
    void use(Character user);
}
