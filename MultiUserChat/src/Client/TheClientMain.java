package Client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class TheClientMain extends Application {
    private static ChatClient client;
    private Stage stage;
    private String login;

    private Label lblNotifier;
    private TextField tfUsername;
    private TextField tfPassword;
    private TextField tfTopic;
    private Label lblUsername, lblPassword;
    private Button btnLogin, logoffff, btnJoinTopic, btnRemoveTopic;
    private VBox root;
    private HBox hBoxHelper1, hBoxHelper2;
    private Scene loginScene;

    private Scene messagingScene;
    private TextArea taMessages;
    private Label lblTfMsg;
    private TextField tfMsg;
    private Button btnSend, btnLogoff, btnClear, btnExit;
    private TextField tfRecipient;
    private Label lblTfRecipient;
    private RadioButton rdTM;
    private RadioButton rdDM;

    private List<String> onliners;
    private ObservableList<String> onlineUsers;
    private Set<String> onlineUsersSet = new HashSet<>();
    private ComboBox<String> comboBoxRecipientsOnline;
    private List<String> topics;
    private Set<String> availableTopicsSet = new HashSet<>();
    private ObservableList<String> availableTopics;
    private ComboBox<String> comboBoxAvailableTopics;

    public static void main(String[] args) {
        launch(args);
    }

    public void initLoginScene() {
        root = new VBox(10);
        root.setPadding(new Insets(10,10, 10, 10));
        root.setAlignment(Pos.CENTER);

        lblUsername = new Label("Username:");
        lblPassword = new Label("Password: ");

        tfUsername = new TextField();
        tfPassword = new TextField();

        hBoxHelper1 = new HBox(10);
        hBoxHelper1.getChildren().addAll(lblUsername, tfUsername);

        hBoxHelper2 = new HBox(10);
        hBoxHelper2.getChildren().addAll(lblPassword, tfPassword);

        btnLogin = new Button("Login");
        logoffff = new Button("EXIT");

        lblNotifier = new Label("");
        root.getChildren().addAll(hBoxHelper1, hBoxHelper2, btnLogin, logoffff, lblNotifier);

        loginScene = new Scene(root, 300, 300);

        btnLogin.setOnAction(event -> {
            try {
                btnLoginTriggered();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        logoffff.setOnAction(event -> System.exit(0));
    }

    public void initMessagingScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 10, 10, 10));

        taMessages = new TextArea();
        taMessages.setEditable(false);

        lblTfMsg = new Label("Type message body...");
        tfMsg = new TextField();

//        tfRecipient = new TextField();
//        tfRecipient.setPrefSize(30, 10);
//        lblTfRecipient = new Label("Recipient: ");

        onliners = new ArrayList<>();
        onlineUsers = FXCollections.observableList(onliners);
        onlineUsers.addListener((ListChangeListener<String>) c -> System.out.println("someone came online / went offline!"));
        comboBoxRecipientsOnline = new ComboBox<>(onlineUsers);

        topics = new ArrayList<>();
        availableTopics = FXCollections.observableList(topics);
        availableTopics.addListener((ListChangeListener<String>) c -> System.out.println("Topic added / removed!!!!"));
        comboBoxAvailableTopics = new ComboBox<>(availableTopics);

        rdDM = new RadioButton("Direct message");
        rdTM = new RadioButton("Topic message");
        ToggleGroup toggleGroup = new ToggleGroup();
        rdDM.setToggleGroup(toggleGroup);
        rdTM.setToggleGroup(toggleGroup);

        HBox hBoxHelperCombo = new HBox(10);
        hBoxHelperCombo.getChildren().addAll(comboBoxRecipientsOnline, comboBoxAvailableTopics, rdDM, rdTM);

        VBox vBoxHelper = new VBox(10);
        vBoxHelper.getChildren().addAll(hBoxHelperCombo, lblTfMsg, tfMsg);

        HBox hBoxHelper = new HBox(10);
        btnSend = new Button("Send");
        btnLogoff = new Button("Logoff");
        btnClear = new Button("Clear");
        hBoxHelper.getChildren().addAll(btnSend, btnClear, btnLogoff);

        HBox hBoxBottom = new HBox(10);
        btnJoinTopic = new Button("Join topic");
        btnRemoveTopic = new Button("Remove topic");
        tfTopic = new TextField();
        Label lblTopic = new Label("Topic: ");
        hBoxBottom.getChildren().addAll(lblTopic, tfTopic, btnJoinTopic, btnRemoveTopic);

        btnExit = new Button("EXIT");

        btnLogoff.setOnAction(event -> {
            try {
                client.logoff();
                changeScene(stage, loginScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnSend.setOnAction(event -> {
            String msgBody = tfMsg.getText().trim();
            String recipient = "";

            if (rdTM.isSelected()) {
                if (comboBoxAvailableTopics.getValue() != null)
                    recipient = comboBoxAvailableTopics.getValue();

                tfMsg.clear();
//                tfRecipient.clear();

                if (!msgBody.isEmpty()) {
                    try {
                        client.msg(recipient, msgBody);
                        displayMsg(recipient, msgBody, 's');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (rdDM.isSelected()) {
                if (comboBoxRecipientsOnline.getValue() != null)
                    recipient = comboBoxRecipientsOnline.getValue();

                tfMsg.clear();
//                tfRecipient.clear();

                if (!msgBody.isEmpty()) {
                    try {
                        client.msg(recipient, msgBody);
                        displayMsg(recipient, msgBody, 's');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnClear.setOnAction(event -> taMessages.clear());

        btnJoinTopic.setOnAction(event -> {
            String topic = tfTopic.getText().trim();
            if (!tfTopic.getText().trim().isEmpty()) {
                try {
                    if (!availableTopicsSet.contains(topic)) {
                        System.out.println("---About to join topic!!!");
                        client.joinTopic(topic);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            tfTopic.clear();
        });

        btnRemoveTopic.setOnAction(event -> {
            if (!tfTopic.getText().trim().isEmpty()) {
                try {
                    String topic = tfTopic.getText().trim();

                    if (availableTopicsSet.contains(topic)) {
                        client.removeFromTopic(tfTopic.getText().trim());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            tfTopic.clear();
        });

        btnExit.setOnAction(event -> {
            try {
                client.logoff();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    client.getSocket().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.exit(0);
        });

        root.getChildren().addAll(taMessages, vBoxHelper, hBoxHelper, hBoxBottom, btnExit);

        messagingScene = new Scene(root, 600, 400);
    }

    private final String separator = "\n---------------------------------------\n";

    private void displayMsg(String recipient, String msgBody, char option) {
        String tmp = "";
        if (option == 's')
            tmp = "me -> " + recipient + ": " + "'" + msgBody +"' "  + "\n\t" + getFormattedDate() + separator;
        else if (option == 'r')
            tmp = recipient + " --> me" + ": " + "'" + msgBody +"' "  + "\n\t" + getFormattedDate() + separator;

        taMessages.appendText(tmp);
    }

    private String getFormattedDate() {
        Scanner helper = new Scanner((new Date()).toString());
        helper.next();
        return helper.next() + " " + helper.next() + " " + helper.next() + "";
    }

    private void btnLoginTriggered() throws IOException {
        client = new ChatClient("localhost", 9090);

        client.addUserListener(new UserListener() {
            @Override
            public void online(String login) {
                System.out.println("----- handling online notification!!!");
                System.out.println("ONLINE: " + login);
                if (!onlineUsersSet.contains(login)) {
                    onlineUsersSet.add(login);
                    onlineUsers.add(login);
                }
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: " + login);
                if (onlineUsersSet.contains(login)) {
                    onlineUsers.remove(login);
                    onlineUsersSet.remove(login);
                }
            }
        });

        client.addMessageListener((fromUser, msgBody) -> {
            if (!fromUser.equalsIgnoreCase(login))
                displayMsg(fromUser, msgBody, 'r');
        });

        client.addTopicListener(new TopicListener() {
            @Override
            public void joinTopic(String topic) {
                System.out.println("---- Handling topic join notif!!!");
                System.out.println("JOINED TOPIC: " + topic);
                if (!availableTopicsSet.contains(topic)) {
                    availableTopicsSet.add(topic);
                    availableTopics.add(topic);
                }
            }

            @Override
            public void removeTopic(String topic) {
                System.out.println("----- Handling topic remove notif!!!!");
                System.out.println("REMOVED TOPIC: " + topic);
                if (availableTopicsSet.contains(topic)) {
                    availableTopicsSet.remove(topic);
                    availableTopics.remove(topic);
                }
            }
        });

        if (!client.connect()) {
            System.err.println("Connection failed!");
            lblNotifier.setTextFill(Color.RED);
            lblNotifier.setText("Connection failed!");
        }
        else {
            System.out.println("Connected successful");

            tryLogin();
        }
    }


    private void tryLogin() throws IOException {
        String username = tfUsername.getText().trim();
        login = username;
        stage.setTitle("Logged in as: " + username);
        String password = tfPassword.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            if (client.login(username, password)) {
                System.out.println("OK login!");
                lblNotifier.setTextFill(Color.GREEN);
                lblNotifier.setText("OK login!");

                changeScene(stage, messagingScene);
            }
            else {
                System.err.println("Bad login!");
                lblNotifier.setTextFill(Color.RED);
                lblNotifier.setText("Bad login!");
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        initLoginScene();
        initMessagingScene();

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void changeScene(Stage primaryStage, Scene scene) {
        System.out.println("changin scene!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
