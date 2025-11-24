public class ConsoleRenderer {

    public void printBattleIntro(Wizard player, Wizard enemy) {
        System.out.println("=== Burtininko dvikova ===");
        System.out.println(player.getName() + " vs " + enemy.getName());
        System.out.println();
    }

    public void printRoundHeader(int roundNumber) {
        System.out.println("\n================================");
        System.out.println("           RAUNDAS " + roundNumber);
        System.out.println("================================");
    }

    public void printBattleStatus(Wizard player, Wizard enemy) {
        System.out.println("TAVO BŪSENA:");
        player.printStatus();
        System.out.println("PRIEŠO BŪSENA:");
        enemy.printStatus();
        System.out.println();
    }

    public void printEndOfRound(Wizard player, Wizard enemy, int manaRegen) {
        System.out.println("\n--- Raundo pabaiga ---");
        System.out.println(player.getName() + " atgauna " + manaRegen
                + " manos. Mana: " + player.getMana() + "/" + player.getMaxMana());
        System.out.println(enemy.getName() + " atgauna " + manaRegen
                + " manos. Mana: " + enemy.getMana() + "/" + enemy.getMaxMana());

        System.out.println("\nAtnaujinta būsena po raundo:");
        printBattleStatus(player, enemy);

        System.out.println("Paspausk Enter, kad tęstum...");
        try {
            System.in.read();
        } catch (Exception e) {
            // V1 – dzin, ignoruojam
        }
    }
}
