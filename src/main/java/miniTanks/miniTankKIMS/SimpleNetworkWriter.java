package miniTanks.miniTankKIMS;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleNetworkWriter {
    
    private BufferedWriter writer;
    
    public SimpleNetworkWriter(Socket socket) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void write(String message) {
        
//        System.out.println("Me:" + message);
        
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
