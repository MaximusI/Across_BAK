package Server;

import com.google.common.base.Ascii;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageASTM {


    public static char STX = '\002';
    public static char ETX = '\003';
    public static char ETB = '\027';
    public static char EOT = '\004';
    public static char ENQ = '\005';
    public static char ACK = '\006';
    public static char NAK = '\025';
    public static char CR = '\r';
    public static char LF = '\n';
    public static char MOR = '>';
    public static char FS = '\034';
    public static char GS = '\035';
    public static char RS = '\036';
    public static char SFS = '\027';
    public static char VT = 0x0B;
    private static MessageASTM instance;
    public static ArrayList<String> incomingPackageMessage = new ArrayList<>();
    public static ArrayList<String> outgoingPackageMessage = new ArrayList<>();
    private static boolean isQuery = false;

    private MessageASTM(){

    }

    //  Pattern Singleton
    public static MessageASTM getInstance() {
        if (instance == null) {
            instance = new MessageASTM();
        }
        return instance;
    }

    public static void setIsQuery(boolean isQuery) {
        MessageASTM.isQuery = isQuery;
    }

    public static boolean getIsQuery() {
        return isQuery;
    }

    public static void sendOutMessage(DataInputStream dataIn, DataOutputStream dataOut) {
        // Add test message
        outgoingPackageMessage.add(sendStrMessageFromHOST("1H|\\^&|||LIS"));
        outgoingPackageMessage.add(sendStrMessageFromHOST("2P|1|3|||KOTIVETS MIKHAIL ANDREEVICH||20181227|F"));
        outgoingPackageMessage.add(sendStrMessageFromHOST("3O|1|505871.2^KLPN||||20190106|20190106|||||||||||||||"));
        outgoingPackageMessage.add(sendStrMessageFromHOST("4L|1|F"));

        int clientMessage = -1;
        int coutnOfStrMessageFromHOST = 0;
        try {
            // Sending answer from LIS
            dataOut.write(ENQ);
            do {
                clientMessage = dataIn.read();

                    if (clientMessage == ACK && coutnOfStrMessageFromHOST < outgoingPackageMessage.size())
                        dataOut.writeBytes(outgoingPackageMessage.get(coutnOfStrMessageFromHOST++));

            } while (coutnOfStrMessageFromHOST < outgoingPackageMessage.size());
            if (dataIn.read() == ACK)
                dataOut.write(EOT);
            outgoingPackageMessage.clear();
            setIsQuery(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sendStrMessageFromHOST(String retmsg) {
        retmsg = STX + retmsg + CR + ETX + SupportCls.getCheckSum(retmsg) + CR + LF;
        return retmsg;
    }


    public static void processingMessageIn(DataInputStream dataIn, DataOutputStream dataOut) {
        int clientIntMessage = -1;
        try {
            // Sending answer from LIS
            dataOut.write(ACK);
            do {
                String str = "";
                clientIntMessage = dataIn.read();
                while (clientIntMessage != LF && clientIntMessage != EOT) {
                    clientIntMessage = dataIn.read();
                    str += Character.toString((char) clientIntMessage);
                }
                if (clientIntMessage != EOT) {
                    incomingPackageMessage.add(str);
                    dataOut.write(ACK);
                }
            } while (clientIntMessage != EOT);
            SupportCls.printArray(incomingPackageMessage);

            if (incomingPackageMessage.get(1).toCharArray()[1] == 'Q') {
                MessageASTM.setIsQuery(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
