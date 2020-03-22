package serverclient;

import database.JDBCOperation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int MAX_THREADS = 10;

    /**
     * Constructor for the Server class
     *
     * @param port port number to listen on
     */
    public Server(int port) {

        // initialise ServerSocket object that will be used to accept new clients
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Creating server listening "
                    + "to port " + port);

            // initialise a fixed size thread pool that can allow up to MAX_THREADS concurrent threads
            ExecutorService threadPool =
                    Executors.newFixedThreadPool(MAX_THREADS);

            // an infinite loop to accept clients indefinitely (on the main thread)
            while (true) {
                System.out.println("Waiting for client to connect");
                // call .accept to wait for a new client to connect
                // a new socket object is returned by .accept when the
                // new client connects successfully
                Socket newClientSocket = serverSocket.accept();
                // pass the socket created for the new client to a separate
                // ClientHandlerThread object and execute it on the thread pool
                threadPool.execute(
                        new ClientHandlerThread(newClientSocket));
            }

        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Inner class to represent a dedicated task that handles a particular client
     */
    private static class ClientHandlerThread implements Runnable {

        private Socket clientSocket;

        /**
         * Simple constructor that takes
         * the socket created by the ServerSocket.accept()
         * method
         *
         * @param clientSocket the socket created
         * by serverSocket.accept() that
         * communicates to a specific client
         */
        public ClientHandlerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        /*
         * every thing that happens inside the run method
         * will execute on a new thread
         * (put as much work in here as you can)
         *
         */

        @Override
        public void run() {
            System.out.println("Connected to client");

            try {
                ObjectOutputStream clientOut =
                        new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream clientIn =
                        new ObjectInputStream(clientSocket.getInputStream());
                // Since I am sending String objects,
                // I can safely downcast to String here
                String request = (String) clientIn.readObject();

                String[] requestList = request.split(",");
                String response = null;

                switch (Integer.parseInt(requestList[0])) {
                    case 0:
                        if(JDBCOperation.login(requestList[1], requestList[2], requestList[3])) {
                            response = "success";
                        }else {
                            response = "fail";
                        }
                        break;
                    case 1:
                        if(!JDBCOperation.invalidUsername(requestList[1])) {
                            response = "success";
                        }else {
                            response = "fail";
                        }
                        break;
                    case 2:
                        if(JDBCOperation.updatePassword(requestList[1], requestList[2], requestList[3])) {
                            response = "success";
                        }else {
                            response = "fail";
                        }
                        break;
                    case 3:
                        if(JDBCOperation.insertBookSlot(requestList[1], requestList[2], requestList[3], requestList[4])) {
                            response = "success";
                        }else {
                            response = "fail";
                        }
                        break;
                    case 4:
                        response = JDBCOperation.getFreeTimeSlots(requestList[1]).toString();
                        break;
                    case 5:
                        response = JDBCOperation.getModules(requestList[1]).toString();
                        break;
                    case 6:
                        response = JDBCOperation.getAllTutors();
                        break;
                    case 7:
                        if(JDBCOperation.bookTimeSlot(requestList[1], requestList[2], requestList[3], requestList[4], requestList[5])) {
                            response = "success";
                        }else {
                            response = "fail";
                        }
                        break;
                    case 8:
                        if(JDBCOperation.deleteFreeTimeSlot(requestList[1], requestList[2], requestList[3], requestList[4])) {
                            response = "success";
                        }else {
                            response = "fail";
                        }
                        break;
                    case 9:
                        response = JDBCOperation.getBookedTimeSlots(requestList[1], requestList[2]).toString();
                        break;
                    case 10:
                        response = JDBCOperation.getBookedTimeSlotsForTutor(requestList[1]);
                        break;
                    case 11:
                        response = JDBCOperation.getStudentName(Integer.parseInt(requestList[1]));
                        break;
                    case 12:
                        JDBCOperation.updateEmail(requestList[1], requestList[2]);
                        response = "success";
                        break;
                    case 13:
                        JDBCOperation.sendFeedback(requestList[1], requestList[2], requestList[3]);
                        response = "success";
                        break;
                    case 14:
                        response = JDBCOperation.getFeedback(requestList[1]);
                        break;
                    case 15:
                        response = JDBCOperation.getFeedbackHistory(requestList[1]);
                        break;
                    case 16:
                        String studentPassword = PasswordEncryption.encrypt(requestList[4]);

                        JDBCOperation.insertStudent(requestList[1], requestList[2], requestList[3], studentPassword,
                                requestList[5], requestList[6], Integer.parseInt(requestList[7]));
                        response = "success";
                        break;
                    case 17:
                        String tutorPassword = PasswordEncryption.encrypt(requestList[4]);

                        JDBCOperation.insertTutor(requestList[1], requestList[2], requestList[3], tutorPassword,
                                requestList[5], requestList[6]);
                        response = "success";
                        break;
                    case 18:
                        JDBCOperation.insertRelationship(requestList[1], requestList[2], requestList[3]);
                        response = "success";
                        break;
                    case 19:
                        response = JDBCOperation.getAllStudents();
                        break;
                }

                clientOut.writeObject(response);

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Connection with client lost.");
            }

        }

    }

    public static void main(String[] args) {
        int port = 8888;
        new Server(port);
    }
}
