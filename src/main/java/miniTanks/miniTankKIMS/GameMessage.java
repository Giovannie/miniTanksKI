package miniTanks.miniTankKIMS;

public class GameMessage {

    private double accelerate;
    private double turn;
    private boolean shoot;
    private String name;
    private String pw;

    public GameMessage(double acc, double turn, boolean shoot) {
        accelerate = acc;
        this.turn = turn;
        this.shoot = shoot;
    }
}
