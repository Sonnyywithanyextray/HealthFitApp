import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.animation.ScaleTransition;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class App extends Application {

    private static final String BACKGROUND_IMAGE_PATH = "background.png";
    private static final String FONT_NAME = "FreeSans";
    private static final double FONT_SIZE = 16.0;
    private static final double BUTTON_WIDTH = 200.0;
    private static final double BUTTON_HEIGHT = 40.0;
    private static final Color BUTTON_COLOR = Color.rgb(233, 30, 99);
    private static final String MUSIC_FILE_PATH = "C:\\Users\\etels\\Desktop\\Health\\Healthapp\\src\\Clairedelune.wav";

    private StackPane root;
    private StackPane contentPane;
    private Deque<Node> history = new ArrayDeque<>();
    private Label descriptionMessage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setWidth(900);
        primaryStage.setHeight(900);
        StdAudio.playInBackground(MUSIC_FILE_PATH);
        primaryStage.setResizable(false);

        root = createBackgroundPane(BACKGROUND_IMAGE_PATH);
        contentPane = new StackPane();
        root.getChildren().add(contentPane);
        addBackArrow();

        descriptionMessage = createErrorLabel("");  
        descriptionMessage.setVisible(false);  // Initially hidden
        root.getChildren().add(descriptionMessage);  // Add to the root so it's always at the bottom
        StackPane.setAlignment(descriptionMessage, Pos.BOTTOM_CENTER);
        StackPane.setMargin(descriptionMessage, new Insets(0, 0, 10, 0));  // Margin from bottom

        Button startButton = createOptionButton("Get started", "Click this button to begin", false);
        startButton.setOnAction(event -> showSecondScreen(primaryStage));

        contentPane.getChildren().add(startButton);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            StdAudio.stopInBackground();
        });
    }

    private ImageView createBackArrow() {
        Image backArrowImage = new Image("backarrow.png");
        ImageView backArrow = new ImageView(backArrowImage);
        backArrow.setFitWidth(50);
        backArrow.setFitHeight(50);
        addHoverEffect(backArrow);  // Apply hover effect to back arrow

        return backArrow;
    }
    private void addBackArrow() {
        ImageView backArrow = createBackArrow();
        backArrow.setOnMouseClicked(event -> goBack());

        StackPane.setAlignment(backArrow, Pos.TOP_RIGHT);
        StackPane.setMargin(backArrow, new Insets(10, 10, 0, 0));  // Top and right margin of 10

        contentPane.getChildren().add(backArrow);
    }
    
    

    private void goBack() {
        if (!history.isEmpty()) {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(history.pop());
        }
    }

    private void showSecondScreen(Stage primaryStage) {
        if (!contentPane.getChildren().isEmpty()) {
            history.push(contentPane.getChildren().get(0));
        }
    
        StackPane secondScreen = createBackgroundPane(BACKGROUND_IMAGE_PATH);
    
        Label messageText = createText("Please choose one of the three options: ");
        Button bmiButton = createOptionButton("BMI calculator", "Your tooltip message here", false);
        Button bodyMassButton = createOptionButton("Body mass calculator", "Your tooltip message here", false);
        Button weightLossButton = createOptionButton("Weight loss Predictor", "Your tooltip message here", false);
    
        VBox combinedVBox = new VBox();
        combinedVBox.setSpacing(20.0);
        combinedVBox.setAlignment(Pos.CENTER);
    
        Button[] buttons = { bmiButton, bodyMassButton, weightLossButton };
        List<ImageView> questionMarks = new ArrayList<>();
    
        Label descriptionMessage = new Label();
        descriptionMessage.setWrapText(true);
        descriptionMessage.prefWidthProperty().bind(contentPane.widthProperty().subtract(40)); // Assuming 20 pixels margin on each side
        descriptionMessage.setFont(Font.font(FONT_NAME, FontWeight.NORMAL, FONT_SIZE));
        descriptionMessage.setTextFill(Color.BLACK);
        descriptionMessage.setVisible(false);  // Initially hidden
    
        // Wrap the descriptionMessage in a styled StackPane
        StackPane descriptionPane = createDescriptionPane(descriptionMessage);
        descriptionPane.setVisible(false);  // Initially hidden
    
        for (int i = 0; i < 3; i++) {
            ImageView questionMark = createQuestionMark();
            addHoverEffect(questionMark);
            questionMarks.add(questionMark);
    
            final int optionIndex = i;
    
            questionMark.setOnMouseClicked(event -> {
                switch (optionIndex) {
                    case 0: // First question mark
                        descriptionMessage.setText("Body Mass Index (BMI) is a numerical value calculated based on a person's weight and height. It is a commonly used method to assess whether a person's weight is within a healthy range relative to their height. BMI provides an estimate of body fat and is often used as a screening tool to classify individuals into different weight categories.");
                        break;
                    case 1: // Second question mark
                        descriptionMessage.setText("");
                        break;
                    case 2: // Third question mark
                        descriptionMessage.setText("Weight loss refers to the reduction of one's total body mass, often achieved through a combination of dietary changes, physical activity, and lifestyle adjustments. It can result from a decrease in body fat, muscle mass, or loss of fluids. Healthy and sustainable weight loss is usually accomplished gradually and is best maintained with long-term lifestyle changes. It's recommended to consult with healthcare professionals when embarking on a weight loss journey to ensure safety and effectiveness.\"");
                        break;
                    default:
                        descriptionMessage.setText("This is a description for option " + (optionIndex + 1));
                        break;
                }
                boolean isVisible = descriptionPane.isVisible();
                descriptionPane.setVisible(!isVisible);
                descriptionMessage.setVisible(!isVisible);
            });
    
            HBox hbox = new HBox(10.0, buttons[i], questionMark);
            hbox.setAlignment(Pos.CENTER);
            combinedVBox.getChildren().add(hbox);
        }
    
        VBox mainVBox = new VBox(30.0, messageText, combinedVBox, descriptionPane);
        mainVBox.setAlignment(Pos.CENTER);
    
        secondScreen.getChildren().add(mainVBox);
    
        contentPane.getChildren().clear();
        contentPane.getChildren().add(secondScreen);
        addBackArrow();
    
        bmiButton.setOnAction(event -> showThirdScreen(primaryStage));
    }

     private StackPane createDescriptionPane(Label descriptionLabel) {
        StackPane descriptionPane = new StackPane(descriptionLabel);
        descriptionPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-padding: 5px; -fx-border-radius: 5px;");
        return descriptionPane;
    }


    private VBox createQuestionMarksVBox(int count) {
        VBox vbox = new VBox(20.0);  
        for (int i = 0; i < count; i++) {
            ImageView questionMark = createQuestionMark();
            addHoverEffect(questionMark);
            vbox.getChildren().add(questionMark);
        }
        return vbox;
    }


    private void showThirdScreen(Stage primaryStage) {
        if (!contentPane.getChildren().isEmpty()) {
            history.push(contentPane.getChildren().get(0));
        }

        StackPane thirdScreen = createBackgroundPane(BACKGROUND_IMAGE_PATH);

        Label questionText = createText("What is your age?");
        TextField ageField = createInputField(100);
        Button submitButton = createOptionButton("Submit", "Submit Age", false);

        Label errorMessage = createErrorLabel("Please enter a valid integer age.");
        StackPane errorPane = createErrorPane(errorMessage);
        errorPane.setVisible(false);

        HBox inputGroup = new HBox(10, ageField, submitButton);
        inputGroup.setAlignment(Pos.CENTER);

        VBox contentGroup = new VBox(20, questionText, inputGroup, errorPane);
        contentGroup.setAlignment(Pos.CENTER);
        thirdScreen.getChildren().add(contentGroup);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(thirdScreen);
        addBackArrow();


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
        if (!contentPane.getChildren().isEmpty()) {
            history.push(contentPane.getChildren().get(0));
        }

        StackPane fourthScreen = createBackgroundPane(BACKGROUND_IMAGE_PATH);

        Label unitLabel = createText("Choose your preferred system:");
        Button imperialButton = createOptionButton("Imperial", "Choose the Imperial system", false);
        Button metricButton = createOptionButton("Metric", "Choose the Metric system", false);

        HBox buttonGroup = new HBox(10, imperialButton, metricButton);
        buttonGroup.setAlignment(Pos.CENTER);

        VBox contentGroup = new VBox(20, unitLabel, buttonGroup);
        contentGroup.setAlignment(Pos.CENTER);
        fourthScreen.getChildren().add(contentGroup);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(fourthScreen);
        addBackArrow();


        imperialButton.setOnAction(event -> showFifthScreen(primaryStage, "imperial"));
        metricButton.setOnAction(event -> showFifthScreen(primaryStage, "metric"));
    }

    private void showFifthScreen(Stage primaryStage, String unit) {
        if (!contentPane.getChildren().isEmpty()) {
            history.push(contentPane.getChildren().get(0));
        }

        StackPane fifthScreen = createBackgroundPane(BACKGROUND_IMAGE_PATH);

        Label heightLabel = createText("Enter your height:");
        TextField heightFeetField = createInputField(100);
        heightFeetField.setMaxWidth(100);
        TextField heightInchesField = createInputField(100);
        heightInchesField.setMaxWidth(100);
        Label weightLabel = createText("Enter your weight:");
        TextField weightField = createInputField(100);
        weightField.setMaxWidth(100);
        Button submitButton = createOptionButton("Submit", "Submit Height and Weight", false);

        VBox contentGroup = new VBox();

        if (unit.equalsIgnoreCase("metric")) {
            contentGroup.getChildren().addAll(heightLabel, heightFeetField, weightLabel, weightField);
        } else if (unit.equalsIgnoreCase("imperial")) {
            contentGroup.getChildren().addAll(heightLabel, heightFeetField, heightInchesField, weightLabel, weightField);
        }

        contentGroup.getChildren().add(submitButton);
        contentGroup.setAlignment(Pos.CENTER);
        fifthScreen.getChildren().add(contentGroup);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(fifthScreen);
        addBackArrow();


        submitButton.setOnAction(event -> {
            String heightFeetText = heightFeetField.getText();
            String heightInchesText = heightInchesField.getText();
            String weightText = weightField.getText();

            try {
                double heightFeet = Double.parseDouble(heightFeetText);
                double heightInches = Double.parseDouble(heightInchesText);
                double weight = Double.parseDouble(weightText);

                double height = convertToInches(heightFeet, heightInches);
                double bmi = calculateBMI(height, weight, unit);

                // Show the BMI result dialog with the calculated BMI value
                showBMIResultDialog(primaryStage, bmi);
            } catch (NumberFormatException e) {
                Alert invalidInputAlert = new Alert(Alert.AlertType.ERROR);
                invalidInputAlert.setContentText("Invalid height or weight input. Please enter valid numeric values.");
                invalidInputAlert.showAndWait();
            }
        });
    }

    private double convertToInches(double feet, double inches) {
        return feet * 12.0 + inches;
    }

    private double calculateBMI(double height, double weight, String unit) {
        if (unit.equalsIgnoreCase("metric")) {
            double heightMeters = height / 100.0;
            return weight / (heightMeters * heightMeters);
        } else if (unit.equalsIgnoreCase("imperial")) {
            return (weight / (height * height)) * 703.0;
        } else {
            throw new IllegalArgumentException("Invalid unit. Must be 'imperial' or 'metric'.");
        }
    }

    private boolean isValidAge(String ageText) {
        try {
            int age = Integer.parseInt(ageText);
            return age > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Label createErrorLabel(String errorMessage) {
        Label errorLabel = new Label(errorMessage);
        errorLabel.setFont(Font.font(FONT_NAME, FontWeight.NORMAL, FONT_SIZE - 4));
        errorLabel.setTextFill(Color.RED);
        return errorLabel;
    }

    private StackPane createErrorPane(Label errorLabel) {
        StackPane errorPane = new StackPane(errorLabel);
        errorPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-padding: 5px; -fx-border-radius: 5px;");
        return errorPane;
    }

    private StackPane createBackgroundPane(String imagePath) {
        StackPane pane = new StackPane();
        Image backgroundImage = new Image(imagePath);
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(pane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(pane.heightProperty());
        pane.getChildren().add(backgroundImageView);
        return pane;
    }

    private Label createText(String text) {
        Label messageText = new Label(text);
        messageText.setFont(Font.font(FONT_NAME, FontWeight.BOLD, FONT_SIZE + 15));
        messageText.setTextFill(Color.BLACK);
        return messageText;
    }

    private Button createOptionButton(String text, String tooltipText, boolean addQuestionMark) {
        Button button = createButton(text);
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setFont(Font.font(FONT_NAME, FontWeight.NORMAL, 12));
        Tooltip.install(button, tooltip);
    
        HBox buttonContent = new HBox(button);
    
        if (addQuestionMark) {
            ImageView questionMark = createQuestionMark();
            addHoverEffect(questionMark);  // Apply hover effect to question mark
            buttonContent.getChildren().add(questionMark);
            buttonContent.setSpacing(10.0);
        }
    
        button.setGraphic(buttonContent);
    
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.7);
        button.setOnMouseEntered(event -> fadeTransition.play());
        button.setOnMouseExited(event -> fadeTransition.stop());
    
        return button;
    }

    private Button createOptionButton(String text, boolean addQuestionMark) {
        return createOptionButton(text, "Default tooltip message here", addQuestionMark);
    }
    

    
    

    private void addHoverEffect(ImageView imageView) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), imageView);
        st.setByX(0.2);  // Scale by 20%
        st.setByY(0.2);  // Scale by 20%
        st.setCycleCount(2);  // Cycle count should be 2
        st.setAutoReverse(true);  // Auto reverse to get the original size

        // Add event handlers for mouse hover
        imageView.setOnMouseEntered(e -> {
            st.playFromStart();  // Play the animation when mouse enters
        });
    }
    
    

    private ImageView createQuestionMark() {
        Image questionMarkImage = new Image("Questionmark.png");
        ImageView questionMark = new ImageView(questionMarkImage);
        questionMark.setFitWidth(18);
        questionMark.setFitHeight(18);
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

    private void showBMIResultDialog(Stage primaryStage, double bmi) {
         // Create a new container using BorderPane
    BorderPane bmiResultContainer = new BorderPane();

    // Create the vignette with background and text based on BMI
    StackPane vignette = createVignettePane(bmi);
    bmiResultContainer.setCenter(vignette);

    VBox dialogContent = createDialogContents(bmi);
    bmiResultContainer.setTop(dialogContent);

    // Now, only add this combined container to the root
    root.getChildren().add(bmiResultContainer);

    // Add close action to remove the combined container
    dialogContent.setOnMouseClicked(event -> {
        root.getChildren().remove(bmiResultContainer);
    });
    }
    
    private StackPane createVignettePane(double bmi) {
            StackPane vignettePane = new StackPane();
        
            // Determine the background image and text based on the BMI value
            String backgroundImagePath;
            String bmiText;
        
            if (bmi < 18.5) {
                backgroundImagePath = "underweight.png";
                bmiText = "You're underweight.";
            } else if (bmi <= 24.9) {  
                backgroundImagePath = "normalweight.png";
                bmiText = "You're in the normal weight range.";
            } else if (bmi <= 29.9) {  
                backgroundImagePath = "overweight.png";
                bmiText = "You're overweight.";
            } else {
                backgroundImagePath = "overweight.png";
                bmiText = "You're obese.";
            }
            
            // Set the background image
            Image backgroundImage = new Image(backgroundImagePath);
            if (backgroundImage.isError()) {
                System.out.println("Failed to load image: " + backgroundImage.getException());
            }
            ImageView backgroundImageView = new ImageView(backgroundImage);
            backgroundImageView.fitWidthProperty().bind(vignettePane.widthProperty());
            backgroundImageView.fitHeightProperty().bind(vignettePane.heightProperty());
            
            // Set the text
            Label bmiTextLabel = createText(bmiText);
            
            vignettePane.getChildren().addAll(backgroundImageView, bmiTextLabel);
            return vignettePane;
        }
        

    private VBox createDialogContents(double bmi) {
        Label bmiLabel = createText("Your BMI: " + String.format("%.2f", bmi));
        Button closeButton = createOptionButton("Close", "Close the BMI result", false);
        closeButton.setOnAction(event -> {
            root.getChildren().remove(root.getChildren().size() - 1);
            root.getChildren().remove(root.getChildren().size() - 1);
        });
    
        VBox dialogContent = new VBox(bmiLabel, closeButton);
        dialogContent.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-spacing: 20px;");
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPrefWidth(300);
    
        return dialogContent;
    }
    
    private TextField createInputField(double prefWidth) {
        TextField textField = new TextField();
        textField.setFont(Font.font(FONT_NAME, FontWeight.NORMAL, FONT_SIZE));
        textField.setPrefWidth(prefWidth);
        return textField;
    }
}

