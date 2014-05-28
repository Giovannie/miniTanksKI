package miniTanks.miniTankKIMS;

public class ServerMessage {
    
    private String id;
    private GameObject[] msg;
    
    public ServerMessage() {
        
    }
    
    public GameObject[] getObjects() {
        return msg;
    }

    public String getId() {
        return id;
    }
}
