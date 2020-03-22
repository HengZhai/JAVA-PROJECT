package GUI;

import database.Student;
import database.Tutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import serverclient.Client;

/**
 * This javafx application class is to provide an interface that lists all the history of feedback
 */
public class FeedbackHistory extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //design the general scene of the FeedbackHistory interface
        GridPane gp=new GridPane();
        Client.getSocket();

        String feedback;
        if(Student.getUsername() != null) {
            feedback = Client.sendMessage("15," + Student.getUsername());
        }else {
            feedback = Client.sendMessage("15," + Tutor.getUsername());
        }


        VBox all = new VBox();

        if(!feedback.equals("")) {
            String[] detail = feedback.split("#");
            for (String s : detail) {
                String[] moreDetail = s.split("/");
                Label sender = new Label("Sender: ");
                Label senderName = new Label(moreDetail[0]);
                Label receiver = new Label("Receiver: ");
                Label receiverName = new Label(moreDetail[1]);
                Label date = new Label("Date: ");
                Label dateDetail = new Label(moreDetail[2]);
                Label feedbackLabel = new Label("Feedback: ");
                Label feedbackDetail = new Label(moreDetail[3]);
                Label space = new Label("         ");
                HBox hBox1 = new HBox(sender, senderName);
                HBox hBox2 = new HBox(receiver, receiverName);
                HBox hBox3 = new HBox(date, dateDetail);
                HBox hBox4 = new HBox(feedbackLabel, feedbackDetail);
                VBox part = new VBox(hBox1, hBox2, hBox3, hBox4, space);
                all.getChildren().add(part);
            }
        }

        Button back = new Button("Back");


        gp.add(all, 0, 0);
        gp.add(back, 0, 1);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);
        gp.setLayoutX(55);
        gp.setLayoutY(50);
        //design the background of the FeedbackHistory interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud5.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(600);
        imageView.setFitWidth(400);
        img.getChildren().add(imageView);
        //display the final scene of FeedbackHistory interface
        Group root = new Group(img,gp);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Feedback History");
        primaryStage.setHeight(600);
        primaryStage.setWidth(400);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to Feedback interface
        back.setOnAction(actionEvent -> Platform.runLater(() -> {
            new Feedback().start(new Stage());
            primaryStage.hide();
        }));
    }
}
