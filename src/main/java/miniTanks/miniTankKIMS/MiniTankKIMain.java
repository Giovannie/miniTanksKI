package miniTanks.miniTankKIMS;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MiniTankKIMain {

    private static final String IP = "141.3.235.98";
    private static final int PORT = 2014;
    private static Socket socket;
    
    public static void main(String[] args) {
        
        System.out.println("Starting tank");
        try {
            socket = new Socket(IP, PORT);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        SimpleNetworkReader reader = new SimpleNetworkReader(socket);
        SimpleNetworkWriter writer = new SimpleNetworkWriter(socket);
        SimpleKI kI = new SimpleKI();
        
        writer.write("{\"name\":\"giovannie\", \"pw\":\"blablabla\"}");
        String line = "";
        while((line = reader.read()) != null) {
//            System.out.println("Server:" + line);
            writer.write(kI.response(line));
        }
        
        System.out.println("Ended tank.");
    }

}
