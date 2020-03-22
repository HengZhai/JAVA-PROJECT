package GUI;

import database.Student;
import database.Tutor;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import serverclient.Client;

/**
 * This javafx application class is just for login
 */
public class Login extends Application {

    private static final int MAX_ATTEMPT = 3;

    static int attempt = 0;

    public static int getAttempt() {
        return attempt;
    }

    public static void setAttempt(int attempt) {
        Login.attempt = attempt;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //design the general scene of the Login interface
        GridPane gp=new GridPane();
        Client.getSocket();

        Label label1 = new Label("Username:");
        label1.setTextFill(Color.WHITE);
        label1.setFont(Font.font(14));

        Label label2 = new Label("Password:");
        label2.setTextFill(Color.WHITE);
        label2.setFont(Font.font(14));

        Label label3 = new Label("New user?");
        label3.setTextFill(Color.WHITE);

        Label label4 = new Label("Forget password?");
        label4.setTextFill(Color.WHITE);

        TextField textField=new TextField();
        textField.setPromptText("Please enter your username");
        PasswordField passwordField=new PasswordField();
        passwordField.setPromptText("please enter your password");

        // Add Student and Tutor option buttons
        RadioButton student = new RadioButton("Student");
        student.setTextFill(Color.WHITE);
        RadioButton tutor = new RadioButton("Tutor");
        tutor.setTextFill(Color.WHITE);
        final ToggleGroup group = new ToggleGroup();

        student.setToggleGroup(group);
        tutor.setToggleGroup(group);

        Label option = new Label();

        group.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if(student.isSelected()) {
                option.setText("Student");

            }else if(tutor.isSelected()) {
                option.setText("Tutor");
            }
        });
        student.setSelected(true);
        Button button1=new Button("Clear");
        Button button2=new Button("Login");
        button2.setDefaultButton(true);
        Button button3=new Button("Sign up");
        Button button4=new Button("Change");
        gp.add(label1,1,0);
        gp.add(textField,2,0);
        gp.add(label2,1,1);
        gp.add(passwordField,2,1);
        gp.add(student, 1, 2);
        gp.add(tutor,2, 2);
        gp.add(button1,1,3);
        gp.add(button2,2,3);
        GridPane.setMargin(button2,new Insets(0,0,0,130));
        gp.add(label3,2,4);
        GridPane.setMargin(label3,new Insets(0,0,0,120));
        gp.add(button3,2,5);
        GridPane.setMargin(button3,new Insets(0,0,0,120));
        gp.add(label4, 1, 4);
        gp.add(button4,1, 5);
        gp.setAlignment(Pos.CENTER);
        gp.setLayoutX(100);
        gp.setLayoutY(145);
        gp.setHgap(10);
        gp.setVgap(10);
        //design the background of Login interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/loginbackground.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitHeight(500);
        img.getChildren().add(imageView);
        //display the final scene of Login interface
        Group root = new Group(img,gp);
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to clear words in textField and passwordField
        button1.setOnAction(actionEvent -> {
            textField.setText("");
            passwordField.setText("");
        });

        // add button event to jump to StudentInterface or TutorInterface if username and password are correct
        // Show alert if users enter wrong username or password
        // If users try more than three times, users need to change their own password and login again
        button2.setOnAction(actionEvent -> Platform.runLater(() -> {
            if(Client.sendMessage("0," + option.getText() + "," + textField.getText() + "," + passwordField.getText()).equals("success") && getAttempt() < MAX_ATTEMPT) {
                if(option.getText().equals("Student")) {
                    try {
                        Student.setUsername(textField.getText());
                        new StudentInterface().start(new Stage());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    primaryStage.hide();
                }else if(option.getText().equals("Tutor")) {
                    try {
                        Tutor.setUsername(textField.getText());
                        new TutorInterface().start(new Stage());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    primaryStage.hide();
                }
            }else {
                Client.getSocket();
                if(getAttempt() >= MAX_ATTEMPT) {
                    showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Forget Your Password?", "Maybe you need to change your password and try to login in again");
                }else {
                    setAttempt(getAttempt() + 1);
                    showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Wrong username or password!", "Please try again");
                }

                label1.setTextFill(Color.RED);
                label2.setTextFill(Color.RED);
                FadeTransition fade=new FadeTransition();
                fade.setNode(gp);
                fade.setDuration(Duration.seconds(1));
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.play();
            }
        }));

        // add button event to jump to AccountTypeSelect interface
        button3.setOnAction(actionEvent -> Platform.runLater(() -> {
            try {
                new AccountTypeSelect().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.hide(); }));

        // add button event to jump to ChangePassword interface
        button4.setOnAction(actionEvent -> Platform.runLater(() -> {
            try {
                new ChangePassword().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.hide(); }));
    }

    /**
     * The method is to show different type of alerts to improve user experience of this project
     * @param alertType type of alert
     * @param owner window
     * @param title title of alert
     * @param message content of alert
     */
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}

