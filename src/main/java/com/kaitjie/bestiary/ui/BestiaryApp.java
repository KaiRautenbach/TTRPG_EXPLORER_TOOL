package com.kaitjie.bestiary.ui;

import com.kaitjie.bestiary.api.ConnectivityChecker;
import com.kaitjie.bestiary.api.DndApiClient;
import com.kaitjie.bestiary.db.DatabaseManager;
import com.kaitjie.bestiary.db.MonsterDao;
import com.kaitjie.bestiary.model.*;
import com.kaitjie.bestiary.sync.SyncService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class BestiaryApp extends Application {

    private DatabaseManager dbManager;
    private MonsterDao monsterDao;
    private SyncService syncService;

    private final ObservableList<MonsterSummary> masterData = FXCollections.observableArrayList();
    private ListView<MonsterSummary> listView;
    private TextArea detailArea;
    private Label statusLabel;
    private GridPane abilityScoreGrid;

    @Override
    public void init() throws Exception {
        // Runs off the JavaFX UI thread — safe place for DB/setup work
        dbManager = new DatabaseManager();
        monsterDao = new MonsterDao(dbManager);
        DndApiClient apiClient = new DndApiClient();
        ConnectivityChecker connectivityChecker = new ConnectivityChecker();
        syncService = new SyncService(apiClient, monsterDao, connectivityChecker);

        masterData.addAll(monsterDao.getAllMonsterSummaries());
    }

    @Override
    public void start(Stage stage) {
        TextField searchField = new TextField();
        searchField.setPromptText("Search monsters by name...");

        Button syncButton = new Button("Sync from API");
        statusLabel = new Label(masterData.size() + " monsters loaded.");

        HBox topBar = new HBox(10, searchField, syncButton, statusLabel);
        topBar.setPadding(new Insets(10));
        HBox.setHgrow(searchField, Priority.ALWAYS);

        FilteredList<MonsterSummary> filteredData = new FilteredList<>(masterData, s -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String query = newVal == null ? "" : newVal.trim().toLowerCase();
            filteredData.setPredicate(summary ->
                    query.isEmpty()
                            || (summary.getName() != null && summary.getName().toLowerCase().contains(query))
                            || (summary.getIndex() != null && summary.getIndex().toLowerCase().contains(query))
            );
        });

        listView = new ListView<>(filteredData);
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(MonsterSummary item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showMonsterDetail(newVal.getIndex());
            }
        });

        detailArea = new TextArea();
        detailArea.setEditable(false);
        detailArea.setWrapText(true);
        detailArea.setFont(Font.font("Monospaced", 13));
        detailArea.setText("Select a monster to view its stat block.");
        VBox.setVgrow(detailArea, Priority.ALWAYS);

        abilityScoreGrid = buildAbilityScoreGrid(null);

        VBox detailPane = new VBox(10, abilityScoreGrid, detailArea);
        detailPane.setPadding(new Insets(10));

        SplitPane splitPane = new SplitPane(listView, detailPane);
        splitPane.setDividerPositions(0.3);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(splitPane);

        syncButton.setOnAction(e -> runSync());

        Scene scene = new Scene(root, 1000, 650);
        stage.setTitle("Pocket Bestiary");
        stage.setScene(scene);
        stage.show();
    }

    private void showMonsterDetail(String index) {
        try {
            Monster monster = monsterDao.getMonsterByIndex(index);
            if (monster == null) {
                detailArea.setText("Not found.");
                return;
            }

            detailArea.setText(buildStatBlockText(monster));

            GridPane newGrid = buildAbilityScoreGrid(monster);
            VBox parent = (VBox) abilityScoreGrid.getParent();
            int idx = parent.getChildren().indexOf(abilityScoreGrid);
            parent.getChildren().set(idx, newGrid);
            abilityScoreGrid = newGrid;

        } catch (Exception e) {
            detailArea.setText("Error loading monster: " + e.getMessage());
        }
    }

    private GridPane buildAbilityScoreGrid(Monster m) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(4);

        String[] labels = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};
        int[] scores = m == null
                ? new int[]{0, 0, 0, 0, 0, 0}
                : new int[]{
                m.getStrength(), m.getDexterity(), m.getConstitution(),
                m.getIntelligence(), m.getWisdom(), m.getCharisma()
        };

        for (int col = 0; col < labels.length; col++) {
            Label nameLabel = new Label(labels[col]);
            nameLabel.setStyle("-fx-font-weight: bold;");
            nameLabel.setAlignment(Pos.CENTER);
            nameLabel.setMaxWidth(Double.MAX_VALUE);

            int score = scores[col];
            int modifier = Math.floorDiv(score - 10, 2);
            String modText = (modifier >= 0 ? "+" : "") + modifier;

            Label valueLabel = new Label(m == null ? "-" : score + " (" + modText + ")");
            valueLabel.setAlignment(Pos.CENTER);
            valueLabel.setMaxWidth(Double.MAX_VALUE);

            grid.add(nameLabel, col, 0);
            grid.add(valueLabel, col, 1);

            GridPane.setHalignment(nameLabel, HPos.CENTER);
            GridPane.setHalignment(valueLabel, HPos.CENTER);

            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / labels.length);
            grid.getColumnConstraints().add(cc);
        }

        return grid;
    }

    private void runSync() {
        statusLabel.setText("Syncing...");
        Task<Void> syncTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                syncService.syncMonsters();
                return null;
            }
        };

        syncTask.setOnSucceeded(e -> {
            try {
                List<MonsterSummary> refreshed = monsterDao.getAllMonsterSummaries();
                Platform.runLater(() -> {
                    masterData.setAll(refreshed);
                    statusLabel.setText(masterData.size() + " monsters loaded.");
                });
            } catch (Exception ex) {
                statusLabel.setText("Sync finished, but refresh failed: " + ex.getMessage());
            }
        });

        syncTask.setOnFailed(e ->
                Platform.runLater(() -> statusLabel.setText("Sync failed: " + syncTask.getException().getMessage()))
        );

        Thread thread = new Thread(syncTask);
        thread.setDaemon(true);
        thread.start();
    }

    private String buildStatBlockText(Monster m) {
        StringBuilder sb = new StringBuilder();

        sb.append(m.getName()).append("\n");
        sb.append(nullSafe(m.getSize())).append(" ").append(nullSafe(m.getType()))
                .append(", ").append(nullSafe(m.getAlignment())).append("\n\n");

        if (m.getArmorClass() != null && !m.getArmorClass().isEmpty()) {
            ArmorClass ac = m.getArmorClass().get(0);
            sb.append("Armor Class: ").append(ac.getValue())
                    .append(" (").append(nullSafe(ac.getType())).append(")\n");
        }
        sb.append("Hit Points: ").append(m.getHitPoints())
                .append(" (").append(nullSafe(m.getHitDice())).append(")\n");

        if (m.getSpeed() != null) {
            Speed s = m.getSpeed();
            sb.append("Speed: ");
            appendIfPresent(sb, "walk ", s.getWalk());
            appendIfPresent(sb, "fly ", s.getFly());
            appendIfPresent(sb, "swim ", s.getSwim());
            appendIfPresent(sb, "climb ", s.getClimb());
            appendIfPresent(sb, "burrow ", s.getBurrow());
            sb.append("\n");
        }
        sb.append("\n");

        if (m.getProficiencies() != null && !m.getProficiencies().isEmpty()) {
            sb.append("Proficiencies: ");
            for (Proficiency p : m.getProficiencies()) {
                if (p.getProficiency() != null) {
                    sb.append(p.getProficiency().getName()).append(" +").append(p.getValue()).append(", ");
                }
            }
            sb.append("\n");
        }

        appendStringList(sb, "Damage Vulnerabilities", m.getDamageVulnerabilities());
        appendStringList(sb, "Damage Resistances", m.getDamageResistances());
        appendStringList(sb, "Damage Immunities", m.getDamageImmunities());

        if (m.getConditionImmunities() != null && !m.getConditionImmunities().isEmpty()) {
            sb.append("Condition Immunities: ");
            for (ApiRef ref : m.getConditionImmunities()) {
                sb.append(ref.getName()).append(", ");
            }
            sb.append("\n");
        }

        if (m.getSenses() != null) {
            Senses s = m.getSenses();
            sb.append("Senses: ");
            appendIfPresent(sb, "darkvision ", s.getDarkVision());
            appendIfPresent(sb, "blindsight ", s.getBlindSight());
            appendIfPresent(sb, "tremorsense ", s.getTremorSense());
            appendIfPresent(sb, "truesight ", s.getTrueSight());
            sb.append("passive Perception ").append(s.getPassivePerception()).append("\n");
        }

        sb.append("Languages: ").append(nullSafe(m.getLanguages())).append("\n");
        sb.append("Challenge Rating: ").append(m.getChallengeRating())
                .append(" (").append(m.getXp()).append(" XP)\n\n");

        appendAbilities(sb, "SPECIAL ABILITIES", m.getSpecialAbilities());
        appendActions(sb, "ACTIONS", m.getActions());
        appendActions(sb, "LEGENDARY ACTIONS", m.getLegendaryActions());

        return sb.toString();
    }

    private void appendStringList(StringBuilder sb, String label, List<String> values) {
        if (values != null && !values.isEmpty()) {
            sb.append(label).append(": ").append(String.join(", ", values)).append("\n");
        }
    }

    private void appendIfPresent(StringBuilder sb, String label, String value) {
        if (value != null && !value.isBlank()) {
            sb.append(label).append(value).append("  ");
        }
    }

    private void appendAbilities(StringBuilder sb, String header, List<SpecialAbility> abilities) {
        if (abilities == null || abilities.isEmpty()) return;
        sb.append(header).append("\n");
        for (SpecialAbility a : abilities) {
            sb.append("- ").append(a.getName()).append(": ").append(nullSafe(a.getDesc())).append("\n");
        }
        sb.append("\n");
    }

    private void appendActions(StringBuilder sb, String header, List<ActionEntry> actions) {
        if (actions == null || actions.isEmpty()) return;
        sb.append(header).append("\n");
        for (ActionEntry a : actions) {
            sb.append("- ").append(a.getName());
            if (a.getAttackBonus() != null) {
                sb.append(" (+").append(a.getAttackBonus()).append(" to hit)");
            }
            sb.append(": ").append(nullSafe(a.getDesc())).append("\n");
        }
        sb.append("\n");
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    @Override
    public void stop() throws Exception {
        if (dbManager != null) {
            dbManager.closeConnection();
        }
    }
}