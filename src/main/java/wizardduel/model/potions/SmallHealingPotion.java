package wizardduel.model.potions;

import wizardduel.model.Character;
import wizardduel.model.enums.PotionType;

public class SmallHealingPotion extends AbstractPotion {

    private final int healAmount;

    public SmallHealingPotion() {
        super("potion_small_heal_01", "Small Healing Potion", PotionType.SMALL_HEAL, 4);
        this.healAmount = 150; // 4x150 = 600 total heal
    }

    @Override
    public void use(Character user) {
        if (!canUse()) {
            System.out.println(user.getName() + " has no uses left for " + getName() + ".");
            return;
        }
        user.heal(healAmount);
        consumeOneUse();
        System.out.println(user.getName() + " uses " + getName() +
                " and restores " + healAmount + " HP (" + remainingUses + "/" + maxUses + " left).");
    }
}
