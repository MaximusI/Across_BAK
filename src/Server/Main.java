package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.google.common.base.*;

public class Main {

    public static void main(String[] args) {
        FileOutputStream logASTM = null;
        try {
            logASTM = new FileOutputStream(new File(".\\files\\log.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ServerSocket serv = null;
        Socket sock = null;
        try {
            serv = new ServerSocket(8189);
            System.out.println("Сервер запущен, ожидаем подключения...");
            sock = serv.accept();
            System.out.println("Клиент подключился");
            DataInputStream dataIn = new DataInputStream(sock.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(sock.getOutputStream());
            //ArrayList<String> messageList = new ArrayList<>();
            int clientIntMessage;
            while (true) {
                clientIntMessage = dataIn.read();
                if (clientIntMessage == Ascii.ENQ) {
                    MessageASTM.processingMessageIn(dataIn, dataOut);
                    if (MessageASTM.getIsQuery() == true)
                        MessageASTM.sendOutMessage(dataIn, dataOut);
                    MessageASTM.incomingPackageMessage.clear();
                }
            }
        } catch(IOException e){
            System.out.println("Ошибка инициализации сервера");
                try {
                    serv.close();
                    sock.close();
                    logASTM.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
    }
}

