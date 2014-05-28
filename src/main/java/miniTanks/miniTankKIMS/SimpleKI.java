package miniTanks.miniTankKIMS;

public class SimpleKI {


    private static final int FIELD_SIZE = 100;
    private static final int AIM_CORRECT_ITERATIONS = 4;
    private static final double NERVIOSITY_FACTOR = 2;
    private static final double NO_TARGET_TURN = 0.2;
    private static final double MAX_MISSILE_RANGE = 90;
    
    private static final Converter CONVERTER = new Converter();
    private ServerMessage message;
    
    public SimpleKI() {
    }

    public String response(String line) {
        
        message = CONVERTER.toObject(line);
        
        double direction = turnToTarget();
        
        return CONVERTER.toString(new GameMessage(1d, direction, Math.abs(direction ) < 0.15));
    }
    
    private double turnToTarget() {
        
        GameObject[] objects = message.getObjects();
        GameObject me = null;
        GameObject target = null;
        
                

        for (GameObject object : objects) {
                    
            if (message.getId().equals(object.getID())) {
                me = object;
                break;
            }
                    
        }
        
        if (me == null)
            return NO_TARGET_TURN;

        double value = Double.NEGATIVE_INFINITY;
        for (GameObject object : objects) {
            
            if (object.getID() == null || me.getID().equals(object.getID())) {
                continue;
            }
            
            object.setX((object.getX() - me.getX()));
            if (object.getX() > FIELD_SIZE/2)
                object.setX(object.getX() - FIELD_SIZE);
            if (object.getX() < -FIELD_SIZE/2)
                object.setX(FIELD_SIZE + object.getX());
            
            object.setY((object.getY() - me.getY()));
            if (object.getY() > FIELD_SIZE/2)
                object.setY(object.getY() - FIELD_SIZE);
            if (object.getY() < -FIELD_SIZE/2)
                object.setY(FIELD_SIZE + object.getY());
            
            
            double targetValue = calculateValue(object, me);
            if (value < targetValue) {
                value = targetValue;
                target = object;
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
            targetYCorrection = Math.sin(target.getDirection()) * targetDistance / target.getSpeed();
            targetXCorrection = Math.cos(target.getDirection()) * targetDistance / target.getSpeed();//
        }
        
        double targetDirection = Math.atan2(target.getY() + targetYCorrection, target.getX() + targetXCorrection);
        
        if (targetDirection < 0)
            targetDirection += 2*Math.PI;
        
        double direction = targetDirection - me.getDirection();
        direction *= NERVIOSITY_FACTOR * direction;
        
        if (direction > 1)
            direction = 1;
        if (direction < -1)
            direction = -1;
        
        return direction;
    }
    
    private double calculateValue(GameObject target, GameObject me) {

        if (target == null)
            return Double.NEGATIVE_INFINITY;
        
        if (Math.sqrt((Math.pow(target.getX(), 2d) + Math.pow(target.getY(), 2d))) > MAX_MISSILE_RANGE)
            return Double.NEGATIVE_INFINITY;//more than shoot range away: don't shoot
        
        double targetDirection = Math.atan2(target.getY(), target.getX());
        double myDirection = me.getDirection() > Math.PI ? me.getDirection() - 2 * Math.PI : me.getDirection();
        
        if (target.getScore() > me.getScore())
            return (5 - target.getHealth()) * 3 / (Math.sqrt((Math.pow(target.getX(), 2d) + Math.pow(target.getY(), 2d))) * Math.abs(targetDirection - myDirection));
        
        return (5 - target.getHealth()) * 1 / (Math.sqrt((Math.pow(target.getX(), 2d) + Math.pow(target.getY(), 2d))) * Math.abs(targetDirection - myDirection));
    }

}
