package server;

import interfaces.Executable;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Server started...");

            while (true) {
                Socket client = server.accept();

                ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                String classFile = (String) in.readObject();
                classFile = classFile.replace("client", "server");

                byte[] bytes = (byte[]) in.readObject();
                FileOutputStream fos = new FileOutputStream(classFile);
                fos.write(bytes);

                Executable job = (Executable) in.readObject();

                long start = System.nanoTime();
                Object result = job.execute();
                long end = System.nanoTime();

                ResultImpl res = new ResultImpl(result, (end - start));

                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

                String resClass = "out/production/Lab5/server/ResultImpl.class";
                out.writeObject(resClass);

                FileInputStream fis = new FileInputStream(resClass);
                out.writeObject(fis.readAllBytes());

                out.writeObject(res);

                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
