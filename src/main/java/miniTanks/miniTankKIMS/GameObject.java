package miniTanks.miniTankKIMS;

public class GameObject {

    private String tank;
    private double x;
    private double y;
    private double direction;
    private double speed;
    private double cooldown;
    private String missile;
    private String owner;
    private int score;
    private int health;

    public Object getID() {
        return tank;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
   

    public double getDirection() {
        return direction;
    }

    public int getScore() {
        return score;
    }
    
    public int getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }
}
