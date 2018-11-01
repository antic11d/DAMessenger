package ChatServer;

public class Command {
    private String cmd;
    private String topic = null, recipient = null, msgBody = null;

    public Command(String[] tokens) {
        cmd = tokens[0];
        if (cmd.equalsIgnoreCase("msg") && tokens.length >= 2) {
            recipient = tokens[1];

            StringBuilder txtBody = new StringBuilder();
            for (String s : tokens)
                txtBody.append(s);

            msgBody = txtBody.toString();
        }
        else if ( (cmd.equalsIgnoreCase("join") ||  cmd.equalsIgnoreCase("remove") ) && tokens.length >= 2) {
            topic = tokens[1];
        }
    }


}
