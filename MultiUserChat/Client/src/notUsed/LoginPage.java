//package notUsed;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class LoginPage extends Application {
//    private Label lblNotifier = new Label();
//    private TextField tfUsername;
//    private TextField tfPassword;
//    private Scene scene;
//    private static Client.ChatClient client;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        VBox root = new VBox(10);
//        root.setPadding(new Insets(10,10, 10, 10));
//        root.setAlignment(Pos.CENTER);
//
//        Label lblUsername = new Label("Username:");
//        Label lblPassword = new Label("Password: ");
//
//        tfUsername = new TextField();
//        tfPassword = new TextField();
//
//        HBox hBoxHelper1 = new HBox(10);
//        hBoxHelper1.getChildren().addAll(lblUsername, tfUsername);
//
//        HBox hBoxHelper2 = new HBox(10);
//        hBoxHelper2.getChildren().addAll(lblPassword, tfPassword);
//
//        Button btnLogin = new Button("Login");
//
//        root.getChildren().addAll(hBoxHelper1, hBoxHelper2, btnLogin, lblNotifier);
//
//        scene = new Scene(root, 300, 300);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        btnLogin.setOnAction(event -> {
//            try {
//                btnLoginClicked(primaryStage);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public void btnLoginClicked(Stage primaryStage) throws IOException {
//        client = new Client.ChatClient("localhost", 9090);
//        boolean validLogin = false;
//
//        client.addUserListener(new Client.UserListener() {
//            @Override
//            public void online(String login) {
//                System.out.println("ONLINE: " + login);
//            }
//
//            @Override
//            public void offline(String login) {
//                System.out.println("OFFLINE: " + login);
//            }
//        });
//
//        client.addMessageListener((fromUser, msgBody) -> System.out.println("You got a message from " + fromUser + " saying: '" + msgBody + "'"));
//
//        if (!client.connect())
//            System.err.println("Connection failed!");
//        else {
//            System.out.println("Connected successful!");
//
//            String username = tfUsername.getText();
//            String password = tfPassword.getText();
//
//            if (!username.isEmpty() && !password.isEmpty()) {
//                if (client.login(username, password)) {
//                    System.out.println("Login successful");
//
//                    lblNotifier.setText("");
//                    lblNotifier.setTextFill(Color.GREEN);
//                    lblNotifier.setText("Login successful!");
//
//                    validLogin = true;
//                }
//                else {
//                    System.err.println("Login failed");
//                    lblNotifier.setText("");
//                    lblNotifier.setTextFill(Color.RED);
//                    lblNotifier.setText("Login failed! Try again");
//                }
//            }
//
//            if (validLogin) {
//                changeScene(primaryStage);
//            }
//
//
////            client.logoff();
//        }
//    }
//
//    public static Client.ChatClient getClient() {
//        return client;
//    }
//
//    private void changeScene(Stage primaryStage) {
//        Scene scene = Messaging.getMessagingScene();
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//
//
//}
