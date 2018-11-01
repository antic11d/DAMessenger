package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatClient {
    private final String serverName;
    private final int port;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;

    private List<UserListener> userListeners = new ArrayList<>();
    private List<MessageListener> messageListeners = new ArrayList<>();
    private List<TopicListener> topicListeners = new ArrayList<>();

    public ChatClient(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public void msg(String recipient, String msgBody) throws IOException {
        String cmd = "msg " + recipient + " " + msgBody + "\n";

        serverOut.write(cmd.getBytes());
    }

    public void logoff() throws IOException {
        String cmd = "logoff\n";
        serverOut.write(cmd.getBytes());
    }

    public boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        String response;
        serverOut.write(cmd.getBytes());
        response = bufferedIn.readLine();
        System.out.println(response);


        if (response.equalsIgnoreCase("[SERVER] OK LOGIN!")) {
            startReader();
            return true;
        }
        else {
            return false;
        }
    }

    private void startReader() {
        Thread t = new Thread(this::readMessage);
        t.start();
    }

    private void readMessage() {
        try {
            String line;
            while ( (line = bufferedIn.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length > 0) {
                    String cmd = tokens[0];

                    // handling online or offline message
                    if (cmd.equalsIgnoreCase("online")) {
                        handleOnlineNotif(tokens);
                    } else if (cmd.equalsIgnoreCase("offline")) {
                        handleOfflineNotif(tokens);
                    } else if (cmd.equalsIgnoreCase("msg")) {
                        handleMessage(tokens);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void handleMessage(String[] tokens) {
        String from = tokens[2];
        StringBuilder tmpTxtBody = new StringBuilder();
        for (int i = 3; i < tokens.length; i++)
            tmpTxtBody.append(tokens[i] + " ");

        String msgBody = tmpTxtBody.toString();

        for (MessageListener listener : messageListeners) {
            listener.onMessage(from, msgBody);
        }
    }

    private void handleOfflineNotif(String[] tokens) {
        String login = tokens[1];
        System.out.println("Handling offline notification!!!!!!! for user " + login);
        for (UserListener listener : userListeners) {
            listener.offline(login);
        }
    }

    private void handleOnlineNotif(String[] tokens) {
        String login = tokens[1];
        for (UserListener listener : userListeners) {
            listener.online(login);
        }
    }



    public void joinTopic(String topic) throws IOException {
        String cmd = "join " + topic + "\n";

        for (TopicListener listener : topicListeners) {
            listener.joinTopic(topic);
        }

        serverOut.write(cmd.getBytes());
    }

    public void removeFromTopic(String topic) throws IOException {
        String cmd = "remove " + topic + "\n";

        for (TopicListener listener : topicListeners) {
            listener.removeTopic(topic);
        }

        serverOut.write(cmd.getBytes());
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, port);
            System.out.println("Client port is " + socket.getLocalPort());
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            this.serverOut = socket.getOutputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUserListener(UserListener listener) {
        userListeners.add(listener);
    }

    public void removeUserListener(UserListener listener) {
        userListeners.remove(listener);
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    public void addTopicListener(TopicListener listener) {
        topicListeners.add(listener);
    }
}
