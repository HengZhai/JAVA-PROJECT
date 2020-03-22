package GUI;

import database.Student;
import database.Tutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import serverclient.Client;

import static GUI.Login.*;

/**
 * This javafx application class is provide a stage that users can change the password.
 */
public class ChangePassword extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //design the general scene of the ChangePassword interface
        GridPane gp=new GridPane();

        Label username = new Label("username: ");
        Label email = new Label("email: ");
        Label newPassword = new Label("new password: ");
        Label confirm = new Label("confirm password: ");

        TextField usernameField = new TextField();
        TextField emailField = new TextField();
        PasswordField newPasswordField = new PasswordField();
        PasswordField confirmField = new PasswordField();

        usernameField.setPromptText("Username");
        emailField.setPromptText("Email Address");
        newPasswordField.setPromptText("Set new password");
        confirmField.setPromptText("Confirm new password");

        Button cancel = new Button("Back");
        Button submit = new Button("Submit");

        Label notification = new Label();

        gp.add(username,0, 0);
        gp.add(email,0,1);
        gp.add(newPassword, 0, 2);
        gp.add(confirm, 0, 3);

        gp.add(usernameField, 1, 0);
        gp.add(emailField,1,1);
        gp.add(newPasswordField, 1, 2);
        gp.add(confirmField, 1, 3);

        gp.add(cancel, 0, 4);
        gp.add(submit, 1, 4);
        submit.setDefaultButton(true);
        GridPane.setMargin(submit, new Insets(0,0,0,120));
        gp.add(notification, 1, 5);

        ColumnConstraints columnOneConstraint = new ColumnConstraints();
        columnOneConstraint.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstraint = new ColumnConstraints();
        columnTwoConstraint.setHalignment(HPos.LEFT);
        gp.getColumnConstraints().addAll(columnOneConstraint, columnTwoConstraint);
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setLayoutX(85);
        gp.setLayoutY(150);
        //design the background of ChangePassword interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud2.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);
        //display the final scene of ChangePassword interface
        Group root = new Group(img,gp);

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Change new password");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to Setting or Login interface
        cancel.setOnAction(actionEvent -> Platform.runLater(() -> {
            if(Student.getUsername() != null || Tutor.getUsername() != null) {
                try {
                    new Setting().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    new Login().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            primaryStage.hide();
        }));

        // add button event to submit all the information to the server
        submit.setOnAction(actionEvent -> Platform.runLater(() -> {
            if(usernameField.getText().isEmpty() || emailField.getText().isEmpty() || newPasswordField.getText().isEmpty() || confirmField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Empty Error!", "Please fill in the essential information");
            }else if(!newPasswordField.getText().equals(confirmField.getText())) {
                showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Matching Error!", "Please confirm your password again");
            }else {
                if(Client.sendMessage("2," + usernameField.getText() + "," + emailField.getText() + "," + newPasswordField.getText()).equals("success")) {
                    setAttempt(0);
                    showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Change Password", "Your password has been changed successfully, now you need to back to login again");
                }else {
                    showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Information Error!", "Please check your username and email address again");
                }
            }
        }));
    }

}
