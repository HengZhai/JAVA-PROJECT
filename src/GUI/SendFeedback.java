package GUI;

import database.Student;
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
import javafx.stage.Stage;
import serverclient.Client;

/**
 * This javafx application class is to provide an interface that users can send feedback to their student or tutor.
 */
public class SendFeedback extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //design the general scene of the SendFeedback interface
        GridPane gp=new GridPane();
        Label receiver = new Label("To:");

        ChoiceBox cb = new ChoiceBox();
        Label selected = new Label();
        Client.getSocket();
        if(Student.getUsername() != null) {
            String allReceivers = Client.sendMessage("6,");
            String[] receivers = allReceivers.split(",");
            cb.getItems().addAll(receivers);

            cb.getSelectionModel().selectedIndexProperty().addListener((ov,oldv,newv)-> {
                selected.setText(receivers[newv.intValue()]);
            });
        }else {
            String allReceivers = Client.sendMessage("19,");
            String[] receivers = allReceivers.split(",");
            cb.getItems().addAll(receivers);

            cb.getSelectionModel().selectedIndexProperty().addListener((ov,oldv,newv)-> {
                selected.setText(receivers[newv.intValue()]);
            });
        }

        Label title = new Label("Title:");
        TextField textField2=new TextField();
        TextArea textArea=new TextArea();
        textArea.setPrefSize(10,150);
        Button back = new Button("back");
        Button send = new Button("send");

        gp.add(receiver,0,0);
        GridPane.setMargin(receiver,new Insets(0,0,0,15));
        gp.add(cb,1,0);
        GridPane.setMargin(cb,new Insets(0,5,0,-220));
        gp.add(title,0,1);
        GridPane.setMargin(title,new Insets(0,0,0,15));
        gp.add(textField2,1,1);
        GridPane.setMargin(textField2,new Insets(0,5,0,-220));
        gp.add(textArea,0,2);
        GridPane.setMargin(textArea,new Insets(0,-10,0,15));
        gp.add(back,0,3);
        GridPane.setMargin(back,new Insets(0,0,0,15));
        gp.add(send,0,3);
        GridPane.setMargin(send,new Insets(0,-10,0,220));
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);
        gp.setLayoutX(110);
        gp.setLayoutY(100);
        //design the background of the SendFeedback interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud5.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);
        //display the final scene of SendFeedback interface
        Group root = new Group(img,gp);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Send feedback");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to Feedback interface
        back.setOnAction(actionEvent -> Platform.runLater(() -> {
            new Feedback().start(new Stage());
            primaryStage.hide();
        }));

        // add button event to send all the information to the server
        send.setOnAction(actionEvent -> Platform.runLater(() -> {
            Client.getSocket();
            if(Student.getUsername() != null) {
                if(Client.sendMessage("13," + Student.getUsername() + "," + selected.getText() + "," + textField2.getText() + ":" + textArea.getText()).equals("success")) {
                    Login.showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Send Feedback to your tutor", "Feedback has already been sent successfully");
                }
            }else {
                if(Client.sendMessage("13," + Tutor.getUsername() + "," + selected.getText() + "," + textField2.getText() + ":" + textArea.getText()).equals("success")) {
                    Login.showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Send Feedback to your student", "Feedback has already been sent successfully");
                }
            }
        }));
    }
}