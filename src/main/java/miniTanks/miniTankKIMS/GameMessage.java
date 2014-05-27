package miniTanks.miniTankKIMS;

public class GameMessage {

    private double accelerate;
    private double turn;
    private boolean shoot = false;
    private String name;
    private String pw;

    public GameMessage(double direction) {
        accelerate = 1;
        turn = direction;
        
        if (Math.abs(direction ) < 0.15)
            shoot = true;
    }
}
