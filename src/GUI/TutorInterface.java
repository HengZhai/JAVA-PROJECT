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
 * The main interface of tutor account
 */
public class TutorInterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        //design the general scene of the Tutor interface
        GridPane gp=new GridPane();

        Button button1=new Button("Meetings");
        button1.setMinWidth(200);
        Button button2=new Button("Create free time slots");
        button2.setMinWidth(200);
        Button button3=new Button("View free time slots");
        button3.setMinWidth(200);
        Button button4=new Button("Feedback");
        button4.setMinWidth(200);
        Button button5=new Button("Setting");
        button5.setMinWidth(200);

        gp.add(button1,0,0);
        gp.add(button2,0,1);
        gp.add(button3,0,2);
        gp.add(button4,0,3);
        gp.add(button5,0,4);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);
        gp.setLayoutX(150);
        gp.setLayoutY(140);
        //design the background of the Tutor interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/tutorinterface.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);
        //display the final scene of Tutor interface
        Group root = new Group(img,gp);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tutor");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to MeetingForTutor interface
        button1.setOnAction(actionEvent -> Platform.runLater(() -> {
            new MeetingForTutor().start(new Stage());
            primaryStage.hide(); }));

        // add button event to jump to TimeSlotDesign interface
        button2.setOnAction(actionEvent -> Platform.runLater(() -> {
            new TimeSlotDesign().start(new Stage());
            primaryStage.hide(); }));

        // add button event to jump to TimeSlotViewForTutor interface
        button3.setOnAction(actionEvent -> Platform.runLater(() -> {
            new TimeSlotViewForTutor().start(new Stage());
            primaryStage.hide(); }));

        // add button event to jump to Feedback interface
        button4.setOnAction(actionEvent -> {
            new Feedback().start(new Stage());
            primaryStage.hide();
        });

        // add button event to jump to Setting interface
        button5.setOnAction(actionEvent -> Platform.runLater(() -> {
            new Setting().start(new Stage());
            primaryStage.hide(); }));

    }

}
