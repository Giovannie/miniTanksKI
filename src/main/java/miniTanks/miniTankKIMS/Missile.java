package miniTanks.miniTankKIMS;

public class Missile extends GameObject {

    public Missile(String missile, double x, double y,
            double direction, String owner) {
        super(null, missile, x, y, direction);
        this.owner = owner;
    }

}
