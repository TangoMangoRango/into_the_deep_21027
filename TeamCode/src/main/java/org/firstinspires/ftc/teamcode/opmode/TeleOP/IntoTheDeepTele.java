package org.firstinspires.ftc.teamcode.opmode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystem.HorizSlide;


@TeleOp
public class IntoTheDeepTele extends LinearOpMode {


    //quang is stinky
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotorEx slideMotor;
    private Servo leftLinkage;
    private Servo rightLinkage;
    private Servo bottomWrist;

    //private HorizSlide slidey;
    /*
    private DcMotor armMotor;
    private Servo wheelServo;
    private Servo rotationServo;

     quang, it is shower time
     is this the real life
     is this just fantasy*/

    @Override
    public void runOpMode() throws InterruptedException {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");
        leftLinkage = hardwareMap.get(Servo.class, "leftLinkage");
        rightLinkage = hardwareMap.get(Servo.class, "rightLinkage");
        bottomWrist = hardwareMap.get(Servo.class, "bottomWrist");


        //RIGHT MOTORS MUST BE ROTATED BECAUSE THE GEARS CHANGE THEIR ROTATION
        //ALSO I DID THE MATH WRONG DO THIS ACCOUNTS FOR THAT
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        //BRAKES THE ROBOT WHEN THERE IS NO POWER PROVIDED (NO SLIPPY-SLIDING)
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        if (isStopRequested()) return;

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
        double speed = .8;

        double linkagePos = -1;

        leftLinkage.setPosition(0);
        rightLinkage.setPosition(1);
        bottomWrist.setPosition(0);

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
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            //HALF-SPEED TOGGLE
            if (gamepad1.x && !previousGamepad1.x && currentGamepad1.x) {
                if (speed != .8)
                    speed = .8;
                else
                    speed = 0.4;
            }


            //SETS MOVEMENT OF ROBOT (FORWARD +- SIDE TO SIDE +- ROTATION)/TOTAL POWER
            leftFront.setPower(((y + x + rx) / denominator) * speed);
            leftBack.setPower(((y - x + rx) / denominator) * speed);
            rightFront.setPower(((y - x - rx) / denominator) * speed);
            rightBack.setPower(((y + x - rx) / denominator) * speed);

            //SET VARIABLE FOR TRIGGERS. THEY RETURN ONLY POSITIVE VALUES FROM 0-1 DEPENDING ON HOW
            //MUCH THEY'RE PRESSED
            double leftTrigger = gamepad2.left_trigger;
            double rightTrigger = gamepad2.right_trigger;

            //RIGHT GOES UP, LEFT GOES DOWN. DIVIDE BY 2 FOR HALF SPEED.
            double slidePower = (leftTrigger - rightTrigger);
            slideMotor.setPower(slidePower);




            if (gamepad2.y && currentGamepad2.y && !previousGamepad2.y) {
                if (linkagePos == 1) {
                    leftLinkage.setPosition(0);
                    rightLinkage.setPosition(1);
                } else {
                    leftLinkage.setPosition(1);
                    rightLinkage.setPosition(0);
                }
                linkagePos = -linkagePos;
            }
            //                telemetry.addData("leftpos",leftLinkage.getPosition());
            //                telemetry.addData("rightpos",rightLinkage.getPosition());

            if (gamepad2.x && currentGamepad2.x && !previousGamepad2.x) {
                if (bottomWrist.getPosition() == 0) {
                    bottomWrist.setPosition(1);
                } else {
                    bottomWrist.setPosition(0);
                }
            }
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