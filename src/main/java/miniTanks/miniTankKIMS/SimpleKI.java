package miniTanks.miniTankKIMS;

public class SimpleKI {


    private static final int FIELD_SIZE = 100;
    private static final int AIM_CORRECT_ITERATIONS = 6;
    private static final double NERVIOSITY_FACTOR = 1.5;
    private static final double NO_TARGET_TURN = 0.2;
    private static final double MAX_MISSILE_RANGE = 90;
    
    private static final Converter CONVERTER = new Converter();
    private static final double MISSILE_SPEED = 20;
    private static final double AIM_PERFECTION = 0.1;
    private ServerMessage message;
    private int tankCount;
    private Tank me;
    private Tank target;
    private Tank[] tanks;
    private int score;
    
    public SimpleKI() {
    }

    public String response(String line) {
        
        message = CONVERTER.toObject(line);
        
        double direction = turnToTarget();
        
//        if (me != null && me.getScore() != score) {
//            score = me.getScore();
//            System.out.println("Test" + NERVIOSITY_FACTOR + " score: " + score);
//        }
        
        return CONVERTER.toString(new GameMessage(1d, direction, Math.abs(direction ) < AIM_PERFECTION));
    }
    
    private double turnToTarget() {
        
        GameObject[] objects = message.getObjects();
        tanks = new Tank[objects.length];
        me = null;
        target = null;
        
        tankCount = 0;
        for (GameObject object : objects) {
             
            if (!object.isTank())
                continue;
            
            Tank tank = object.toTank();
            tanks[tankCount++] = tank;
            
            if (message.getId().equals(tank.getID())) {
                me = tank;
            }
                    
        }
        
        if (me == null)
            return NO_TARGET_TURN;

        double value = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < tankCount; i++) {
            
            Tank tank = tanks[i];
            
            if (me.getID().equals(tank.getID())) {
                continue;
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
        double targetDistance = 0;
        for (int i = 0; i <= AIM_CORRECT_ITERATIONS; i++) {
            targetDistance = Math.sqrt((Math.pow(target.getX() + targetXCorrection, 2d) + Math.pow(target.getY() + targetYCorrection, 2d)));
            targetYCorrection = Math.sin(target.getDirection()) * targetDistance * target.getSpeed() / MISSILE_SPEED;
            targetXCorrection = Math.cos(target.getDirection()) * targetDistance * target.getSpeed() / MISSILE_SPEED;
        }
        
        double targetDirection = Math.atan2(target.getY() + targetYCorrection, target.getX() + targetXCorrection);
        
        if (targetDirection < 0)
            targetDirection += 2*Math.PI;
        
        double direction = targetDirection - me.getDirection();
        if (direction > Math.PI)
            direction = direction - 2 * Math.PI;
        if (direction < -Math.PI)
            direction = direction + 2 * Math.PI;
        
        direction *= NERVIOSITY_FACTOR;
        
        if (direction > 1)
            direction = 1;
        if (direction < -1)
            direction = -1;
        
        return direction;
    }
    
    private double calculateValue(Tank possibleTarget) {

        if (possibleTarget == null)
            return Double.NEGATIVE_INFINITY;
        
        if (Math.sqrt((Math.pow(possibleTarget.getX(), 2d) + Math.pow(possibleTarget.getY(), 2d))) > MAX_MISSILE_RANGE)
            return Double.NEGATIVE_INFINITY;//more than shoot range away: don't shoot
        
        double targetDirection = Math.atan2(possibleTarget.getY(), possibleTarget.getX());
        double myDirection = me.getDirection() > Math.PI ? me.getDirection() - 2 * Math.PI : me.getDirection();
        
        double value = (5 - possibleTarget.getHealth()) / (Math.sqrt((Math.pow(possibleTarget.getX(), 2d) + Math.pow(possibleTarget.getY(), 2d))) * Math.abs(targetDirection - myDirection));
        
        if (possibleTarget.getScore() > me.getScore())
            return (5 - possibleTarget.getHealth()) * 3 / (Math.sqrt((Math.pow(possibleTarget.getX(), 2d) + Math.pow(possibleTarget.getY(), 2d))) * Math.abs(targetDirection - myDirection));
        
        return possibleTarget.getScore() > me.getScore() ? 3 * value : value;
    }

    public String ID() {
        return "" + NERVIOSITY_FACTOR;
    }

}
