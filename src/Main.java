import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Sveikas atvykęs į Burtininko dvikovą!");
        System.out.print("Įvesk savo burtininko vardą: ");
        String name = scanner.nextLine();
        if (name == null || name.trim().isEmpty()) {
            name = "Žaidėjas";
        }

        // ====== Sukuriam burtus ŽAIDĖJUI ======
        Spell playerFireball = new Spell(
                "Fireball",
                Spell.SpellType.DAMAGE,
                Spell.ElementType.FIRE,
                20,   // mana kaina
                30,   // žala
                2     // cooldown
        );

        Spell playerLightning = new Spell(
                "Lightning Bolt",
                Spell.SpellType.DAMAGE,
                Spell.ElementType.ELECTRIC,
                15,
                20,
                1
        );

        Spell playerHeal = new Spell(
                "Heal",
                Spell.SpellType.HEAL,
                Spell.ElementType.LIGHT,
                15,
                10,
                1
        );

        Spell playerShield = new Spell(
                "Magic Shield",
                Spell.SpellType.SHIELD,
                Spell.ElementType.ICE,
                10,
                10,
                5
        );

        List<Spell> playerSpells = new ArrayList<>();
        playerSpells.add(playerFireball);
        playerSpells.add(playerLightning);
        playerSpells.add(playerHeal);
        playerSpells.add(playerShield);

        // ====== Sukuriam burtus PRIEŠUI (ATSIRĄ INSTANCAI!) ======
        Spell enemyFireball = new Spell(
                "Fireball",
                Spell.SpellType.DAMAGE,
                Spell.ElementType.FIRE,
                20,
                30,
                2
        );

        Spell enemyLightning = new Spell(
                "Lightning Bolt",
                Spell.SpellType.DAMAGE,
                Spell.ElementType.ELECTRIC,
                15,
                20,
                1
        );

        Spell enemyHeal = new Spell(
                "Heal",
                Spell.SpellType.HEAL,
                Spell.ElementType.LIGHT,
                15,
                10,
                1
        );

        Spell enemyShield = new Spell(
                "Magic Shield",
                Spell.SpellType.SHIELD,
                Spell.ElementType.ICE,
                10,
                10,
                5
        );

        List<Spell> enemySpells = new ArrayList<>();
        enemySpells.add(enemyFireball);
        enemySpells.add(enemyLightning);
        enemySpells.add(enemyHeal);
        enemySpells.add(enemyShield);

        // ====== Sukuriam burtininkus ======
        Wizard player = new Wizard(name, 100, 50, playerSpells);
        Wizard enemy = new Wizard("Dark Mage", 100, 50, enemySpells);

        // ====== Pradedam kovą ======
        ConsoleRenderer renderer = new ConsoleRenderer();
        Battle battle = new Battle(player, enemy, renderer);
        battle.start();

    }
}
