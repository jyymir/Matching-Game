import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;

/**
 * CardGridPane is a class that manages a grid of Card objects, including their initialization,
 * placement, and image assignment for a card-matching game.
 */
public class CardGridPane extends GridPane {
    private Card[][] cards; // A 2D array to store the cards in the grid
    private ArrayList<String> cardList; // A list to store paths to card images

    private int MAXROWS; // Maximum number of rows in the grid
    private int MAXCOLS; // Maximum number of columns in the grid
    private int currentRows; // Actual number of rows currently in use
    private int currentCols; // Actual number of columns currently in use

    private int cardSize; // Size of each card

    // Default constructor initializes the card grid with a default card size
    public CardGridPane() {
        this(64); // Default card size is 64
    }

    // Constructor that accepts cardSize and initializes the grid
    public CardGridPane(int cardSize) {
        this.cardSize = cardSize;
        this.MAXCOLS = 8; // Set the grid's maximum column size
        this.MAXROWS = 8; // Set the grid's maximum row size
        this.setCards(new Card[MAXROWS][MAXCOLS]); // Create a 2D array to store cards
        this.cardList = new ArrayList<>(); // Initialize the card image list

        // Populate the grid with Card objects
        for (int row = 0; row < MAXROWS; row++) {
            for (int col = 0; col < MAXCOLS; col++) {
                Card card = new Card(); // Create a new Card object
                card.setCardAndImageSize(cardSize, cardSize); // Set card size
                getCards()[row][col] = card; // Add card to the grid array
                this.add(card, col, row); // Add card to the GridPane at the specified position
            }
        }
    }

    /**
     * Assigns images to cards in the grid using the paths from the cardList.
     */
    public void setCardImages() {
        int index = 0; // Index to track position in cardList

        for (int row = 0; row < currentRows; row++) {
            for (int col = 0; col < currentCols; col++) {
                // Ensure the card exists and can be set
                if (cards != null && cards[row] != null && cards[row][col] != null) {
                    Card card = cards[row][col];
                    card.setPath(getCardList().get(index)); // Assign image path to the card
                    card.flipCard(); // Flip the card to show the image
                    index++;
                } else {
                    System.out.println("Error: Invalid card at [" + row + "][" + col + "]");
                }
            }
        }
    }

    /**
     * Randomly shuffles the card image paths in cardList.
     */
    public void shuffleImages() {
        Collections.shuffle(getCardList()); // Shuffle the card image paths
    }

    /**
     * Retrieves a specific card from the grid based on row and column indices.
     *
     * @param r Row index
     * @param c Column index
     * @return The Card object at the specified position
     */
    public Card getCard(int r, int c) {
        return getCards()[r][c];
    }

    /**
     * Initializes the cards for the game, setting their flipped state, assigning images, and enabling/disabling.
     *
     * @param rows The number of rows in the active game grid
     * @param cols The number of columns in the active game grid
     */
    public void initCards(int rows, int cols) {
        currentRows = rows; // Set the current row count
        currentCols = cols; // Set the current column count

        // Initialize or reset all cards in the grid
        for (int r = 0; r < MAXROWS; r++) {
            for (int c = 0; c < MAXCOLS; c++) {
                Card card = cards[r][c];
                if (card == null) {
                    card = new Card(); // Create a new card if not already initialized
                    card.setCardAndImageSize(cardSize, cardSize); // Set card size
                    cards[r][c] = card; // Assign the card to the grid array
                    this.add(card, c, r); // Add card to GridPane
                }
                card.setFlipped(false); // Reset flipped state
                card.setMatched(false); // Reset matched state
                card.setDisable(true); // Disable by default
                card.flipCard(); // Flip card to show the gray back
            }
        }

        createCardImageList(rows * cols); // Create a list of card image paths
        Collections.shuffle(cardList); // Shuffle the card images

        setCardImages(); // Assign images to cards

        // Enable only the relevant cards
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cards[r][c].setDisable(false); // Enable cards in the active grid
            }
        }
    }

    /**
     * Creates a list of card image paths for the game based on the grid size.
     *
     * @param size The total number of cards required for the game
     */
    public void createCardImageList(int size) {
        cardList.clear(); // Clear any previous data
        int counter = 0;

        // Generate pairs of card image paths
        for (int i = 0; i < size / 2; i++) {
            cardList.add("cardimages/image_" + counter + ".png");
            cardList.add("cardimages/image_" + counter + ".png");
            counter++;
        }

        shuffleImages(); // Shuffle the card image paths
    }

    // Getter and setter methods for class variables...

    public int getMAXROWS() {
        return MAXROWS;
    }

    public void setMAXROWS(int MAXROWS) {
        this.MAXROWS = MAXROWS;
    }

    public int getMAXCOLS() {
        return MAXCOLS;
    }

    public void setMAXCOLS(int MAXCOLS) {
        this.MAXCOLS = MAXCOLS;
    }

    public int getCurrentRows() {
        return currentRows;
    }

    public void setCurrentRows(int currentRows) {
        this.currentRows = currentRows;
    }

    public int getCurrentCols() {
        return currentCols;
    }

    public void setCurrentCols(int currentCols) {
        this.currentCols = currentCols;
    }

    public int getCardSize() {
        return cardSize;
    }

    public void setCardSize(int cardSize) {
        this.cardSize = cardSize;
    }

    public Card[][] getCards() {
        return cards;
    }

    public void setCards(Card[][] cards) {
        this.cards = cards;
    }

    public ArrayList<String> getCardList() {
        return cardList;
    }

    public void setCardList(ArrayList<String> cardList) {
        this.cardList = cardList;
    }
}
