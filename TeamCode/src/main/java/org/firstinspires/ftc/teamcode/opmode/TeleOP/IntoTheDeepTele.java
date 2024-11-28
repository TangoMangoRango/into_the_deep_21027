package org.firstinspires.ftc.teamcode.opmode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class IntoTheDeepTele extends LinearOpMode {


//quang is stinky
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private Servo leftLinkage;
    private Servo rightLinkage;
    /*
    private DcMotor armMotor;
    private Servo wheelServo;
    private Servo rotationServo;

     quang, it is shower time
     is this the real life
     is this just fantasy*/

    @Override

    public void runOpMode() {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftLinkage = hardwareMap.get(Servo.class,"leftLinkage");
        rightLinkage = hardwareMap.get(Servo.class,"rightLinkage");
        /*
        armMotor = hardwareMap.dcMotor.get("armMotor");
        slideMotor = hardwareMap.dcMotor.get("slideMotor");
        rotationServo = hardwareMap.get(Servo.class,"rotationServo");
        wheelServo = hardwareMap.get(Servo.class, "wheelServo");

         */
        //RIGHT MOTORS MUST BE ROTATED BECAUSE THE GEARS CHANGE THEIR ROTATION
        //ALSO I DID THE MATH WRONG DO THIS ACCOUNTS FOR THAT
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        //BRAKES THE ROBOT WHEN THERE IS NO POWER PROVIDED (NO SLIPPY-SLIDING)
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        //LETS THE "ROTATE()" METHOD DO THE REST
        rotate();
        //UPDATES ROBOT'S POSITION (BUT WE DON'T REALLY USE IT)

    }

    private void rotate() {


        //THESE THINGS ARE SO THE TOGGLE WORKS
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        //STARTS AT FULL SPEED
        double speed = 1;

        double armPos = 0;

        boolean armPressed = false;

        leftLinkage.setPosition(0);
        rightLinkage.setPosition(1);

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                //LETS YOU SEE ROBOT'S POSITION IN THE DASHBOARD
                previousGamepad1.copy(currentGamepad1);
                previousGamepad2.copy(currentGamepad2);

        /* STORES THE GAMEPAD INPUT FROM THE PREVIOUS LOOP
        STOPS GAMEPAD VALUES FROM SWITCHING BETWEEN BEING USED AND
        STORED IN PREVIOUSGAMEPAD1/2 */
                currentGamepad1.copy(gamepad1);
                currentGamepad2.copy(gamepad2);
                //FORWARD AND BACKWARD (NEGATIVE BC Y IS REVERSED)(DON'T ASK WHY)
                double y = -gamepad1.left_stick_y;
                //SETS RIGHT STICK ROTATION (MULTIPLIED TO GET THE FULL RANGE OF ROTATION)
                double rx = gamepad1.right_stick_x * 1.1;
                //SIDE-TO-SIDE
                double x = gamepad1.left_stick_x;

                //GETS DENOMINATOR THAT WILL DIVIDE BY TOTAL POWER (SO ROBOT DOESN'T GO SUPER-FAST)
                double denominator = Math.max(Math.abs(y)+Math.abs(x)+Math.abs(rx),1);
                //HALF-SPEED TOGGLE
                if (gamepad1.x && !previousGamepad1.x && currentGamepad1.x){
                    if (speed != 1)
                        speed = 1;
                    else
                        speed = 0.5;
                }


                //SETS MOVEMENT OF ROBOT (FORWARD +- SIDE TO SIDE +- ROTATION)/TOTAL POWER
                leftFront.setPower(((y + x + rx) / denominator)*speed);
                leftBack.setPower(((y - x + rx) / denominator)*speed);
                rightFront.setPower(((y - x - rx) / denominator)*speed);
                rightBack.setPower(((y + x - rx) / denominator)*speed);

                //SET VARIABLE FOR TRIGGERS. THEY RETURN ONLY POSITIVE VALUES FROM 0-1 DEPENDING ON HOW
                //MUCH THEY'RE PRESSED
                double leftTrigger = gamepad2.left_trigger;
                double rightTrigger = gamepad2.right_trigger;

                //RIGHT GOES UP, LEFT GOES DOWN. DIVIDE BY 2 FOR HALF SPEED.
                double powerTotal = ((leftTrigger/3) - rightTrigger);
                //FEEDS POWER TO ARM
                //armMotor.setPower(powerTotal);
                //SETS SLIDEPOWER TO LEFT STICK Y
                double slidePower = gamepad2.left_stick_y;
                //SETS THE POWER EQUAL TO SLIDE POWER
                //slideMotor.setPower(slidePower);




                if (gamepad2.y && currentGamepad2.y && !previousGamepad2.y) {
                    leftLinkage.setPosition(leftLinkage.getPosition()+0.1);
                    rightLinkage.setPosition(rightLinkage.getPosition()-0.1);
                }
                if (gamepad2.x && currentGamepad2.x && !previousGamepad2.x) {
                    leftLinkage.setPosition(leftLinkage.getPosition()-0.1);
                    rightLinkage.setPosition(rightLinkage.getPosition()+0.1);
                }
                telemetry.addData("leftpos",leftLinkage.getPosition());
                telemetry.addData("rightpos",rightLinkage.getPosition());
                telemetry.update();


                /*
                double wheelPos = wheelServo.getPosition();

                //CR SERVO: POSITION OF 0.5 = STOP, <0.5 = REVERSE, >0.5 = FORWARD
                //IF GAMEPAD IS DOUBLE-CLICKED
                if (gamepad1.b && currentGamepad1.b && !previousGamepad1.b) {
                    if (wheelPos != 0.7)
                        wheelServo.setPosition(0.7);
                    else
                        wheelServo.setPosition(0.5);
                }
                if (gamepad1.a && currentGamepad1.a && !previousGamepad1.a) {
                    if (wheelPos != 0.3)
                        wheelServo.setPosition(0.3);
                    else
                        wheelServo.setPosition(0.5);
                }
                */

            }
        }
    }
}

