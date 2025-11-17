import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {

    private static final int BASE_MANA_REGEN = 5;
    private static final int SKIP_MANA_BONUS = 10;

    private Wizard player;
    private Wizard enemy;
    private Random rng = new Random();
    private Scanner scanner = new Scanner(System.in);

    // NAUJA:
    private ConsoleRenderer renderer;
    private int roundNumber = 1;

    public Battle(Wizard player, Wizard enemy, ConsoleRenderer renderer) {
        this.player = player;
        this.enemy = enemy;
        this.renderer = renderer;
    }

    public void start() {
        renderer.printBattleIntro(player, enemy);

        while (player.isAlive() && enemy.isAlive()) {

            renderer.printRoundHeader(roundNumber);
            renderer.printBattleStatus(player, enemy);

            // Žaidėjo ėjimas
            playerTurn();
            if (!enemy.isAlive()) break;

            // Priešo ėjimas
            enemyTurn();
            if (!player.isAlive()) break;

            // Raundo pabaiga
            endOfRound();

            roundNumber++;
        }

        System.out.println("\n=== Kova baigėsi ===");
        player.printStatus();
        enemy.printStatus();

        if (player.isAlive() && !enemy.isAlive()) {
            System.out.println(player.getName() + " laimėjo!");
        } else if (!player.isAlive() && enemy.isAlive()) {
            System.out.println(enemy.getName() + " laimėjo!");
        } else {
            System.out.println("Lygiosios (abu krito)!");
        }
    }


    private void playerTurn() {
        System.out.println("\nTavo ėjimas. Pasirink burtą:");

        List<Spell> spells = player.getAvailableSpells();

        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);

            String extra = "";
            if (!s.isReady()) {
                extra = " [COOLDOWN " + s.getRemainingCooldown() + "]";
            } else if (s.getManaCost() > player.getMana()) {
                extra = " [NEPAKANKA MANOS]";
            }

            System.out.println((i + 1) + ". " + s + extra);
        }

        System.out.println("0. Praleisti ėjimą (+10 manos)");

        Spell chosenSpell = null;

        while (true) {
            System.out.print("Įvesk burto numerį (arba 0 praleisti): ");

            if (!scanner.hasNextInt()) {
                scanner.next();
                System.out.println("Įvesk skaičių.");
                continue;
            }

            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println(player.getName() + " praleidžia ėjimą ir taupo maną.");
                player.regenerateMana(SKIP_MANA_BONUS);
                System.out.println(player.getName() + " atgauna papildomai "
                        + SKIP_MANA_BONUS + " manos už praleistą ėjimą.");
                return;
            }

            int index = choice - 1;

            if (index < 0 || index >= spells.size()) {
                System.out.println("Neteisingas numeris.");
                continue;
            }

            Spell selected = spells.get(index);

            if (!player.canCast(selected)) {
                System.out.println("Šio burto negalima panaudoti dabar (cooldown arba nepakanka manos).");
                continue;
            }

            chosenSpell = selected;
            break;
        }

        System.out.println(player.getName() + " pasirenka burtą: " + chosenSpell.getName());
        castSpell(player, enemy, chosenSpell);
    }



    private void enemyTurn() {
        System.out.println("\nPriešininko ėjimas...");

        Spell chosen = chooseEnemySpell();

        if (chosen == null) {
            System.out.println(enemy.getName() + " nenaudoja jokio burto šiame ėjime (praleidžia ėjimą).");
            enemy.regenerateMana(SKIP_MANA_BONUS);
            System.out.println(enemy.getName() + " atgauna papildomai " +
                    SKIP_MANA_BONUS + " manos už praleistą ėjimą.");
            return;
        }

        System.out.println(enemy.getName() + " naudoja: " + chosen.getName());
        castSpell(enemy, player, chosen);
    }


    private Spell chooseEnemySpell() {
        // NAUJA: naudojam getAvailableSpells
        List<Spell> spells = enemy.getAvailableSpells();

        Spell bestDamage = null;
        Spell bestHeal = null;
        Spell bestShield = null;

        for (Spell s : spells) {
            // NAUJA: viena vieta tikrinimui
            if (!enemy.canCast(s)) {
                continue;
            }

            switch (s.getType()) {
                case DAMAGE:
                    if (bestDamage == null || s.getPower() > bestDamage.getPower()) {
                        bestDamage = s;
                    }
                    break;
                case HEAL:
                    if (bestHeal == null || s.getPower() > bestHeal.getPower()) {
                        bestHeal = s;
                    }
                    break;
                case SHIELD:
                    if (bestShield == null || s.getPower() > bestShield.getPower()) {
                        bestShield = s;
                    }
                    break;
            }
        }

        // Paprasta "AI" logika:
        // - Jei mažai HP, labiau nori HEAL
        // - Jei žaidėjas beveik miręs, nori DAMAGE
        // - Kitu atveju – DAMAGE > SHIELD > HEAL > nieko

        if (enemy.getHealth() < enemy.getMaxHealth() / 2 && bestHeal != null) {
            return bestHeal;
        }

        if (player.getHealth() < 30 && bestDamage != null) {
            return bestDamage;
        }

        if (bestDamage != null) return bestDamage;
        if (bestShield != null) return bestShield;
        if (bestHeal != null) return bestHeal;

        // Jei niekas netinka (cooldown / mana) – grįžtam null (AI skipins ir gaus maną)
        return null;
    }


    private void castSpell(Wizard caster, Wizard target, Spell spell) {
        // Pirma – mana
        boolean manaOk = caster.spendMana(spell.getManaCost());
        if (!manaOk) {
            System.out.println(caster.getName() + " neturi pakankamai manos burtui " +
                    spell.getName() + "!");
            return;
        }

        // Tada efektas
        switch (spell.getType()) {
            case DAMAGE:
                int damage = spell.getPower();
                // Čia vėliau galim pridėti sinergijas, elemental bonus ir t.t.
                System.out.println(caster.getName() + " daro " + damage +
                        " žalos " + target.getName() + "!");
                target.takeDamage(damage);
                break;

            case HEAL:
                int healAmount = spell.getPower();
                System.out.println(caster.getName() + " gydo save " +
                        healAmount + " HP.");
                caster.heal(healAmount);
                break;

            case SHIELD:
                int shieldAmount = spell.getPower();
                System.out.println(caster.getName() + " sukuria skydą (" +
                        shieldAmount + ").");
                caster.addShield(shieldAmount);
                break;
        }

        // Uždėti cooldown
        spell.putOnCooldown();
    }

    private void endOfRound() {
        // Cooldown tick
        player.tickAllSpellsCooldown();
        enemy.tickAllSpellsCooldown();

        // Mana regen
        player.regenerateMana(BASE_MANA_REGEN);
        enemy.regenerateMana(BASE_MANA_REGEN);

        // Vaizdavimas
        renderer.printEndOfRound(player, enemy, BASE_MANA_REGEN);
    }
}
