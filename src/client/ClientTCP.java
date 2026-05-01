package client;

import interfaces.Executable;
import interfaces.Result;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.math.BigInteger;

class ClientTCP_GUI extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private JButton sendButton;

    public ClientTCP_GUI() {
        setTitle("TCP Client - Factorial Task");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        sendButton = new JButton("Send Task");
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Enter N:"), BorderLayout.WEST);
        top.add(inputField, BorderLayout.CENTER);
        top.add(sendButton, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        sendButton.addActionListener(e -> sendTask());

        setVisible(true);
    }

    private void sendTask() {
        try {
            int n = Integer.parseInt(inputField.getText().trim());

            Socket socket = new Socket("127.0.0.1", 5000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            String classFile = "out/production/Lab5/client/JobOne.class";
            out.writeObject(classFile);

            FileInputStream fis = new FileInputStream(classFile);
            out.writeObject(fis.readAllBytes());

            JobOne job = new JobOne(n);
            out.writeObject(job);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            String resClass = (String) in.readObject();
            byte[] resBytes = (byte[]) in.readObject();

            FileOutputStream fos = new FileOutputStream(resClass);
            fos.write(resBytes);

            Result result = (Result) in.readObject();

            outputArea.append("Result: " + result.output() + "\n");
            outputArea.append("Time: " + result.scoreTime() + " ns\n\n");

            socket.close();

        } catch (Exception ex) {
            outputArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new ClientTCP_GUI();
    }
}
