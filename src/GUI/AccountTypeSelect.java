package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This javafx application class is to let users choose which type of account do they want to create before registration.
 */
public class AccountTypeSelect extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //design the general scene of the account type select interface
        GridPane gridPane = new GridPane();
        Button studentSignUp = new Button("Student");
        Button tutorSignUp = new Button("Tutor");
        studentSignUp.setMinWidth(200);
        tutorSignUp.setMinWidth(200);
        gridPane.add(studentSignUp, 0, 0);
        gridPane.add(tutorSignUp, 0, 1);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setLayoutX(140);
        gridPane.setLayoutY(200);
        //design the background of the account type select interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);
        //display the final scene of the account type select interface
        Group root = new Group(img,gridPane);

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sign Up");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to SignUpForStudent interface
        studentSignUp.setOnAction(actionEvent -> Platform.runLater(() -> {
            try {
                new SignUpForStudent().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.hide(); }));


        // add button event to jump to SignUpForTutor interface
        tutorSignUp.setOnAction(actionEvent -> Platform.runLater(() -> {
            try {
                new SignUpForTutor().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.hide(); }));
    }
}
