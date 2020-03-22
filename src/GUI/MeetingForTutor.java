package GUI;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import serverclient.Client;

/**
 * Special meeting interface for tutor account
 */
public class MeetingForTutor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //design the general scene of the MeetingForTutor interface
        GridPane gp = new GridPane();

        Client.getSocket();

        String bookedTimeSlots = Client.sendMessage("10," + Tutor.getUsername());
        String[] timeSlots = bookedTimeSlots.split(",");

        if(!bookedTimeSlots.equals("")) {
            for(int i = 0; i < timeSlots.length; i++) {
                String[] detail = timeSlots[i].split(" ");
                Client.getSocket();
                String studentName = Client.sendMessage("11," + detail[3]);
                Label toDoList = new Label("You have a meeting with " + studentName + " on " + detail[0] + " " + detail[1] + " - " + detail[2] + "\n");

                Label space = new Label("            ");
                toDoList.setMinWidth(300);

                VBox vBox = new VBox(toDoList, space);
                gp.add(vBox, 0, i + 1);
            }
        }else {
            Label noMeeting = new Label("No meeting with your student, waiting for student booking");
            Label space = new Label("            ");
            VBox vBox = new VBox(noMeeting, space);
            gp.add(vBox, 0, 1);
        }

        Button back = new Button("Back");
        back.setMinWidth(300);

        gp.add(back, 0, timeSlots.length + 2);
        gp.setAlignment(Pos.CENTER);

        gp.setLayoutX(100);
        gp.setLayoutY(145);
        //design the background of the MeetingForTutor interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud8.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);
        //display the final scene of MeetingForTutor interface
        Group root = new Group(img,gp);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Meeting");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to TutorInterface
        back.setOnAction(actionEvent -> Platform.runLater(() -> {
            new TutorInterface().start(new Stage());
            primaryStage.hide();
        }));
    }
}
