package ChatServer;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerWorker extends Thread {
    private final Socket clientSocket;
    private String login = null;
    private final Server server;
    private PrintStream printStream;
    private OutputStream outputStream;
    private Set<String> topics = new HashSet<>();


    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        this.printStream = new PrintStream(outputStream);

        while ( (line = reader.readLine() ) != null) {
            String[] tokens = line.split(" ");

            if (tokens.length > 0) {
                String cmd = tokens[0];

                if (cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("logoff")) {
                    handleLogoff();
                    break;
                } else if (cmd.equalsIgnoreCase("login")) {
                    handleLogin(printStream, tokens);
                } else if (cmd.equalsIgnoreCase("msg")) {
                    handleMsg(tokens);
                } else if (cmd.equalsIgnoreCase("join")) {
                    handleTopicJoin(tokens);
                } else if (cmd.equalsIgnoreCase("remove")) {
                    handleTopicRemove(tokens);
                } else {
                    String msg = "[SERVER] UNKOWN COMMAND " + cmd;
                    printStream.println(msg);
                }
            }
        }
        clientSocket.close();
    }

    public boolean isMemberOfTopic(String topic) {
        return topics.contains(topic);
    }

    private void handleTopicRemove(String[] tokens) {
        if (tokens.length == 2 && isMemberOfTopic(tokens[1])) {
            topics.remove(tokens[1]);
        }
    }

    private void handleTopicJoin(String[] tokens) {
        if (tokens.length == 2) {
            this.topics.add(tokens[1]);
        }
    }

    // format direct message: msg login textBody
    // format group/topic message: msg #topic textBody
    private void handleMsg(String[] tokens) {
        String sendingTo = tokens[1];

        boolean isTopicMessage = sendingTo.charAt(0) == '#';

        StringBuilder tmpTxtBody = new StringBuilder();
        for (int i = 2; i < tokens.length; i++)
            tmpTxtBody.append(tokens[i] + " ");

        String textBody = tmpTxtBody.toString();
//        System.out.println(textBody);

        List<ServerWorker> currentWorkerList = server.getWorkers();

        if (isTopicMessage) {
            for (ServerWorker worker : currentWorkerList) {
                if (worker.isMemberOfTopic(sendingTo)) {
                    String msg = "msg from " + login + " in " + sendingTo+ ": " + textBody;
                    worker.send(msg);
                }
            }
        }
        else {
            for (ServerWorker worker : currentWorkerList) {
                if (worker.getLogin().equalsIgnoreCase(sendingTo)) {
                    String msg = "msg from " + login + ": " + textBody;
                    worker.send(msg);
                }
            }
        }
    }

    private void handleLogoff() throws IOException {
        server.removeWorker(this);
        String offlineMsg = "offline " + login;
        List<ServerWorker> currentWorkers = server.getWorkers();

        for (ServerWorker worker : currentWorkers) {
            if (!login.equalsIgnoreCase(worker.getLogin()))
                worker.send(offlineMsg);
        }

        clientSocket.close();
    }



    private void handleLogin(PrintStream printStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String passwd = tokens[2];

            if (validateLogin(login, passwd)) {
                String msg = "[SERVER] OK LOGIN!\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                msg = "[SERVER] User " + login + " logged in succesfully!";

                List<ServerWorker> currentWorkerList = server.getWorkers();
                // send curr user all other online logins
                StringBuilder stringBuilder = new StringBuilder();
                for (ServerWorker worker : currentWorkerList) {
                    if (worker.getLogin() != null) {
                        if (!login.equalsIgnoreCase(worker.getLogin())) {
                            stringBuilder.delete(0, stringBuilder.length());

                            stringBuilder.append("online " + worker.getLogin());
                            send(stringBuilder.toString());
                        }
                    }
                }

                // send other online users curr users status
                String onlineMsg = "online " + login;
                for (ServerWorker worker : currentWorkerList) {
                    if (!login.equalsIgnoreCase(worker.getLogin()))
                        worker.send(onlineMsg);
                }
            }
            else {
                String msg = "[SERVER] BAD LOGIN!";
                printStream.println(msg);
                System.err.println("Bad login for " + login);
            }
        }
    }

    private void send(String onlineMsg) {
        if (login != null)
            printStream.println(onlineMsg);
    }

    // TODO connection to databse and doing proper login authentication
    private boolean validateLogin(String login, String password) {
        if ( (login.equalsIgnoreCase("guest") && password.equalsIgnoreCase("guest"))
            || (login.equalsIgnoreCase("jim") && password.equalsIgnoreCase("jim"))
            || (login.equalsIgnoreCase("misa") && password.equalsIgnoreCase("misa")))
            return true;
        else
            return false;

    }
}
