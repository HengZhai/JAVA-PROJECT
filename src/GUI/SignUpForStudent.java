package GUI;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import serverclient.Client;

import static GUI.Login.showAlert;

/**
 * Special sign up interface for students
 */
public class SignUpForStudent extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //design the general scene of the SignUpForStudent interface
        GridPane gp=new GridPane();

        Label headerLabel = new Label("Registration for Student");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gp.add(headerLabel, 0,0,8,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);

        // Add username Label
        Label label1=new Label("Username:");
        gp.add(label1,0,1);

        // Add username field
        TextField textField1=new TextField();
        textField1.setPromptText("Username");
        gp.add(textField1,1,1);

        // Add first name label
        Label label2=new Label("First name:");
        gp.add(label2,0,2);

        // Add first name field
        TextField textField2=new TextField();
        textField2.setPromptText("First name");
        gp.add(textField2,1,2);

        // Add last name label
        Label label3=new Label("Last name:");
        gp.add(label3,0,3);

        // Add last name field
        TextField textField3=new TextField();
        textField3.setPromptText("Last name");
        gp.add(textField3,1,3);

        // Add password label
        Label label4=new Label("Password:");
        gp.add(label4,0,4);

        // Add password field
        PasswordField textField4=new PasswordField();
        textField4.setPromptText("Password");
        gp.add(textField4,1,4);

        // Add confirm password label
        Label label5=new Label("Confirm Password:");
        gp.add(label5,0,5);

        // Add confirm password field
        PasswordField textField5=new PasswordField();
        textField5.setPromptText("Confirm your password");
        gp.add(textField5,1,5);

        // Add ID label
        Label label6=new Label("University ID:");
        gp.add(label6,0,6);

        // Add ID field
        TextField textField6=new TextField();
        textField6.setPromptText("University ID");
        gp.add(textField6,1,6);

        // Add course field
        Label label7=new Label("Course ID:");
        gp.add(label7,0,7);

        ChoiceBox cb = new ChoiceBox();
        final String[] courses = {"1- MSc Computer Science", "2- MSc Advanced Computer Science"};
        cb.getItems().addAll(courses);
        gp.add(cb, 1, 7);

        Label selected = new Label();
        cb.getSelectionModel().selectedIndexProperty().addListener((ov,oldv,newv)-> {
            selected.setText(courses[newv.intValue()]);
        });

        // Add email label
        Label label8=new Label("Email:");
        gp.add(label8,0,8);

        // Add email field
        TextField textField7=new TextField();
        textField7.setPromptText("Email address");
        gp.add(textField7,1,8);

        // Add buttons
        Button button1=new Button("Back");
        Button button2=new Button("Register");
        button2.setDefaultButton(true);

        gp.add(button1,0,9);
        gp.add(button2,1,9);
        GridPane.setMargin(button2,new Insets(0,0,0,180));
        ColumnConstraints columnOneConstraint = new ColumnConstraints();
        columnOneConstraint.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstraint = new ColumnConstraints();
        columnTwoConstraint.setHalignment(HPos.LEFT);
        gp.getColumnConstraints().addAll(columnOneConstraint, columnTwoConstraint);
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(30);

        gp.setLayoutX(40);
        gp.setLayoutY(30);
        //design the background of the SignUpForStudent interface
        AnchorPane img=new AnchorPane();
        Image image = new Image("file:src/images/cloud6.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(600);
        imageView.setFitWidth(500);
        img.getChildren().add(imageView);
        //display the final scene of SignUpForStudent interface
        Group root = new Group(img,gp);

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Account Registration");
        primaryStage.setHeight(600);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();

        // add button event to jump to Login interface
        button1.setOnAction(actionEvent -> Platform.runLater(() -> {
            try {
                new Login().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.hide();
        }));

        // add button event to achieve the function of registration for student
        button2.setOnAction(event -> {
            if(textField1.getText().isEmpty() ||
                    textField2.getText().isEmpty() ||
                    textField3.getText().isEmpty() ||
                    textField4.getText().isEmpty() ||
                    textField5.getText().isEmpty() ||
                    textField6.getText().isEmpty() ||
                    textField7.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Empty Error!", "Please fill in the essential information");
            }
            else if(!textField4.getText().equals(textField5.getText())) {
                showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Matching Error!", "Please confirm your password again");
            }
            else if(Client.sendMessage("1," + textField1.getText()).equals("fail")) {
                showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Invalid username", "Please try another username");
            }
            else if(!textField7.getText().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$")) {
                showAlert(Alert.AlertType.ERROR, gp.getScene().getWindow(), "Invalid Email Address", "Please make sure your email address is correct");
            }
            else {
                Client.getSocket();
                if(Client.sendMessage("16," + textField1.getText() + "," + textField2.getText() + "," + textField3.getText()
                        + "," + textField4.getText() + "," + textField6.getText() + "," + textField7.getText() + "," + selected.getText().split("-")[0]).equals("success")) {
                    showAlert(Alert.AlertType.INFORMATION, gp.getScene().getWindow(), "Create Student Account", "Congratulations! Your student account has been created successfully.");
                }
            }
        });
    }
}
