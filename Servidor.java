/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API_Wikipedia.Cliente_Servidor;

/**
 *
 * @author Renan
 */

import API_Wikipedia.API_Wikipedia;
import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Scanner;
public class Servidor{
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    API_Wikipedia api;
    Servidor(){}
    void run()
    {
        try{
            //1. creating a server socket
            providerSocket = new ServerSocket(2004, 10);
            //2. Wait for connection
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful.\nserver>Por favor ingrese el nombre de su equipo");
            
            
            //4. The two parts communicate via the input and output streams
            try{
            message = (String)in.readObject();
            API_Wikipedia.setEquipo(message);
            System.out.println("client>" + message);
            sendMessage("Eliga entre las opciones:\n\t1. Año Establecido\n\t2. Liga\n\t3. Colores del equipo\n\t4. Entrenador\n\t5. Gerente general");
            }
            catch(IOException | ClassNotFoundException e){
                System.out.println(e);
            }
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("client>" + message);
                    System.out.print("server> ");
                    switch (message){
                        case "1" :
                            sendMessage(API_Wikipedia.hacer(1));
                            break;
                        case "2":
                            sendMessage(API_Wikipedia.hacer(2));
                            break;
                        case "3":
                            sendMessage(API_Wikipedia.hacer(3));
                            break;
                        case "4":
                            sendMessage(API_Wikipedia.hacer(4));
                            break;
                        case "5":
                            sendMessage(API_Wikipedia.hacer(5));
                            break;
                                
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(!message.equals("bye"));
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    public static void main(String args[])
    {
        Servidor server = new Servidor();
        while(true){
            server.run();
        }
    }
    public void pickHobbie(){
        int randomNum = 1 + (int)(Math.random() * 4); 
        switch (randomNum){
            case 1:
                sendMessage("Mi hobbie favorita es Jugar futbol.");
                break;
            case 2:
                sendMessage("Mi hobbie favorita es Programar en Java.");
                break;
            case 3:
                sendMessage("Mi hobbie favorita es Jugar videojuegos.");
                break;
            case 4:
                sendMessage("Mi hobbie favorita es pasar tiempo con amigos.");
                break;
        }
    }
}

