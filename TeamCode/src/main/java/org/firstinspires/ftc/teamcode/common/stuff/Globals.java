package org.firstinspires.ftc.teamcode.common.stuff;

import org.firstinspires.ftc.teamcode.common.stuff.Location;

public class Globals {

    public static final double LINKAGE_POSITION_INIT = 0.0;
    public static final double LINKAGE_POSITION_EXTEND = 0.5;
    public static final double LINKAGE_POSITION_RETRACT = 0.07;

    //0.89 for 180 degrees servo 0.534 (160.2 degrees)
    public static final double INTAKE_ARM_POSITION_EXTEND = 0.605;
    public static final double INTAKE_ARM_POSITION_RETRACT = 0.0;

    public static final double INTAKE_WRIST_POSITION_EXTEND = 0.08;
    public static final double INTAKE_WRIST_POSITION_RETRACT = 0.35;

    public static final double OUTTAKE_ARM_POSITION_EXTEND = 0.05;
    public static final double OUTTAKE_ARM_POSITION_RETRACT = 0.43;

    public static final double OUTTAKE_WRIST_POSITION_EXTEND = 0.6;
    public static final double OUTTAKE_WRIST_POSITION_RETRACT = 0;

    public static final double CLAW_OPEN = 0.0;
    public static final double CLAW_CLOSE = 0.5;

    public static Location SIDE = Location.FAR;
    /**
     * Match constants.
     */
    public static Location ALLIANCE = Location.RED;
    public static boolean IS_AUTO = false;

    /**
     * Robot State Constants
     */
    public static boolean IS_SCORING = false;
    public static boolean IS_INTAKING = false;

    public static void startScoring() {
        IS_SCORING = true;
        IS_INTAKING = false;
    }

    public static void stopScoring(){
        IS_SCORING = false;
        IS_INTAKING = false;
    }

    public static void startIntaking() {
        IS_SCORING = false;
        IS_INTAKING = true;
    }

    public static void stopIntaking() {
        IS_SCORING = false;
        IS_INTAKING = false;
    }
}