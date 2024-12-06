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
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeft = hardwareMap.dcMotor.get("leftFront");
        DcMotor backLeft = hardwareMap.dcMotor.get("leftBack");
        DcMotor frontRight = hardwareMap.dcMotor.get("rightFront");
        DcMotor backRight = hardwareMap.dcMotor.get("rightBack");
        DcMotorEx slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");
        Servo leftLinkage = hardwareMap.get(Servo.class, "leftLinkage");
        Servo rightLinkage = hardwareMap.get(Servo.class, "rightLinkage");
        Servo bottomWrist = hardwareMap.get(Servo.class, "bottomWrist");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        double speed = .8;

        double linkagePos = -1;

        leftLinkage.setPosition(0);
        rightLinkage.setPosition(1);
        bottomWrist.setPosition(0);

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

            //HALF-SPEED TOGGLE
            if (gamepad1.x && !previousGamepad1.x && currentGamepad1.x) {
                if (speed != .8)
                    speed = .8;
                else
                    speed = 0.4;
            }

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
        }
    }
}
