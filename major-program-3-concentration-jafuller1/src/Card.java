import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Card extends StackPane {
    // Instance variables
    private boolean flipped;
    private boolean matched;
    private String path;
    private Image image;
    private ImageView imageView;

    // Position of the card in the grid
    private int row, col;
    // Grid dimensions
    private int numRows, numCols;

    // Default constructor
    public Card() {
        this.flipped = false;
        this.matched = false;
        this.path = "";
        this.imageView = new ImageView();
        // Add the ImageView to the StackPane
        getChildren().add(imageView);
    }

    // Constructor with an image path
    public Card(String path) {
        // Call the default constructor
        this();
        // Set the image path for this card
        setPath(path);
    }

    // Flips the card to show either its front or back
    public void flipCard() {
        getChildren().clear();

        if (isFlipped()) {
            // If the card is flipped, show the image
            imageView.setImage(new Image(getPath()));
            // Add the ImageView to display the image
            getChildren().add(imageView);
        } else {
            // If the card is not flipped, show a rectangle representing the card back
            // Padding around the rectangle
            double padding = 2;
            // Width of the back rectangle
            double rectWidth = this.getPrefWidth() - 2 * padding;
            // Height of the back rectangle
            double rectHeight = this.getPrefHeight() - 2 * padding;
            Rectangle grayRect = new Rectangle(rectWidth, rectHeight);

            // Set the color of the rectangle based on whether the card is disabled
            if (this.isDisabled()) {
                grayRect.setFill(Color.LIGHTYELLOW);
            } else {
                grayRect.setFill(Color.RED);
            }

            grayRect.setStroke(Color.BLACK);
            grayRect.setStrokeWidth(1);

            // Adds the rectangle to the StackPane
            getChildren().add(grayRect);
        }
    }

    // Sets the size of the card and adjusts the size of its associated image
    public void setCardAndImageSize(int width, int height) {
        this.setPrefSize(width, height);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
    }

    // Sets the image path for the card and loads the image
    public void setPath(String path) {
        this.path = path;
        this.image = new Image(path);
        this.imageView.setImage(image);
    }

    // Marks the card as matched and ensures the image is displayed
    public void setMatched() {
        this.matched = true;
        imageView.setImage(getImage());
        getChildren().add(imageView);
    }

    // Sets the position of the card within the grid (row and column)
    public void setGridPos(int r, int c) {
        // Set the row index
        this.row = r;
        // Set the column index
        this.col = c;
    }

    // Sets the dimensions of the grid (number of rows and columns)
    public void setGridSize(int nr, int nc) {
        // Set the total number of rows
        this.numRows = nr;
        // Set the total number of columns
        this.numCols = nc;
    }

    // Getters and setters for all instance variables
    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped; // Update the flipped state
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched; // Update the matched state
    }

    public String getPath() {
        return path;
    }

    public Image getImage() {
        return image;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
}
