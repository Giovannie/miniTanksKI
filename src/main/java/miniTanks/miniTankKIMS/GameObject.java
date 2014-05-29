package miniTanks.miniTankKIMS;

public class GameObject {

    private String tank;
    private double x;
    private double y;
    private double direction;
    protected double speed;
    protected double cooldown;
    private String missile;
    protected String owner;
    protected int score;
    protected int health;

    public GameObject(String tank, String missile, double x, double y,
            double direction) {
        this.missile = missile;
        this.tank = tank;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public boolean isTank() {
        return tank != null;
    }
    
    public boolean isMissile() {
        return missile != null;
    }
    
    public Object getID() {
        return tank == null ? missile : tank;
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
    
    public Tank toTank() {
        
        if (this.isMissile())
            return null;
        
        return new Tank(tank, x, y, direction, speed, cooldown, score, health);
        
    }
        
    public Missile toMissile() {
        
        if (this.isTank())
            return null;
        
        return new Missile(missile, x, y, direction, owner);
    }
}
