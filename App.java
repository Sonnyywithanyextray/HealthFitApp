import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class App extends Application {

    private static final String BACKGROUND_IMAGE_PATH = "background.png";
    private static final String FONT_NAME = "FreeSans";
    private static final double FONT_SIZE = 16.0;
    private static final double BUTTON_WIDTH = 200.0;
    private static final double BUTTON_HEIGHT = 40.0;
    private static final Color BUTTON_COLOR = Color.rgb(233, 30, 99);
    private static final String MUSIC_FILE_PATH = "Healthapp/src/Debussy - Clair de Lune.wav";

    private StackPane root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StdAudio.playInBackground(MUSIC_FILE_PATH);

        primaryStage.setResizable(false);

        root = createBackgroundPane(BACKGROUND_IMAGE_PATH);

        Button startButton = createOptionButton("Get started", "Click this button to begin", false);
        startButton.setOnAction(event -> showSecondScreen(primaryStage));

        root.getChildren().add(startButton);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            StdAudio.stopInBackground();
        });
    }

    private void showSecondScreen(Stage primaryStage) {
        StackPane secondScreen = createBackgroundPane(BACKGROUND_IMAGE_PATH);

        Label messageText = createText("Choose one of the options below:");

        Button bmiButton = createOptionButton("BMI calculator", "Calculate Body Mass Index", true);
        Button bodyMassButton = createOptionButton("Body mass calculator", "Calculate Body Mass", true);
        Button weightLossButton = createOptionButton("Weight loss Predictor", "Predict Weight Loss", true);

        VBox buttonsPane = new VBox(20.0);
        buttonsPane.getChildren().addAll(messageText, bmiButton, bodyMassButton, weightLossButton);
        buttonsPane.setAlignment(Pos.CENTER);

        StackPane.setAlignment(buttonsPane, Pos.CENTER);
        secondScreen.getChildren().add(buttonsPane);

        primaryStage.getScene().setRoot(secondScreen);

        bmiButton.setOnAction(event -> showThirdScreen(primaryStage));
    }

    private void showThirdScreen(Stage primaryStage) {
        StackPane thirdScreen = createBackgroundPane(BACKGROUND_IMAGE_PATH);

        Label questionText = createText("What is your age?");
        TextField ageField = new TextField();
        Button submitButton = createOptionButton("Submit", "Submit Age", false);

        Label errorMessage = createErrorLabel("Please enter a valid integer age.");
        StackPane errorPane = createErrorPane(errorMessage);
        errorPane.setVisible(false);

        HBox contentPane = new HBox(ageField, submitButton);
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setSpacing(10.0);

        VBox parentContainer = new VBox(questionText, contentPane, errorPane);
        parentContainer.setAlignment(Pos.CENTER);
        parentContainer.setSpacing(100.0);

        thirdScreen.getChildren().add(parentContainer);

        primaryStage.setScene(new Scene(thirdScreen));
        primaryStage.show();

        submitButton.setOnAction(event -> {
            String ageText = ageField.getText();
            if (isValidAge(ageText)) {
                showFourthScreen(primaryStage);
            } else {
                errorMessage.setVisible(true);
                errorPane.setVisible(true);
            }
        });
    }

    private void showFourthScreen(Stage primaryStage) {
        StackPane fourthScreen = createBackgroundPane(BACKGROUND_IMAGE_PATH);

        Label questionText = createText("Do you prefer metric or imperial?");

        Button imperial = createOptionButton("Imperial", "Use Imperial Units", false);
        Button metric = createOptionButton("Metric", "Use Metric Units", false);

        VBox buttonsPane = new VBox(20.0);
        buttonsPane.getChildren().addAll(questionText, imperial, metric);
        buttonsPane.setAlignment(Pos.CENTER);
        StackPane.setAlignment(buttonsPane, Pos.CENTER);

        fourthScreen.getChildren().add(buttonsPane);
        primaryStage.getScene().setRoot(fourthScreen);
    }

    private boolean isValidAge(String ageText) {
        try {
            int age = Integer.parseInt(ageText);
            return age > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Label createErrorLabel(String text) {
        Label errorMessage = new Label(text);
        errorMessage.setFont(Font.font(FONT_NAME, FontWeight.BOLD, 15));
        errorMessage.setTextFill(Color.WHITE);
        errorMessage.setVisible(false); // Hide the error message by default
        return errorMessage;
    }

    private StackPane createErrorPane(Label errorMessage) {
        StackPane errorPane = new StackPane();
        errorPane.getChildren().add(errorMessage);
        errorPane.setVisible(false);

        double errorPaneWidth = errorMessage.getBoundsInLocal().getWidth() + 25.0;
        double errorPaneHeight = errorMessage.getBoundsInLocal().getHeight() + 20.0;
        errorPane.setMinWidth(errorPaneWidth);
        errorPane.setMaxWidth(errorPaneWidth);
        errorPane.setMinHeight(errorPaneHeight);
        errorPane.setMaxHeight(errorPaneHeight);

        // Apply CSS styling to add rounded corners
        errorPane.setStyle("-fx-background-color: red; -fx-background-radius: 10;");

        return errorPane;
    }

    private StackPane createBackgroundPane(String imagePath) {
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: black;");

        Image backgroundImage = new Image(imagePath);
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setPreserveRatio(true);

        // Calculate the initial scale factor based on the current window size
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double scaleFactor = calculateScaleFactor(visualBounds.getWidth(), visualBounds.getHeight());

        // Bind the size of the background image to the pane size
        backgroundImageView.fitWidthProperty().bind(pane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(pane.heightProperty());

        pane.getChildren().add(backgroundImageView);

        return pane;
    }

    private double calculateScaleFactor(double width, double height) {
        Image backgroundImage = new Image(BACKGROUND_IMAGE_PATH);
        double imageWidth = backgroundImage.getWidth();
        double imageHeight = backgroundImage.getHeight();

        double scaleX = width / imageWidth;
        double scaleY = height / imageHeight;

        return Math.max(scaleX, scaleY);
    }

    private Label createText(String text) {
        Label messageText = new Label(text);
        messageText.setFont(Font.font(FONT_NAME, FontWeight.BOLD, FONT_SIZE + 15));
        messageText.setTextFill(Color.BLACK);
        return messageText;
    }

    private Button createOptionButton(String text, String tooltipText, boolean addQuestionMark) {
        Button button = createButton(text);

        // Create a Tooltip for the button
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setFont(Font.font(FONT_NAME, FontWeight.NORMAL, 12));
        Tooltip.install(button, tooltip);

        if (addQuestionMark) {
            // Create the question mark icon label
            ImageView questionMark = createQuestionMark();

            // Create the button content with question mark icon
            StackPane questionMarkContainer = new StackPane(questionMark);
            questionMarkContainer.setAlignment(Pos.CENTER_LEFT);
            questionMarkContainer.setMinWidth(24);
            questionMarkContainer.setMaxWidth(24);
            questionMarkContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 3px 6px; -fx-border-radius: 50%;");

            // Create the button layout
            HBox buttonContent = new HBox(5.0);
            if (button.getGraphic() != null) {
                buttonContent.getChildren().addAll(questionMarkContainer, button.getGraphic());
            } else {
                buttonContent.getChildren().addAll(questionMarkContainer);
            }

            // Set the button content
            button.setGraphic(buttonContent);
        }

        // Create the FadeTransition
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.7);

        // Add event handlers
        button.setOnMouseEntered(event -> fadeTransition.play());
        button.setOnMouseExited(event -> fadeTransition.stop());

        return button;
    }

    private ImageView createQuestionMark() {
        // Load the question mark icon image
        Image questionMarkImage = new Image("Questionmark.png");

        // Create the ImageView for the question mark
        ImageView questionMark = new ImageView(questionMarkImage);
        questionMark.setFitWidth(18);
        questionMark.setFitHeight(18);
        questionMark.setPreserveRatio(true);

        return questionMark;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setStyle("-fx-font-family: " + FONT_NAME + "; -fx-font-weight: bold; -fx-font-size: " + FONT_SIZE + "px;");
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: " + toHexColor(BUTTON_COLOR) + "; -fx-border-radius: 20px; -fx-padding: 10px 20px;");
        return button;
    }

    private String toHexColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
