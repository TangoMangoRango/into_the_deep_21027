package org.firstinspires.ftc.teamcode.opmode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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


    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = hardwareMap.dcMotor.get("leftFront");
        DcMotor backLeft = hardwareMap.dcMotor.get("leftBack");
        DcMotor frontRight = hardwareMap.dcMotor.get("rightFront");
        DcMotor backRight = hardwareMap.dcMotor.get("rightBack");
        DcMotorEx slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");
        Servo leftLinkage = hardwareMap.get(Servo.class, "leftLinkage");
        Servo rightLinkage = hardwareMap.get(Servo.class, "rightLinkage");
        Servo leftIntake = hardwareMap.get(Servo.class, "leftIntake");
        Servo rightIntake = hardwareMap.get(Servo.class, "rightIntake");
        Servo intakeWrist = hardwareMap.get(Servo.class, "intakeWrist");
        Servo intakeClaw = hardwareMap.get(Servo.class, "intakeClaw");
        Servo outputClaw = hardwareMap.get(Servo.class, "outputClaw");
        Servo outputWrist = hardwareMap.get(Servo.class, "outputWrist");
        Servo leftOutput = hardwareMap.get(Servo.class, "leftOutput");
        Servo rightOutput = hardwareMap.get(Servo.class, "rightOutput");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftLinkage.setDirection(Servo.Direction.REVERSE);
        leftIntake.setDirection(Servo.Direction.REVERSE);
        rightOutput.setDirection(Servo.Direction.REVERSE);
        intakeClaw.setDirection(Servo.Direction.REVERSE);

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);



        //LETS THE "ROTATE()" METHOD DO THE REST
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        //STARTS AT FULL SPEED
        double speed = .8;

        boolean linkRetract = true;
        int armCounter = 0;
        boolean inClawClosed = false;
        boolean outClawClosed = true;

        leftLinkage.setPosition(0);
        rightLinkage.setPosition(0);
        intakeClaw.setPosition(0);
        outputClaw.setPosition(0.5);
        intakeWrist.setPosition(.4);
        leftOutput.setPosition(0);
        rightOutput.setPosition(0);
        outputWrist.setPosition(.1);

        waitForStart();

        if (isStopRequested()) return;


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
            //HALF-SPEED TOGGLe


            //SETS MOVEMENT OF ROBOT (FORWARD +- SIDE TO SIDE +- ROTATION)/TOTAL POWER
            frontLeft.setPower(((y + x + rx) / denominator) * speed);
            backLeft.setPower(((y - x + rx) / denominator) * speed);
            frontRight.setPower(((y - x - rx) / denominator) * speed);
            backRight.setPower(((y + x - rx) / denominator) * speed);

            //SET VARIABLE FOR TRIGGERS. THEY RETURN ONLY POSITIVE VALUES FROM 0-1 DEPENDING ON HOW
            //MUCH THEY'RE PRESSED
            double leftTrigger = gamepad2.left_trigger;
            double rightTrigger = gamepad2.right_trigger;

            //RIGHT GOES UP, LEFT GOES DOWN. DIVIDE BY 2 FOR HALF SPEED.
            double slidePower = (leftTrigger - rightTrigger);
            slideMotor.setPower(slidePower);

            if (gamepad1.y && currentGamepad1.y && !previousGamepad1.y) {
                if (speed == 0.8) {
                    speed = 0.4;
                } else {
                    speed = 0.8;
                }
            }


            //Linkage Extend and Retract
            if (gamepad2.left_bumper && currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
                if (linkRetract) {
                    leftLinkage.setPosition(0);
                    rightLinkage.setPosition(0);
                } else {
                    leftLinkage.setPosition(.25);
                    rightLinkage.setPosition(.25);
                }
                linkRetract = !linkRetract;
            }

            if (gamepad2.right_bumper && currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                if (armCounter % 3 == 0) {
                    leftIntake.setPosition(0.75);
                    rightIntake.setPosition(0.75);
                    intakeWrist.setPosition(0.1);
                } else if (armCounter % 3 == 1) {
                    leftIntake.setPosition(0.85);
                    rightIntake.setPosition(0.8);
                } else if (armCounter % 3 == 2) {
                    leftIntake.setPosition(0);
                    rightIntake.setPosition(0);
                    intakeWrist.setPosition(0.3);
                }
                armCounter++;
            }
            /*
            if(gamepad1.right_bumper && currentGamepad2.right_bumper && !previousGamepad2.right_bumper){

            }

             */

            if (gamepad2.x && currentGamepad2.x && !previousGamepad2.x) {
                leftOutput.setPosition(0.05);
                rightOutput.setPosition(0.05);
                outputWrist.setPosition(0.75);
            }

            if (gamepad2.a && currentGamepad2.a && !previousGamepad2.a) {
                if (inClawClosed) {
                    intakeClaw.setPosition(0);
                } else {
                    intakeClaw.setPosition(0.5);
                }
                inClawClosed = !inClawClosed;
            }


            if (gamepad2.b && currentGamepad2.b && !previousGamepad2.b) {
                if (outClawClosed) {
                    outputClaw.setPosition(0);
                } else {
                    outputClaw.setPosition(0.5);
                }
                outClawClosed = !outClawClosed;
            }

            if (gamepad1.b && currentGamepad1.b && !previousGamepad1.b) {

                outputClaw.setPosition(1);
                if (outClawClosed) outClawClosed = false;

                leftOutput.setPosition(0.375);
                rightOutput.setPosition(0.375);
                outputWrist.setPosition(0.05);

                /*leftLinkage.setPosition(0.05);
                rightLinkage.setPosition(0.05);
                leftIntake.setPosition(0.05);
                rightIntake.setPosition(0.05);
                intakeWrist.setPosition(0.4);*/

            }

            if (gamepad1.a && currentGamepad1.a && !previousGamepad1.a) {

                leftLinkage.setPosition(0.1);
                rightLinkage.setPosition(0.1);
                leftIntake.setPosition(0.1);
                rightIntake.setPosition(0.1);
                intakeWrist.setPosition(0.1);

            }

            telemetry.addData("Intake Claw Closed: ", inClawClosed);
            telemetry.addData("Output Claw Closed: ", outClawClosed);
            telemetry.update();
        }

    }


}