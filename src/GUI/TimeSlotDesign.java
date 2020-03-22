package GUI;

import database.Tutor;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import serverclient.Client;

/**
 * This javafx application class is to provide an interface that tutors can create free time slots for their students.
 */
public class TimeSlotDesign extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //design the general scene of the TimeSlotDesign interface
        GridPane gp=new GridPane();

        StringBuffer stringBuffer = new StringBuffer();

        Button back = new Button("<");
        Label head=new Label("Design time slot");
        head.setFont(Font.font(20));
        Label day =new Label("Select day:");
        RadioButton monday = new RadioButton("Monday");
        RadioButton tuesday = new RadioButton("Tuesday");
        RadioButton wednesday = new RadioButton("Wednesday");
        RadioButton thursday = new RadioButton("Thursday");
        RadioButton friday = new RadioButton("Friday");
        final ToggleGroup selectDay = new ToggleGroup();

        monday.setToggleGroup(selectDay);
        tuesday.setToggleGroup(selectDay);
        wednesday.setToggleGroup(selectDay);
        thursday.setToggleGroup(selectDay);
        friday.setToggleGroup(selectDay);

        selectDay.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if(monday.isSelected()) {
                stringBuffer.append("Monday,");
            }else if(tuesday.isSelected()) {
                stringBuffer.append("Tuesday,");
            }else if(wednesday.isSelected()) {
                stringBuffer.append("Wednesday,");
            }else if(thursday.isSelected()) {
                stringBuffer.append("Thursday,");
            }else if(friday.isSelected()) {
                stringBuffer.append("Friday,");
            }
        });
        gp.add(back, 0, 0);
        gp.add(head,1,0);
        gp.add(day,1,1);
        gp.add(monday,1,2);
        gp.add(tuesday,1,3);
        gp.add(wednesday,1,4);
        gp.add(thursday,1,5);
        gp.add(friday,1,6);


        Label timeSlot=new Label("Select time slot:");
        RadioButton button1 = new RadioButton("9:00-10:00");
        RadioButton button2 = new RadioButton("10:00-11:00");
        RadioButton button3 = new RadioButton("11:00-12:00");
        RadioButton button4 = new RadioButton("12:00-13:00");
        RadioButton button5 = new RadioButton("13:00-14:00");
        RadioButton button6 = new RadioButton("14:00-15:00");
        RadioButton button7 = new RadioButton("15:00-16:00");
        RadioButton button8 = new RadioButton("16:00-17:00");
        RadioButton button9 = new RadioButton("17:00-18:00");
        Button create = new Button("create time slot");

        final ToggleGroup selectSlot = new ToggleGroup();

        button1.setToggleGroup(selectSlot);
        button2.setToggleGroup(selectSlot);
        button3.setToggleGroup(selectSlot);
        button4.setToggleGroup(selectSlot);
        button5.setToggleGroup(selectSlot);
        button6.setToggleGroup(selectSlot);
        button7.setToggleGroup(selectSlot);
        button8.setToggleGroup(selectSlot);
        button9.setToggleGroup(selectSlot);

        selectSlot.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if(button1.isSelected()) {
                stringBuffer.append("9:00,10:00");
            }else if(button2.isSelected()) {
                stringBuffer.append("10:00,11:00");
            }else if(button3.isSelected()) {
                stringBuffer.append("11:00,12:00");
            }else if(button4.isSelected()) {
                stringBuffer.append("12:00,13:00");
            }else if(button5.isSelected()) {
                stringBuffer.append("13:00,14:00");
            }else if(button6.isSelected()) {
                stringBuffer.append("14:00,15:00");
            }else if(button7.isSelected()) {
                stringBuffer.append("15:00,16:00");
            }else if(button8.isSelected()) {
                stringBuffer.append("16:00,17:00");
            }else if(button9.isSelected()) {
                stringBuffer.append("17:00,18:00");
            }
        });


        gp.add(timeSlot,2,1);
        gp.add(button1,2,2);
        gp.add(button2,3,2);
        gp.add(button3,2,3);
        gp.add(button4,3,3);
        gp.add(button5,2,4);
        gp.add(button6,3,4);
        gp.add(button7,2,5);
        gp.add(button8,3,5);
        gp.add(button9,2,6);
        gp.add(create, 4, 7);

        GridPane.setMargin(back, new Insets(0, 50, 0, 0));
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);
        gp.setLayoutX(15);
        gp.setLayoutY(55);
        //design the background of the TimeSlotDesign interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud7.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);
        img.getChildren().add(imageView);
        //display the final scene of TimeSlotDesign interface
        Group root = new Group(img,gp);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Time slot");
        primaryStage.setHeight(400);
        primaryStage.setWidth(600);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to TutorInterface
        back.setOnAction(actionEvent -> Platform.runLater(() -> {
            new TutorInterface().start(new Stage());
            primaryStage.hide();
        }));

        // add button event to achieve the function of creating time slot
        create.setOnAction(actionEvent -> Platform.runLater(() -> {
            Client.getSocket();

            if(Client.sendMessage("3," + stringBuffer + "," + Tutor.getUsername()).equals("success")) {
                Login.showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Create free time slot", "This time slot has been created successfully");
            }else {
                Login.showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Create free time slot", "This time slot has been created before, try another time slot");
            }
        }));
    }
}
