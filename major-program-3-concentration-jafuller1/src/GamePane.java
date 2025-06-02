import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.sun.media.jfxmedia.logging.Logger.setLevel;

/**
 * GamePane is the main panel for the card-matching game, handling game initialization,
 * user interaction, and level selection.
 */
public class GamePane extends BorderPane {

    private int rows, cols; // Number of rows and columns in the game grid
    private int numClicks; // Tracks the number of cards clicked
    private int numMatched; // Tracks the number of matched pairs
    private CardGridPane cardGridPane; // GridPane for the card grid
    private HBox commandPane; // HBox for control buttons and UI components

    private Button exitButton; // Button to exit the game
    private Button newGameButton; // Button to start a new game

    private ComboBox<String> lvlSelector; // Dropdown for level selection
    private Card clickedCardOne, clickedCardTwo; // References to the two clicked cards

    private Label turnLabel; // Label to display the number of turns
    private int turnCount = 0; // Counter for the number of turns taken

    // Default constructor for GamePane
    public GamePane() {
        this(100); // Default card size is 100
    }

    // Constructor that accepts cardSize and initializes the game components
    public GamePane(int cardSize) {
        // Initialize the CardGridPane with the specified card size
        cardGridPane = new CardGridPane(cardSize);

        // Create an exit button and set its action
        exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0)); // Closes the application

        // Create the turn label to display the number of turns
        turnLabel = new Label("Turns: " + turnCount);

        // Create a new game button and set its action
        newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> newGame()); // Starts a new game when clicked

        // Initialize the command pane for UI components
        commandPane = new HBox(10); // Horizontal box with 10px spacing
        commandPane.setAlignment(Pos.CENTER); // Center-align the components

        // Initialize the level selector (dropdown)
        lvlSelector = new ComboBox<>();
        lvlSelector.getItems().addAll("Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6");
        lvlSelector.setValue("Level 1"); // Set default level

        // Set level selection action
        lvlSelector.setOnAction(e -> {
            String selected = lvlSelector.getValue(); // Get selected level
            rows = 2; // Default rows
            cols = 3; // Default columns

            // Adjust rows and columns based on level
            switch (selected) {
                case "Level 2":
                    rows = 2;
                    cols = 4;
                    break;
                case "Level 3":
                    rows = 4;
                    cols = 4;
                    break;
                case "Level 4":
                    rows = 4;
                    cols = 6;
                    break;
                case "Level 5":
                    rows = 6;
                    cols = 6;
                    break;
                case "Level 6":
                    rows = 8;
                    cols = 8;
                    break;
            }

            // Refresh the card grid with new rows and columns
            cardGridPane.initCards(rows, cols);
            cardGridPane.createCardImageList(rows * cols);
            cardGridPane.setCardImages();
        });

        // Add UI components to the command pane
        commandPane.getChildren().addAll(lvlSelector, newGameButton, exitButton, turnLabel);

        // Set the layout of the GamePane
        setBottom(commandPane); // Command pane at the bottom
        setCenter(cardGridPane); // Card grid in the center

        // Start a new game
        newGame();

        // Register card listeners for user interaction
        registerCardListeners();
    }

    /**
     * Starts a new game by resetting variables and reinitializing the grid.
     */
    public void newGame() {
        numClicks = 0;
        numMatched = 0;
        clickedCardTwo = null;
        clickedCardOne = null;
        turnCount = 0; // Reset the turn counter
        turnLabel.setText("Turns: " + turnCount); // Update the turn label
        cardGridPane.initCards(2, 3); // Default to 2x3 grid
        cardGridPane.createCardImageList(2 * 3); // Generate card images
        cardGridPane.setCardImages(); // Assign images to cards

        // Reset all cards in the grid
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Card card = cardGridPane.getCard(row, col);
                if (card != null) {
                    card.setMatched(false); // Set as not matched
                    card.setFlipped(false); // Set as not flipped
                    card.flipCard(); // Flip to gray side
                    card.setStyle(""); // Remove any special styling
                }
            }
        }

        // Register card listeners for the new game
        registerCardListeners();
    }

    /**
     * Registers listeners for all cards to handle user interactions.
     */
    public void registerCardListeners() {
        for (int row = 0; row < 8; row++) {
    for (int col = 0; col < 8; col++) {
        Card card = cardGridPane.getCard(row, col);
        card.setFlipped(false); // Ensure the card starts unflipped
        card.flipCard(); // Display the card's default image

        // Add a mouse event listener to the card
        card.setOnMousePressed(e -> {
            if (card.isFlipped() || card.isMatched() || numClicks >= 2) {
                return; // Ignore if already flipped/matched or if waiting for an animation
            }

            card.setFlipped(true);
            card.flipCard();

    if (numClicks == 0) {
        clickedCardOne = card; // Set the first clicked card
        numClicks = 1;
    } else if (numClicks == 1) {
        clickedCardTwo = card; // Set the second clicked card
        numClicks = 2;

        // AnimationTimer to handle the matching logic with a delay
        new AnimationTimer() {
            private long startTime = -1;

            @Override
            public void handle(long now) {
                if (startTime < 0) {
                    startTime = now;
                }
                if (now - startTime >= 800_000_000) { // 800 ms delay
                    turnCount++;
                    turnLabel.setText("Turns: " + turnCount);

                    if (clickedCardOne.getPath().equals(clickedCardTwo.getPath())) {
                        // Cards match
                        clickedCardOne.setMatched(true);
                        clickedCardTwo.setMatched(true);
                        clickedCardOne.setStyle("-fx-background-color: green;");
                        clickedCardTwo.setStyle("-fx-background-color: green;");
                        numMatched++;

                        // Play a sound upon match
                        try {
                            File file = new File("WHAT OH HELL NAH - Angry Grandpa Clip.wav");
                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioInputStream);
                            clip.start();
                        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        // Check if all pairs are matched
                        int totalMatchesNeeded = (cardGridPane.getCurrentRows() * cardGridPane.getCurrentCols()) / 2;
                        if (numMatched == totalMatchesNeeded) {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Concentration");
                                alert.setHeaderText("Congratulations!");
                                alert.setContentText("You've matched all the cards in " + turnCount + " turns!");
                                alert.showAndWait();
                            });
                        }
                    } else {
                        // Cards don't match; flip them back
                        clickedCardOne.setFlipped(false);
                        clickedCardTwo.setFlipped(false);
                        clickedCardOne.flipCard();
                        clickedCardTwo.flipCard();
                    }

                    // Reset for the next turn
                    clickedCardOne = null;
                    clickedCardTwo = null;
                    numClicks = 0;
                    stop(); // Stop the AnimationTimer
                }
            }
        }.start();
    }
});
}
}
}
}
