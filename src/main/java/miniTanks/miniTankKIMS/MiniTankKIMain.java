package miniTanks.miniTankKIMS;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MiniTankKIMain {

    private static final String HOST = "pick-your-axe.de";
    private static final int PORT = 2014;
    private static Socket socket;
    
    
    public static void main(String[] args) {
        
            while (true) {
                
                System.out.println("Startet tank");
        
            SimpleNetworkReader reader = null;
            SimpleNetworkWriter writer = null;
            
            try {
                socket = new Socket(HOST, PORT);
            } catch (UnknownHostException e) {
                     // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            reader = new SimpleNetworkReader(socket);
            writer = new SimpleNetworkWriter(socket);
            
            
            SimpleKI kI = new SimpleKI();
            
            writer.write("{\"name\":\"giovannieADVANCED\", \"pw\":\"blabla+" + kI.ID() + "\"}");
            String line = "";
            while((line = reader.read()) != null) {
//                    System.out.println("Server:" + line);
                writer.write(kI.response(line));
            }
                
            System.out.println("Ended tank.");
            
            try {
                socket.close();
                Thread.sleep(1000);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }
    }

}
