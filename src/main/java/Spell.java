public class Spell {

    public enum SpellType {
        DAMAGE,
        HEAL,
        SHIELD
        // Vėliau galėsi pridėti: BUFF, DEBUFF, POISON, SILENCE ir t.t.
    }

    public enum ElementType {
        FIRE,
        WATER,
        ICE,
        ELECTRIC,
        LIGHT,
        BLOOD,
        POISON,
        NEUTRAL
    }

    private String name;
    private SpellType type;
    private ElementType element;
    private int manaCost;
    private int power;

    // Cooldown (bendras ir likęs)
    private int cooldown;          // kiek ėjimų reikia laukti po panaudojimo
    private int remainingCooldown; // kiek ėjimų dar liko (0 = galima naudoti)
    public Spell(String name,
                 SpellType type,
                 ElementType element,
                 int manaCost,
                 int power,
                 int cooldown) {
        this.name = name;
        this.type = type;
        this.element = element;
        this.manaCost = manaCost;
        this.power = power;
        this.cooldown = Math.max(0, cooldown);
        this.remainingCooldown = 0; // iš pradžių visada paruoštas
    }

    public String getName() {
        return name;
    }

    public SpellType getType() {
        return type;
    }

    public ElementType getElement() {
        return element;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getPower() {
        return power;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getRemainingCooldown() {
        return remainingCooldown;
    }

    // Ar galima naudoti burtą šiuo ėjimu?
    public boolean isReady() {
        return remainingCooldown <= 0;
    }

    // Uždėti cooldown po panaudojimo
    public void putOnCooldown() {
        this.remainingCooldown = cooldown;
    }

    // Kviesti kiekvieno ėjimo pabaigoje, kad cooldown mažėtų
    public void tickCooldown() {
        if (remainingCooldown > 0) {
            remainingCooldown--;
        }
    }

    @Override
    public String toString() {
        String status = isReady()
                ? "READY"
                : "CD: " + remainingCooldown;
        return name +
                " (" + type +
                ", Element: " + element +
                ", Mana: " + manaCost +
                ", Power: " + power +
                ", " + status + ")";
    }
}
