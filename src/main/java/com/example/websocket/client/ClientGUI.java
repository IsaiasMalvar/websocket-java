package com.example.websocket.client;

import com.example.websocket.Message;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame implements MessageListener {


    private MyStompClient myStompClient;
    private String username;
    private  JPanel connectedUsersPanel, messagePanel;
    private JScrollPane messagePanellScrollPane;

    public ClientGUI(String username) throws ExecutionException, InterruptedException {
        super("User: " + username);
        this.username = username;
        myStompClient = new MyStompClient(this, username);
        setSize(1218, 685);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Do you really want to leave?", "Exit", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    myStompClient.disconnectUser(username);
                    ClientGUI.this.dispose();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateMessageSize();
            }
        });

        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGUIcomponents();


    }

    private void addGUIcomponents(){
        addConnectedUsersComponent();
        addChatComponents();
    }

    private void addConnectedUsersComponent(){
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setBorder(Utilities.addPadding(10,10,10,10));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 18));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        // Add label to the panel
        connectedUsersPanel.add(connectedUsersLabel);

        // Add the entire panel to the frame, not just the label
        add(connectedUsersPanel, BorderLayout.WEST);
    }

    private void addChatComponents(){
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setBorder(Utilities.addPadding(10,10,10,10));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanellScrollPane = new JScrollPane(messagePanel);
        messagePanellScrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        messagePanellScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagePanellScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        messagePanellScrollPane.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                revalidate();
                repaint();
            }
        });

        chatPanel.add(messagePanellScrollPane, BorderLayout.CENTER);




        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(Utilities.addPadding(10,10,10,10));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        JTextField inputField = new JTextField();
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER){
                    String input = inputField.getText();

                    if(input.isEmpty()) return;

                    inputField.setText("");



                    myStompClient.sendMessage(new Message(username, input));
                }
            }
        });
        inputField.setBorder(Utilities.addPadding(10,10,10,10));
        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setFont(new Font("Inter", Font.PLAIN, 16));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));
        inputPanel.add(inputField, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        add(chatPanel, BorderLayout.CENTER);


    }

    private JPanel createChatMessageComponent(Message message){
        JPanel chatMessage = new JPanel();
        double dynamicallySetWidth = (int) getWidth() * 0.60;
        chatMessage.setBackground(Utilities.TRANSPARENT_COLOR);
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setBorder(Utilities.addPadding(20,20,10,20));

        JLabel usernameLabel = new JLabel(message.getUser());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 18));
        usernameLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(usernameLabel);


        JLabel messageLabel = new JLabel();
        String messageContent = message.getMessage();
        messageLabel.setText("<html>" +
                "<body style='width:" + dynamicallySetWidth + "px'>" +
                messageContent +
                "</body>" +
                "</html>");
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLabel);

        return chatMessage;
    }

    @Override
    public void onMessageReceived(Message message) {
        System.out.println("onMessageReceived");
        messagePanel.add(createChatMessageComponent(message));

        repaint();
        revalidate();

        messagePanellScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
    }

    @Override
    public void onActiveUsersUpdated(ArrayList<String> users) {
        System.out.println("users" + users);
            if(connectedUsersPanel.getComponents().length >= 2){
                connectedUsersPanel.remove(1);
            }
            JPanel userListPanel = new JPanel();
            userListPanel.setBackground(Utilities.TRANSPARENT_COLOR);
            userListPanel.setLayout(new BoxLayout( userListPanel, BoxLayout.Y_AXIS));

            for(String user: users){
                JLabel username = new JLabel();
                username.setText(user);
                username.setForeground(Utilities.TEXT_COLOR);
                username.setFont(new Font ("Inter", Font.BOLD, 16));
                userListPanel.add(username);
            }

            connectedUsersPanel.add(userListPanel);
            revalidate();
            repaint();
    }

    private void updateMessageSize(){
        for(int i = 0; i < messagePanel.getComponents().length; i++){

            Component component = messagePanel.getComponent(i);

            if(component instanceof  JPanel){
                JPanel chatMessage = (JPanel) component;
                if(chatMessage.getComponent(1) instanceof JLabel){
                    double dynamicallySetWidth = (int) getWidth() * 0.40;
                    System.out.println(dynamicallySetWidth);
                    JLabel messageLabel = (JLabel) chatMessage.getComponent(1);
                    String messageContent = messageLabel.getText();
                    messageLabel.setText("<html>" +
                            "<body style='width:" + dynamicallySetWidth + "px'>" +
                            messageContent +
                            "</body>" +
                            "</html>");
                }
            }
        }
    }
}
