package serverclient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static Socket socket;


    public static Socket getSocket() {

        try {
            // socket created on client side with BOTH host IP address and port
            socket = new Socket("localhost", 8888);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("connection with server lost");
        }
        return socket;
    }

    public static String sendMessage(String message) {
        String result = null;
        try {
            // wrap input/output streams in object input/output streams
            ObjectInputStream serverIn =
                    new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream serverOut =
                    new ObjectOutputStream(socket.getOutputStream());

            System.out.println(message);
            serverOut.writeObject(message);


            result = (String) serverIn.readObject();

            System.out.println("Connected: " + socket);

            System.out.println(result);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}

