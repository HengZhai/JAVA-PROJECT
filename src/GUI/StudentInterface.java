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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import serverclient.Client;

/**
 * The main interface of student account
 */
public class StudentInterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //design the general scene of the Student interface
        GridPane gp=new GridPane();
        Client.getSocket();

        Button button1=new Button("Meetings");
        button1.setMinWidth(200);
        Button button2=new Button("View free time slots");
        button2.setMinWidth(200);
        Button button3=new Button("Feedback");
        button3.setMinWidth(200);
        Button button4=new Button("Setting");
        button4.setMinWidth(200);
        gp.add(button1,0,0);
        gp.add(button2,0,1);
        gp.add(button3,0,2);
        gp.add(button4,0,3);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);
        gp.setLayoutX(300);
        gp.setLayoutY(130);
        //design the general scene of the left side menu
        BorderPane bp = new BorderPane();

        VBox vbox=new VBox();
        vbox.setPrefWidth(50);
        vbox.setPrefHeight(400);
        vbox.setStyle("-fx-background-color: #D3D3D3");
        bp.setLeft(vbox);

        //design the account button in the left side menu
        Image image1 = new Image("file:src/images/account.png");
        ImageView imageView1 = new ImageView(image1);
        Button account = new Button();
        account.setGraphic(imageView1);
        account.setPrefSize(45,50);
        account.setStyle("-fx-background-color: #D3D3D3");
        Pane paneIndicator1 = new Pane();
        paneIndicator1.setPrefSize(5,50);
        paneIndicator1.setStyle("-fx-background-color: #D3D3D3");
        menuDecorator1(account,paneIndicator1);
        HBox hbox1= new HBox(paneIndicator1,account);
        vbox.getChildren().add(hbox1);

        //design the module button in the left side menu
        Image image2 = new Image("file:src/images/module.png");
        ImageView imageView2 = new ImageView(image2);
        Button module = new Button();
        module.setGraphic(imageView2);
        module.setPrefSize(45,50);
        module.setStyle("-fx-background-color: #D3D3D3");
        Pane paneIndicator2 = new Pane();
        paneIndicator2.setPrefSize(5,50);
        paneIndicator2.setStyle("-fx-background-color: #D3D3D3");
        menuDecorator1(module,paneIndicator2);
        HBox hbox2= new HBox(paneIndicator2,module);
        vbox.getChildren().add(hbox2);
        //design the background of the Student interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/studentinterfacebackground.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400);
        imageView.setFitWidth(800);
        img.getChildren().add(imageView);
        //display the final scene of Student interface
        Group root = new Group(img,gp,bp);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student");
        primaryStage.setHeight(400);
        primaryStage.setWidth(800);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to MeetingForStudent interface
        button1.setOnAction(actionEvent -> Platform.runLater(() -> {
            new MeetingForStudent().start(new Stage());
            primaryStage.hide(); }));

        // add button event to jump to TimeSlotViewForStudent interface
        button2.setOnAction(actionEvent -> Platform.runLater(() -> {
            new TimeSlotViewForStudent().start(new Stage());
            primaryStage.hide(); }));

        // add button event to jump to Feedback interface
        button3.setOnAction(actionEvent -> {
            new Feedback().start(new Stage());
            primaryStage.hide(); });

        // add button event to jump to Setting interface
        button4.setOnAction(actionEvent -> Platform.runLater(() -> {
            new Setting().start(new Stage());
            primaryStage.hide(); }));

        // add button event to jump to Setting interface
        account.setOnAction(actionEvent -> Platform.runLater(() -> {
            new Setting().start(new Stage());
            primaryStage.hide(); }));

        // add button event to open a new left side menu to show modules and tutors
        module.setOnAction(actionEvent -> Platform.runLater(() -> {

            VBox vbox2=new VBox();
            vbox2.setPrefWidth(100);
            vbox2.setPrefHeight(400);
            vbox2.setStyle("-fx-background-color: #DCDCDC");
            bp.setCenter(vbox2);


            String modules= Client.sendMessage("5," + Student.getUsername());
            String[] mod = modules.split(",");
            for(int i = 0; i < mod.length; i++) {
                Button newButton = new Button(mod[i]);
                newButton.setPrefSize(100,50);
                newButton.setStyle("-fx-background-color: #DCDCDC");
                Pane paneIndicator3 = new Pane();
                paneIndicator3.setPrefSize(5,50);
                paneIndicator3.setStyle("-fx-background-color: #DCDCDC");
                menuDecorator2(newButton,paneIndicator3);
                HBox hbox3= new HBox(paneIndicator3,newButton);
                vbox2.getChildren().add(hbox3);

                newButton.setOnAction(actionEvent1 -> Platform.runLater(() -> {
                    String value1 = ((Button)actionEvent1.getSource()).getText();
                    Client.getSocket();
                    String allTutors = Client.sendMessage("6,");
                    String[] tutors = allTutors.split(",");
                    VBox vbox3=new VBox();
                    vbox3.setPrefWidth(100);
                    vbox3.setPrefHeight(400);
                    vbox3.setStyle("-fx-background-color: #F5F5F5");
                    bp.setRight(vbox3);


                    for (String tutor : tutors) {
                        Button Tom = new Button(tutor);
                        Tom.setPrefSize(100, 50);
                        Tom.setStyle("-fx-background-color: #F5F5F5");
                        Pane paneIndicator6 = new Pane();
                        paneIndicator6.setPrefSize(5, 50);
                        paneIndicator6.setStyle("-fx-background-color: #F5F5F5");
                        menuDecorator3(Tom, paneIndicator6);
                        HBox hbox6 = new HBox(paneIndicator6, Tom);
                        vbox3.getChildren().add(hbox6);

                        Tom.setOnAction(actionEvent2 -> Platform.runLater(() -> {
                            String value2 = ((Button)actionEvent2.getSource()).getText();
                            Tutor.setUsername(value2);
                            Client.getSocket();
                            if(Client.sendMessage("18," + Student.getUsername() + "," + value1 + "," + Tutor.getUsername()).equals("success")) {
                                Login.showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Match your tutor", "You have successfully matched this tutor based on your choice");
                            }
                        }));
                    }

                    Group root3 = new Group(img,gp,bp);
                    Scene scene3 = new Scene(root3);
                    primaryStage.setScene(scene3);
                    primaryStage.setResizable(false);
                    primaryStage.show();
                }));
            }

            //display the final scene of student interface
            Group root2 = new Group(img,gp,bp);
            Scene scene2 = new Scene(root2);
            primaryStage.setScene(scene2);
            primaryStage.setResizable(false);
            primaryStage.show();

        }));

    }

    //the method is to display the effect of selecting a button in the left side menu
    private void menuDecorator1(Button btn, Pane pane){
        btn.setOnMouseEntered(value -> {
            btn.setStyle("-fx-background-color:#C0C0C0");
            pane.setStyle("-fx-background-color:blue");
        });
        btn.setOnMouseExited(value -> {
            btn.setStyle("-fx-background-color:#D3D3D3");
            pane.setStyle("-fx-background-color:#D3D3D3");
        });
    }

    //the method is to display the effect of selecting a button in the left side menu
    private void menuDecorator2(Button btn, Pane pane){
        btn.setOnMouseEntered(value -> {
            btn.setStyle("-fx-background-color:#D3D3D3");
            pane.setStyle("-fx-background-color:blue");
        });
        btn.setOnMouseExited(value -> {
            btn.setStyle("-fx-background-color:#DCDCDC");
            pane.setStyle("-fx-background-color:#DCDCDC");
        });
    }

    //the method is to display the effect of selecting a button in the left side menu
    private void menuDecorator3(Button btn, Pane pane){
        btn.setOnMouseEntered(value -> {
            btn.setStyle("-fx-background-color:#DCDCDC");
            pane.setStyle("-fx-background-color:blue");
        });
        btn.setOnMouseExited(value -> {
            btn.setStyle("-fx-background-color:#F5F5F5");
            pane.setStyle("-fx-background-color:#F5F5F5");
        });
    }

}