import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.net.ServerSocket;

class ClientChatForm extends Thread implements ActionListener {
    JFrame jr;
    static Socket conn;
    JPanel panel;
    JTextField NewMsg;
    JTextArea ChatHistory;
    JButton Send;

    public void clientChatFormFunc() throws UnknownHostException, IOException {
        jr = new JFrame();
        panel = new JPanel();
        NewMsg = new JTextField();
        ChatHistory = new JTextArea();
        Send = new JButton("Send");
        jr.setSize(500, 500);
        jr.setVisible(true);
        panel.setLayout(null);
        jr.add(panel);
        ChatHistory.setBounds(20, 20, 450, 360);

        panel.add(ChatHistory);
        NewMsg.setBounds(20, 400, 340, 30);
        panel.add(NewMsg);
        Send.setBounds(375, 400, 95, 30);
        panel.add(Send);
        Send.addActionListener(this);
        conn = new Socket(InetAddress.getLocalHost(), 2000);
        ChatHistory.setText("Connected to Server");
        jr.setTitle("Client");
        while (true) {
            try {
                DataInputStream dis = new DataInputStream(conn.getInputStream());
                String string = dis.readUTF();
                ChatHistory.setText(ChatHistory.getText() + '\n' + "Server:" + string);
            } catch (Exception e1) {
                ChatHistory.setText(ChatHistory.getText() + '\n' + "Message sending fail:Network Error");
                try {
                    Thread.sleep(3000);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == Send) && (NewMsg.getText() != "")) {
            ChatHistory.setText(ChatHistory.getText() + '\n' + "Me:" + NewMsg.getText());
            try {
                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                dos.writeUTF(NewMsg.getText());
            } catch (Exception e1) {
                ChatHistory.setText(ChatHistory.getText() + '\n' + "Message sending fail:Network Error");
                try {
                    Thread.sleep(3000);
                    System.exit(0);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }

            NewMsg.setText("");
        }
    }

    public void run() {
        ClientChatForm chatForm = new ClientChatForm();
        try {
            chatForm.clientChatFormFunc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerChatForm extends Thread implements ActionListener {
    JFrame jr;
    static ServerSocket server;
    static Socket conn;
    JPanel panel;
    JTextField NewMsg;
    JTextArea ChatHistory;
    JButton Send;
    DataInputStream dis;
    DataOutputStream dos;

    public void serverChatFormFunc() throws UnknownHostException, IOException {
        jr = new JFrame();
        panel = new JPanel();
        NewMsg = new JTextField();
        ChatHistory = new JTextArea();
        Send = new JButton("Send");
        jr.setSize(500, 500);
        jr.setVisible(true);
        panel.setLayout(null);
        jr.add(panel);
        ChatHistory.setBounds(20, 20, 450, 360);
        panel.add(ChatHistory);
        NewMsg.setBounds(20, 400, 340, 30);
        panel.add(NewMsg);
        Send.setBounds(375, 400, 95, 30);
        panel.add(Send);
        jr.setTitle("Server");
        Send.addActionListener(this);
        server = new ServerSocket(2000, 1, InetAddress.getLocalHost());

        ChatHistory.setText("Waiting for Client");
        conn = server.accept();
        ChatHistory.setText(ChatHistory.getText() + '\n' + "Client Found");
        while (true) {
            try {
                DataInputStream dis = new DataInputStream(conn.getInputStream());
                String string = dis.readUTF();
                ChatHistory.setText(ChatHistory.getText() + '\n' + "Client:" + string);
            } catch (Exception e1) {
                ChatHistory.setText(ChatHistory.getText() + '\n' + "Message sending fail:Network Error");
                try {
                    Thread.sleep(3000);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == Send) && (NewMsg.getText() != "")) {
            ChatHistory.setText(ChatHistory.getText() + '\n' + "ME:" + NewMsg.getText());
            try {
                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                dos.writeUTF(NewMsg.getText());
            } catch (Exception e1) {
                try {
                    Thread.sleep(3000);
                    System.exit(0);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
            NewMsg.setText("");
        }
    }

    public void run() {
        ServerChatForm sr = new ServerChatForm();
        try {
            sr.serverChatFormFunc();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

public class Project {
    public static void main(String[] args) throws UnknownHostException, IOException {
        ClientChatForm cr = new ClientChatForm();
        ServerChatForm sr = new ServerChatForm();
        sr.start();
        cr.start();
    }
}