package GUI;

import database.Tutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import serverclient.Client;

/**
 * Special interface for tutors to view free time slots
 */
public class TimeSlotViewForTutor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //design the general scene of the TimeSlotViewForTutor interface
        GridPane gp=new GridPane();
        Client.getSocket();
        Label label1=new Label("View free time slots");
        label1.setFont(Font.font(20));

        String freeTimeSlots = Client.sendMessage("4," + Tutor.getUsername());
        String[] timeSlots = freeTimeSlots.split(",");

        final ToggleGroup group = new ToggleGroup();

        if(!freeTimeSlots.equals("")) {
            for(int i = 0; i < timeSlots.length; i++) {
                String[] slotDetail = timeSlots[i].split(" ");
                RadioButton button1 = new RadioButton(slotDetail[0] + " - " + slotDetail[1] + " - " + slotDetail[2]);
                gp.add(button1,1,i + 1);

                button1.setToggleGroup(group);
            }
        }else {
            Label noTimeSlot = new Label("No free time slot available");
            gp.add(noTimeSlot, 1, 1);
        }

        Label selectTimeSlot = new Label();
        group.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            RadioButton temp_rb=(RadioButton)group.getSelectedToggle();
            selectTimeSlot.setText(temp_rb.getText());
        });

        Button back = new Button("<");
        Button delete = new Button("Delete");
        Button refresh = new Button("Refresh");

        gp.add(label1,1,0);
        gp.add(back, 0, 0);
        gp.add(delete, 2, timeSlots.length + 2);
        gp.add(refresh,0, timeSlots.length + 2);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);

        gp.setLayoutX(110);
        gp.setLayoutY(160);
        //design the background of the TimeSlotViewForTutor interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud8.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);
        //display the final scene of TimeSlotViewForTutor interface
        Group root = new Group(img,gp);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Time slot");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to TutorInterface
        back.setOnAction(actionEvent -> Platform.runLater(() -> {
            new TutorInterface().start(new Stage());
            primaryStage.hide();
        }));

        // add button event to achieve the function of deleting time slot
        delete.setOnAction(actionEvent -> Platform.runLater(() -> {
            Client.getSocket();
            String[] time = selectTimeSlot.getText().split(" - ");
            if(Client.sendMessage("8," + time[0] + "," + time[1] + "," + time[2] + "," + Tutor.getUsername()).equals("success")) {
                Login.showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Delete Time Slot Success", "Congratulations! This free time slot has been deleted successfully");
            }else {
                Login.showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Delete Time Slot Fail", "Sorry, This free time slot has already been booked by your student");
            }
        }));

        // add button event to refresh this interface
        refresh.setOnAction(actionEvent -> Platform.runLater(() -> {
            try {
                new TimeSlotViewForTutor().start(new Stage());
            }catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.hide(); }));
    }
}

