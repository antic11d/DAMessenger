//package notUsed;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.Scanner;
//
//public class Messaging extends Application {
//    private static TextArea taMessages;
//    private static Label lblTfMsg;
//    private static TextField tfMsg;
//    private static Button btnSend, btnLogoff;
//    private static TextField tfRecipient;
//    private static Label lblTfRecipient;
//
////    public static Scene getMessagingScene() {
////        VBox root = new VBox(10);
////        root.setPadding(new Insets(10, 10, 10, 10));
////
////        taMessages = new TextArea();
////        taMessages.setEditable(false);
////
////        lblTfMsg = new Label("Type message body...");
////        tfMsg = new TextField();
////
////        tfRecipient = new TextField();
////        tfRecipient.setPrefSize(30, 10);
////        lblTfRecipient = new Label("Recipient: ");
////
////        VBox vBoxHelper = new VBox(10);
////        vBoxHelper.getChildren().addAll(lblTfRecipient, tfRecipient, lblTfMsg, tfMsg);
////
////        btnSend = new Button("Send");
////        btnLogoff = new Button("Logoff");
////
////        root.getChildren().addAll(taMessages, vBoxHelper, btnSend, btnLogoff);
////
////        return new Scene(root, 800, 300);
////    }
////
////    @Override
////    public void start(Stage primaryStage) throws Exception {
////
////        primaryStage.setScene(getMessagingScene());
////        primaryStage.show();
////
////        btnSend.setOnAction(event -> sendButtonClicked());
////        btnLogoff.setOnAction(event -> {
////            try {
////                LoginPage.getClient().logoff();
////            } catch (IOException e) {
////                e.printStackTrace();
////                try {
////                    LoginPage.getClient().getSocket().close();
////                } catch (IOException e1) {
////                    e1.printStackTrace();
////                }
////            }
////        });
////    }
////
////    private void sendButtonClicked() {
////        if (!tfMsg.getText().isEmpty()) {
////            String msg = "--------------------------------\nme: " + tfMsg.getText() + "\n\t" + getTime() + "\n";
////
////            taMessages.appendText(msg);
////        }
////        tfMsg.clear();
////    }
////
////    private static String getTime() {
////        Scanner scanner = new Scanner(new Date().toString());
////        String time = "";
////        scanner.next();
////        time += scanner.next() + " " + scanner.next() + " " + scanner.next();
////
////        scanner.close();
////
////        return time;
////    }
////}
