package Client;

public interface UserListener {
    void online(String login); // for notifying when user comes online
    void offline(String login); // for notifying when user goes offline
}
