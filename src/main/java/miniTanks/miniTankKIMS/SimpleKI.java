package miniTanks.miniTankKIMS;

import java.util.LinkedList;

public class SimpleKI {

    //Factors might not be perfect. Need statistical base to find optimum.
    private static final int AIM_CORRECT_ITERATIONS = 6;
    private static final double NERVIOSITY_FACTOR = 1.5;
    private static final double UNKNOWN_FACTOR = 1;// 1 / NERVIOSITY_FACTOR?;
    private static final double STAY_ON_TARGET_FACTOR = 1.5;
    private static final double COLLISION_DETECTION_RANGE = 50;
    private static final int COLLISION_CORRECTION_ITERATION = 6;
    
    private static final boolean TANK_MISSILE_COLISION_CALCULATION = true;
    
    private static final double NO_TARGET_TURN = 0.2;
    
    private static final int FIELD_SIZE = 100;
    private static final double MAX_MISSILE_RANGE = 90;
    private static final Converter CONVERTER = new Converter();
    private static final double MISSILE_SPEED = 20;
    private static final double EPSILON = 1E-10;
    
    private ServerMessage message;
    private int missileCount;
    private Tank me;
    private Tank target;
    private LinkedList<Tank> tanks;
    private Missile[] missiles;
    private double distance;
    private boolean shoot;
    
    public SimpleKI() {
    }

    public String response(String line) {
        
        message = CONVERTER.toObject(line);
        
        double direction = turnToTarget();
        
//        if (me != null && me.getScore() != score) {
//            score = me.getScore();
//            System.out.println("Test" + NERVIOSITY_FACTOR + " score: " + score);
//        }
        
        return CONVERTER.toString(new GameMessage(1d, direction, shoot));
    }
    
    private double turnToTarget() {
        
        GameObject[] objects = message.getObjects();
        tanks = new LinkedList<Tank>();
        missiles = new Missile[objects.length];
        me = null;
        target = null;
        
        missileCount = 0;
        for (GameObject object : objects) {
             
            if (!object.isTank()) {
                Missile missile = object.toMissile();
                missiles[missileCount++] = missile;
                continue;
            }
            
            Tank tank = object.toTank();
            tanks.add(tank);
            
            if (message.getId().equals(tank.getID())) {
                me = tank;
            }
                    
        }
        
        if (me == null)
            return NO_TARGET_TURN;
        

        double value = Double.NEGATIVE_INFINITY;
        for (Tank tank : tanks) {
            
            if (me.getID().equals(tank.getID())) {
                continue;
            }
            
            if (TANK_MISSILE_COLISION_CALCULATION) {
                for (int i = 0; i < missileCount; i++) {//Faster: sort missiles and only consider possible ones
                    Missile missile = missiles[i];
                    if (probableCollision(tank, missile))
                        tank.setHealth(tank.getHealth() - 1);
                }
            }
            
            tank.setX((tank.getX() - me.getX()));
            if (tank.getX() > FIELD_SIZE/2)
                tank.setX(tank.getX() - FIELD_SIZE);
            if (tank.getX() < -FIELD_SIZE/2)
                tank.setX(FIELD_SIZE + tank.getX());
            
            tank.setY((tank.getY() - me.getY()));
            if (tank.getY() > FIELD_SIZE/2)
                tank.setY(tank.getY() - FIELD_SIZE);
            if (tank.getY() < -FIELD_SIZE/2)
                tank.setY(FIELD_SIZE + tank.getY());
            
            
            double targetValue = calculateValue(tank);
            if (value < targetValue) {
                value = targetValue;
                target = tank;
            }
            
        }
        
        if (target == null)
            return NO_TARGET_TURN;
        
        //correct aiming
        double targetYCorrection = 0;
        double targetXCorrection = 0;
        for (int i = 0; i <= AIM_CORRECT_ITERATIONS; i++) {
            distance = Math.sqrt((Math.pow(target.getX() + targetXCorrection, 2d) + Math.pow(target.getY() + targetYCorrection, 2d)));
            targetYCorrection = Math.sin(target.getDirection()) * distance * target.getSpeed() / MISSILE_SPEED;
            targetXCorrection = Math.cos(target.getDirection()) * distance * target.getSpeed() / MISSILE_SPEED;
        }
        
        double targetDirection = Math.atan2(target.getY() + targetYCorrection, target.getX() + targetXCorrection);
        double neededDirection = Math.atan2(1, distance - 1);
        
        if (targetDirection < 0)
            targetDirection += 2*Math.PI;
        
        double direction = targetDirection - me.getDirection();
        if (direction > Math.PI)
            direction = direction - 2 * Math.PI;
        if (direction < -Math.PI)
            direction = direction + 2 * Math.PI;
        
        shoot = Math.abs(direction) <= Math.abs(neededDirection);
        
        direction *= NERVIOSITY_FACTOR;
        
        if (direction > 1)
            direction = 1;
        if (direction < -1)
            direction = -1;
        
        return direction;
    }
    
    private boolean probableCollision(Tank tank, Missile missile) {


        double correctedX = tank.getX();
        double correctedY = tank.getY();

        double yCorrection = 0;
        double xCorrection = 0;
        double missileDistance = Math.sqrt((Math.pow(tank.getX() + xCorrection - missile.getX(), 2d) + Math.pow(tank.getY() + yCorrection - missile.getY(), 2d)));
        for (int i = 0; i <= COLLISION_CORRECTION_ITERATION; i++) {
            missileDistance = Math.sqrt((Math.pow(tank.getX() + xCorrection - missile.getX(), 2d) + Math.pow(tank.getY() + yCorrection - missile.getY(), 2d)));
            yCorrection = Math.sin(tank.getDirection()) * missileDistance * tank.getSpeed() / MISSILE_SPEED;
            xCorrection = Math.cos(tank.getDirection()) * missileDistance * tank.getSpeed() / MISSILE_SPEED;
        }
        
        correctedX = tank.getX() + xCorrection;
        correctedY = tank.getY() + yCorrection;
        
        
        double normX = missile.getX() - correctedX;
        if (normX > FIELD_SIZE/2)
            normX = normX - FIELD_SIZE;
        if (normX < -FIELD_SIZE/2)
            normX = FIELD_SIZE + normX;
        
        double normY = missile.getY() - correctedY;
        if (normY > FIELD_SIZE/2)
            normY = normY - FIELD_SIZE;
        if (normY < -FIELD_SIZE/2)
            normY = FIELD_SIZE + normY;
        
        
        if (Math.sqrt(Math.pow(normX, 2d) + Math.pow(normY, 2d)) > COLLISION_DETECTION_RANGE)
            return false;
        
        normX += Math.sin(missile.getDirection()) * missileDistance;
        normY += Math.cos(missile.getDirection()) * missileDistance;
        
        if (Math.sqrt(Math.pow(normX, 2d) + Math.pow(normY, 2d)) < 1) {
            
            return true;
            
        }
                
        return false;
    }

    private double calculateValue(Tank possibleTarget) {
        
        if (possibleTarget == null)
            return Double.NEGATIVE_INFINITY;
        
        if (possibleTarget.getHealth() < 1)
            return Double.NEGATIVE_INFINITY;
        
        if (Math.sqrt((Math.pow(possibleTarget.getX(), 2d) + Math.pow(possibleTarget.getY(), 2d))) > MAX_MISSILE_RANGE)
            return Double.NEGATIVE_INFINITY;//more than shoot range away: don't shoot
        
        double targetDirection = Math.atan2(possibleTarget.getY(), possibleTarget.getX());
        double myDirection = me.getDirection() > Math.PI ? me.getDirection() - 2 * Math.PI : me.getDirection();
        
        double value = 1 / (Math.sqrt((Math.pow(possibleTarget.getX(), 2d) + Math.pow(possibleTarget.getY(), 2d)) * Math.pow(targetDirection - myDirection, 2d)) * possibleTarget.getHealth());
        
        if (this.target == possibleTarget)
            value *= STAY_ON_TARGET_FACTOR;
        
        return possibleTarget.getScore() > me.getScore() ? 3 * value : value;
    }

    public String ID() {
        return "" + NERVIOSITY_FACTOR;
    }

}
