package miniTanks.miniTankKIMS;

public class Tank extends GameObject {


    public Tank(String tank, double x, double y, double direction,
            double speed, double cooldown, int score, int health) {
        super(tank, null, x, y, direction);
        this.speed = speed;
        this.cooldown = cooldown;
        this.score = score;
        this.health = health;
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
    
    public double getCooldown() {
        return cooldown;
    }
}
