package GUI;

import database.Student;
import database.Tutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import serverclient.Client;

import static GUI.Login.showAlert;

/**
 * This javafx application class is to provide an interface that users can do some operations for their personal account.
 */
public class Setting extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //design the general scene of the Setting interface
        GridPane gp = new GridPane();

        Button button1 = new Button("Change Password");
        Button button2 = new Button("Change Email Address");
        Button button3 = new Button("Sign Out");
        Button back = new Button("Back to home page");

        button1.setMinWidth(200);
        button2.setMinWidth(200);
        button3.setMinWidth(200);
        back.setMinWidth(200);

        TextField newEmail = new TextField();
        newEmail.setPromptText("Enter your new email address");
        Button submit = new Button("Submit");
        newEmail.setMinWidth(200);
        submit.setMinWidth(200);

        gp.add(button1,0,0);
        gp.add(button2,0,1);
        gp.add(button3,0,2);
        gp.add(back, 0, 3);

        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);
        gp.setLayoutX(150);
        gp.setLayoutY(150);
        //design the background of the Setting interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud3.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);
        //display the final scene of Setting interface
        Group root = new Group(img,gp);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Account Setting");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to ChangePassword interface
        button1.setOnAction(actionEvent -> Platform.runLater(() -> {
            try {
                new ChangePassword().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.hide();
        }));

        // add button event to achieve the function of changing email address
        button2.setOnAction(actionEvent -> Platform.runLater(() -> {
            gp.add(newEmail, 0, 4);
            gp.add(submit, 0, 5);
            Client.getSocket();
            submit.setOnAction(actionEvent1 -> Platform.runLater(() -> {
                if(Student.getUsername() != null) {
                    if(newEmail.getText().isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Empty Error!", "Please provide your new email address");
                    }else if(newEmail.getText().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$")) {
                        showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Invalid Email Address", "Please make sure your email address is correct");
                    }else if(Client.sendMessage("12," + Student.getUsername() + "," + newEmail.getText()).equals("success")) {
                        showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Change Email Address", "Your Email Address has already been changed successfully");
                    }
                }else {
                    if(newEmail.getText().isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Empty Error!", "Please provide your new email address");
                    }else if(newEmail.getText().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$")) {
                        showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Invalid Email Address", "Please make sure your email address is correct");
                    }else if(Client.sendMessage("12," + Tutor.getUsername() + "," + newEmail.getText()).equals("success")) {
                        showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Change Email Address", "Your Email Address has already been changed successfully");
                    }
                }
            }));
        }));

        // add button event to jump to Login interface
        button3.setOnAction(actionEvent -> Platform.runLater(() -> {
            try {
                new Login().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.hide();
        }));

        // add button event to jump to StudentInterface or TutorInterface
        back.setOnAction(actionEvent -> Platform.runLater(() -> {
            if(Student.getUsername() != null) {
                new StudentInterface().start(new Stage());
            }else {
                new TutorInterface().start(new Stage());
            }
            primaryStage.hide();
        }));
    }
}
