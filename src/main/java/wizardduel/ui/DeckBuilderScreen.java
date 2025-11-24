package wizardduel.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wizardduel.factory.SpellFactory;
import wizardduel.factory.PotionFactory;
import wizardduel.model.Spell;
import wizardduel.model.enums.PotionType;
import wizardduel.model.potions.Potion;
import wizardduel.ui.components.PotionCardView;
import wizardduel.ui.components.SpellCardView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeckBuilderScreen {

    private static final int DECK_SIZE = 7;

    public static Scene create(Stage stage, GameSession session) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label title = new Label("Create Your Deck");
        title.getStyleClass().add("title-label");

        // ===== SPELL LISTAI =====
        // Vietoj SpellRepository – naudojam tavo SpellFactory
        List<Spell> allSpells = SpellFactory.createSpellPool();

        ListView<Spell> availableList = new ListView<>();
        availableList.setItems(FXCollections.observableArrayList(allSpells));
        availableList.setCellFactory(list -> new SpellCell());
        // daugiau vietos kortoms
        availableList.setPrefHeight(380);

        ListView<Spell> deckList = new ListView<>();
        deckList.setItems(FXCollections.observableArrayList(session.getPlayerDeck()));
        deckList.setCellFactory(list -> new SpellCell());
        // DECK JUOSTA: horizontali per visą plotį
        deckList.setOrientation(Orientation.HORIZONTAL);
        deckList.setPrefHeight(180);
        deckList.setMinHeight(180);

        setupDragAndDrop(availableList, deckList);

        // Tik available – eina į center-left
        VBox availableBox = new VBox(5,
                new Label("Available Spells"),
                availableList
        );
        availableBox.setPadding(new Insets(10));

        // Deck – atskirai, viršuje per visą app plotį
        VBox deckBox = new VBox(5,
                new Label("Your Deck (7 unique spells)"),
                deckList
        );
        deckBox.setPadding(new Insets(10));

        // ===== POTION UI =====
        PotionCardView smallPotionView = new PotionCardView(
                PotionType.SMALL_HEAL,
                "Small Healing Potion",
                150,
                4
        );
        PotionCardView healPotionView = new PotionCardView(
                PotionType.HEAL,
                "Healing Potion",
                220,
                3
        );
        PotionCardView largePotionView = new PotionCardView(
                PotionType.LARGE_HEAL,
                "Large Healing Potion",
                350,
                2
        );

        List<PotionCardView> potionViews = List.of(
                smallPotionView,
                healPotionView,
                largePotionView
        );

        for (PotionCardView view : potionViews) {
            view.setOnMouseClicked(e -> {
                for (PotionCardView v : potionViews) {
                    v.setSelected(false);
                }
                view.setSelected(true);

                Potion p = createPotionForType(view.getType());
                session.setSelectedPotion(p);
            });
        }

        VBox potionBox = new VBox(10,
                new Label("Choose a Potion"),
                smallPotionView,
                healPotionView,
                largePotionView
        );
        potionBox.setPadding(new Insets(10));

        // ===== APATINIAI MYGTUKAI =====
        Button cancelButton = new Button("Cancel");
        Button confirmButton = new Button("Confirm");

        cancelButton.setOnAction(e -> stage.setScene(TitleScreen.create(stage)));

        confirmButton.setOnAction(e -> {
            List<Spell> selectedPreviewSpells = new ArrayList<>(deckList.getItems());
            if (selectedPreviewSpells.size() != DECK_SIZE || !areUnique(selectedPreviewSpells)) {
                // jei norėsi – pridėsim error label / alert
                return;
            }

            // Konvertuojam preview Spells į tikrus instancus per ID
            List<Spell> realDeck = new ArrayList<>();
            for (Spell preview : selectedPreviewSpells) {
                realDeck.add(SpellFactory.createById(preview.getId()));
            }

            session.setDeck(realDeck);
            stage.setScene(TitleScreen.create(stage));
        });

        HBox bottomBox = new HBox(10, cancelButton, confirmButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));

        // ===== LAYOUT =====
        // Viršuj: Title + Deck juosta per visą plotį
        VBox topBox = new VBox(10, title, deckBox);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10, 0, 10, 0));

        // Centre: kairė – available, dešinė – potionai
        HBox centerBox = new HBox(20, availableBox, potionBox);
        centerBox.setPadding(new Insets(10));

        root.setTop(topBox);
        root.setCenter(centerBox);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(
                DeckBuilderScreen.class.getResource("/styles.css").toExternalForm()
        );

        return scene;
    }

    private static void setupDragAndDrop(ListView<Spell> availableList,
                                         ListView<Spell> deckList) {

        availableList.setOnDragDetected(event -> {
            Spell selected = availableList.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            var db = availableList.startDragAndDrop(TransferMode.MOVE);
            var content = new javafx.scene.input.ClipboardContent();
            content.putString(selected.getName());
            db.setContent(content);
            event.consume();
        });

        deckList.setOnDragOver(event -> {
            if (event.getGestureSource() != deckList &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        deckList.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String spellName = db.getString();
                Spell dragged = availableList.getItems()
                        .stream()
                        .filter(s -> s.getName().equals(spellName))
                        .findFirst()
                        .orElse(null);
                if (dragged != null) {
                    if (!deckList.getItems().contains(dragged)
                            && deckList.getItems().size() < DECK_SIZE) {

                        // Įdedam į decką
                        deckList.getItems().add(dragged);
                        // Pašalinam iš bendro listo
                        availableList.getItems().remove(dragged);

                        success = true;
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // Dvigubas click ant deck kortos – išmetam iš deck ir grąžinam į available
        deckList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Spell selected = deckList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    deckList.getItems().remove(selected);
                    // grąžinam į available, jeigu dar nėra
                    if (!availableList.getItems().contains(selected)) {
                        availableList.getItems().add(selected);
                    }
                }
            }
        });
    }

    private static boolean areUnique(List<Spell> deck) {
        Set<String> ids = new HashSet<>();
        for (Spell s : deck) {
            if (!ids.add(s.getId())) {
                return false;
            }
        }
        return true;
    }

    private static class SpellCell extends ListCell<Spell> {
        @Override
        protected void updateItem(Spell spell, boolean empty) {
            super.updateItem(spell, empty);
            if (empty || spell == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new SpellCardView(spell));
            }
        }
    }

    private static Potion createPotionForType(PotionType type) {
        return switch (type) {
            case SMALL_HEAL -> PotionFactory.createSmallHealingPotion();
            case HEAL -> PotionFactory.createHealingPotion();
            case LARGE_HEAL -> PotionFactory.createLargeHealingPotion();
        };
    }

}
