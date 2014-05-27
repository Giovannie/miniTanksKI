package miniTanks.miniTankKIMS;

public class SimpleKI {

    private Converter converter = new Converter();
    private ServerMessage message;
    
    public SimpleKI() {
    }

    public String response(String line) {
        
        message = converter.toObject(line);
        
        double direction = message.giveNearestTankDirection();
        
        return converter.toString(new GameMessage(direction));
    }

}
