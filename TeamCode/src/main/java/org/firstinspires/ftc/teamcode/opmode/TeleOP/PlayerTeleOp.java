package org.firstinspires.ftc.teamcode.opmode.TeleOP;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class PlayerTeleOp extends LinearOpMode{
    @Override
    public void runOpMode(){
        // Declare our motors
        // Make sure your ID's match your configuration
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
        Servo rightOutput = hardwareMap.get(Servo.class, "leftOutput");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftLinkage.setDirection(Servo.Direction.REVERSE);
        leftIntake.setDirection(Servo.Direction.REVERSE);
        intakeClaw.setDirection(Servo.Direction.REVERSE);

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        double speed = .8;

        double linkagePos = -1;
        int armCounter = 0;
        int claw = 0;
        int wrist = 0;

        leftLinkage.setPosition(0);
        rightLinkage.setPosition(0);
        intakeWrist.setPosition(.4);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);



            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = ((rotY + rotX + rx) / denominator) * speed;
            double backLeftPower = ((rotY - rotX + rx) / denominator) * speed;
            double frontRightPower = ((rotY - rotX - rx) / denominator) * speed;
            double backRightPower = ((rotY + rotX - rx) / denominator) * speed;

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            //SET VARIABLE FOR TRIGGERS. THEY RETURN ONLY POSITIVE VALUES FROM 0-1 DEPENDING ON HOW
            //MUCH THEY'RE PRESSED
            double leftTrigger = gamepad2.left_trigger;
            double rightTrigger = gamepad2.right_trigger;

            //RIGHT GOES UP, LEFT GOES DOWN. DIVIDE BY 2 FOR HALF SPEED.
            double slidePower = (leftTrigger - rightTrigger);
            slideMotor.setPower(slidePower);





            //IF LINKAGEPOS = 1, THEN LINKAGES ARE EXTENDED. IF == -1, LINKAGES ARE RETRACTED. TOGGLES ACCORDINGLY.
            //POSITION 0 IS HOME POSITION
            if (gamepad2.y && currentGamepad2.y && !previousGamepad2.y) {
                if (linkagePos == 1) {
                    leftLinkage.setPosition(0);
                    rightLinkage.setPosition(0);
                } else {
                    leftLinkage.setPosition(.3);
                    rightLinkage.setPosition(.3);
                }
                linkagePos = -linkagePos;
            }

            //
            if (gamepad2.x && currentGamepad2.x && !previousGamepad2.x) {
               if (wrist % 2 == 0) {
                    intakeWrist.setPosition(0.1);
                } else {
                    intakeWrist.setPosition(0.3);
                }
                wrist++;

            }

            if (gamepad2.a && currentGamepad2.a && !previousGamepad2.a) {
                if(armCounter % 3 == 0) {
                    leftIntake.setPosition(.7);
                    rightIntake.setPosition(.7);
                } else if(armCounter % 3 == 1) {
                    leftIntake.setPosition(.8);
                    rightIntake.setPosition(.8);
                } else if(armCounter % 3 == 2){
                    leftIntake.setPosition((0));
                    rightIntake.setPosition(0);
                }
                armCounter++;
            }

            if (gamepad2.b && currentGamepad2.b && !previousGamepad2.b) {
                if(claw % 2 == 0) {
                    intakeClaw.setPosition(0);
                } else {
                    intakeClaw.setPosition(.3);
                }
                claw++;
            }


            if (gamepad1.x && currentGamepad1.x && !previousGamepad1.x) {
                if (wrist % 2 == 0) {
                    outputWrist.setPosition(0);
                } else {
                    outputWrist.setPosition(0.3);
                }
                wrist++;

            }


            telemetry.update();
        }
    }
}
