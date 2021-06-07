package spaceship;

import org.springframework.beans.factory.annotation.Autowired;
import spaceship.model.Spaceship;
import spaceship.model.Game;
import spaceship.request.SalvoShotRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spaceship.service.AttackService;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class ApplicationUI extends javafx.application.Application {

    Game game;
    int gridSize;
    String[][] playerGrid;
    String[][] opponentGrid;
    String[][] hiddenOpponentGrid;
    int playerHealth;
    GridPane gridPane;
    int numberOfShots;
    AttackService attackService = new AttackService();


    public static void main(String[] args) {
        ApplicationUI.launch(ApplicationUI.class);
    }

    @Override
    public void start(Stage stage) {

        game = new Game("opponent-ui", "”Task Opponent UI","standard", null, "match-ui");
        gridSize = Spaceship.gridSize;
        playerGrid = game.getSpaceship1().getGrid()[0];
        opponentGrid = game.getSpaceship2().getGrid()[0];
        constructHiddenOpponentGrid();
        playerHealth = (int)game.getSpaceship1().getSpaceshipsHealth().values().stream().filter(e -> e != 0).count();

        initializeGridPane();
        Scene scene = new Scene(gridPane);
        stage.setTitle("Spaceships battle");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeGridPane() {

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                ToggleButton toggleButton = new ToggleButton(hiddenOpponentGrid[i][j]);
                toggleButton.setOnAction(event -> checkNumberOfShots());
                toggleButton.setStyle("-fx-font-weight: bold");
                toggleButton.setDisable(true);
                gridPane.add(toggleButton, j, i);
            }
        }

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Button button = new Button(playerGrid[i][j]);
                button.setStyle("-fx-font-weight: bold");
                button.setDisable(true);
                gridPane.add(button, j + gridSize + 25, i);
            }
        }

        Button button = new Button("fire salvo");
        button.setOnAction(event -> fire());
        button.setStyle("-fx-font-size:25");
        button.setDisable(true);
        gridPane.add(button, 2, gridSize + 2,6, 2);

        String[] rules = { "standard", "super-charge", "desperation",
                "1-shot", "2-shot", "3-shot", "4-shot", "5-shot", "6-shot", "7-shot", "8-shot", "9-shot", "10-shot" };

        ComboBox comboBox = new ComboBox(FXCollections.observableArrayList(rules));
        comboBox.setOnAction(event -> updateRules(comboBox.getValue()));
        gridPane.add(comboBox, 10, gridSize + 2,6, 2);
    }

    private void fire() {

        ObservableList<Node> gridChildren = gridPane.getChildren();
        List<String> salvo = new ArrayList<>();

        for (int i = 0; i < gridSize * gridSize; i++) {
            if (((ToggleButton) gridPane.getChildren().get(i)).isSelected()) {
                String rowPosition = Integer.toString(i / 16, 16);
                String columnPosition = Integer.toString(i % 16, 16).toUpperCase();
                salvo.add(rowPosition + "x" + columnPosition);
            }
        }

        attackService.takeSalvoShot(game, 2, new SalvoShotRequest(salvo));
        constructHiddenOpponentGrid();

        for (String shot : salvo) {
            int shotRow = Integer.parseInt(shot.substring(0, 1), 16);
            int shotColumn = Integer.parseInt(shot.substring(2, 3), 16);

            ToggleButton toggleButton = ((ToggleButton)gridChildren.get(shotRow * 16 + shotColumn));
            toggleButton.setText(hiddenOpponentGrid[shotRow][shotColumn]);
            toggleButton.setStyle("-fx-background-color: #e6e6ff");
            toggleButton.setSelected(false);
        }

        if ("opponent-ui".equals(game.getGame().get("player_turn"))) {
            while ("opponent-ui".equals(game.getGame().get("player_turn"))) {
                attackService.takeAutoSalvoShot(game);
            }
        }

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Button button = ((Button)gridChildren.get(gridSize * gridSize + i * 16 + j));
                if (!button.getText().equals(playerGrid[i][j])) {
                    button.setText(playerGrid[i][j]);
                    button.setStyle("-fx-background-color: #e6e6ff");
                }
            }
        }

        if (game.getGame().containsKey("won")) {
            for (Node node :  gridChildren) {
                node.setDisable(true);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game status");
            alert.setHeaderText(null);
            alert.setContentText("Winner is " + game.getGame().get("won"));
            alert.show();
        }
    }

    private void updateRules(Object object) {

        String rules = object.toString();
        game.setRules(rules);

        if (rules.equals("standard") || rules.equals("super-charge")) {
            numberOfShots = playerHealth;
        } else if (rules.contains("-shot")) {
            numberOfShots = Integer.valueOf(rules.substring(0, rules.length() - "-shot".length()));
        } else if (rules.equals("desperation")) {
            numberOfShots = 6 - playerHealth;
        }

        for (Node node : gridPane.getChildren()) {
            node.setDisable(!node.isDisable());
        }
        gridPane.getChildren().get(2 * gridSize * gridSize).setDisable(true);
    }


    private void constructHiddenOpponentGrid() {

        hiddenOpponentGrid = new String[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (opponentGrid[i][j].equals("*")) {
                    hiddenOpponentGrid[i][j] = ".";
                } else {
                    hiddenOpponentGrid[i][j] = opponentGrid[i][j];
                }
            }
        }
    }

    private void checkNumberOfShots() {

        int selectedCount = 0;
        ObservableList<Node> gridChildren = gridPane.getChildren();
        for (int i = 0; i < gridSize * gridSize; i++) {
            if (((ToggleButton)gridChildren.get(i)).isSelected()) {
                selectedCount++;
            }
        }
       gridChildren.get(2 * gridSize * gridSize).setDisable(selectedCount != numberOfShots);
    }
}
