import java.util.List;

public class Wizard {

    private String name;
    private int maxHealth;
    private int health;
    private int maxMana;
    private int mana;
    private int shield;
    private List<Spell> spells;

    public Wizard(String name, int maxHealth, int maxMana, List<Spell> spells) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.maxMana = maxMana;
        this.mana = maxMana;
        this.shield = 0;
        this.spells = spells;
    }

    // --- Getteriai ---

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getShield() {
        return shield;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    /**
     * V1: grąžina visus burtus.
     * V2: čia galėsim gražinti tik 3 aktyvius iš 7 ciklo.
     */
    public List<Spell> getAvailableSpells() {
        return spells;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // --- Veiksmai su resursais ---

    // Bando išleisti maną. Grąžina true, jei pavyko.
    public boolean spendMana(int amount) {
        if (amount > mana) {
            return false;
        }
        mana -= amount;
        return true;
    }

    // Po ėjimo atsistato šiek tiek manas
    public void regenerateMana(int amount) {
        if (amount <= 0) return;
        mana = Math.min(maxMana, mana + amount);
    }

    // Gauna žalos (pirma sudeginam skydą, po to gyvybes)
    public void takeDamage(int amount) {
        if (amount <= 0) return;

        if (shield > 0) {
            int absorbed = Math.min(shield, amount);
            shield -= absorbed;
            amount -= absorbed;
        }

        if (amount > 0) {
            health -= amount;
            if (health < 0) {
                health = 0;
            }
        }
    }

    // Gydosi
    public void heal(int amount) {
        if (amount <= 0) return;
        health = Math.min(maxHealth, health + amount);
    }

    // Prideda skydo
    public void addShield(int amount) {
        if (amount <= 0) return;
        shield += amount;
    }

    // --- Cooldown'ų tvarkymas visiems burtams ---

    public void tickAllSpellsCooldown() {
        for (Spell spell : spells) {
            spell.tickCooldown();
        }
    }

    // --- Statuso atvaizdavimas konsolėje ---

    public void printStatus() {
        System.out.println(
                name + ": HP " + health + "/" + maxHealth +
                        ", Mana " + mana + "/" + maxMana +
                        ", Shield " + shield
        );
    }

    /**
     * Ar burtininkas DABAR gali panaudoti šį burtą:
     * - jis nėra ant cooldown
     * - turi pakankamai manos
     */
    public boolean canCast(Spell spell) {
        if (spell == null) return false;
        if (!spell.isReady()) return false;
        return spell.getManaCost() <= mana;
    }



}
