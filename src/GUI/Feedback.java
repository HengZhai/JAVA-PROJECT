package GUI;

import database.Student;
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
 * This javafx application class is to provide an interface of feedback
 */
public class Feedback extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //design the general scene of the Feedback interface
        GridPane gp=new GridPane();

        Button button1 = new Button("Send feedback");
        Button button2 = new Button("Received feedback");
        Button button3 = new Button("Feedback history");
        Button back = new Button("Back to home page");

        button1.setMinWidth(200);
        button2.setMinWidth(200);
        button3.setMinWidth(200);
        back.setMinWidth(200);

        gp.add(button1,0,1);
        gp.add(button2,0,2);
        gp.add(button3,0,3);
        gp.add(back, 0, 4);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);

        gp.setLayoutX(150);
        gp.setLayoutY(150);
        //design the background of the Feedback interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud4.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);

        Group root = new Group(img,gp);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Feedback");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to SendFeedback interface
        button1.setOnAction(actionEvent -> Platform.runLater(() -> {
            new SendFeedback().start(new Stage());
            primaryStage.hide();
        }));

        // add button event to jump to ReceivedFeedback interface
        button2.setOnAction(actionEvent -> Platform.runLater(() -> {
            new ReceivedFeedback().start(new Stage());
            primaryStage.hide();
        }));

        // add button event to jump to FeedbackHistory interface
        button3.setOnAction(actionEvent -> Platform.runLater(() -> {
            new FeedbackHistory().start(new Stage());
            primaryStage.hide();
        }));

        // add button event to jump to StudentInterface or TutorInterface interface
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
