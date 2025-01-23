package org.firstinspires.ftc.teamcode.opmode.TeleOP;

import static org.firstinspires.ftc.teamcode.common.stuff.Globals.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp
public class NEWIntoTheDeepTele extends LinearOpMode {



    private ElapsedTime timer = new ElapsedTime();



    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = hardwareMap.dcMotor.get("leftFront");
        DcMotor backLeft = hardwareMap.dcMotor.get("leftBack");
        DcMotor frontRight = hardwareMap.dcMotor.get("rightFront");
        DcMotor backRight = hardwareMap.dcMotor.get("rightBack");

        DcMotorEx slideLeft = hardwareMap.get(DcMotorEx.class, "slideLeft");
        DcMotorEx slideRight = hardwareMap.get(DcMotorEx.class, "slideRight");

        Servo leftLinkage = hardwareMap.get(Servo.class, "leftLinkage");
        Servo rightLinkage = hardwareMap.get(Servo.class, "rightLinkage");

        Servo leftIntake = hardwareMap.get(Servo.class, "leftIntake");
        Servo rightIntake = hardwareMap.get(Servo.class, "rightIntake");

        Servo intakeClaw = hardwareMap.get(Servo.class, "intakeClaw");
        Servo outputClaw = hardwareMap.get(Servo.class, "outputClaw");

        Servo intakeWrist = hardwareMap.get(Servo.class, "intakeWrist");
        Servo outputWrist = hardwareMap.get(Servo.class, "outputWrist");

        Servo leftOutput = hardwareMap.get(Servo.class, "leftOutput");
        Servo rightOutput = hardwareMap.get(Servo.class, "rightOutput");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        slideLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        leftLinkage.setDirection(Servo.Direction.REVERSE);
        leftIntake.setDirection(Servo.Direction.REVERSE);
        rightOutput.setDirection(Servo.Direction.REVERSE);
        intakeClaw.setDirection(Servo.Direction.REVERSE);

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //STARTS A TIMER SO WE CAN ADD DELAYS LATER
        ElapsedTime runtime = new ElapsedTime();

        //LETS THE "ROTATE()" METHOD DO THE REST
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        //STARTS AT 1 SPEED
        double speed = 1;

        boolean linkRetract = true;
        int armCounter = 0;
        boolean inClawClosed = false;
        boolean outClawClosed = false;
        boolean thrownBack = true;
        int direction = 1;

        leftLinkage.setPosition(LINKAGE_POSITION_INIT);
        rightLinkage.setPosition(LINKAGE_POSITION_INIT);

        intakeClaw.setPosition(CLAW_OPEN);
        outputClaw.setPosition(CLAW_CLOSE);

        intakeWrist.setPosition(INTAKE_WRIST_POSITION_RETRACT);
        leftIntake.setPosition(INTAKE_ARM_POSITION_RETRACT);
        rightIntake.setPosition(INTAKE_ARM_POSITION_RETRACT);
        leftOutput.setPosition(OUTTAKE_ARM_POSITION_EXTEND);
        rightOutput.setPosition(OUTTAKE_ARM_POSITION_EXTEND);
        outputWrist.setPosition(OUTTAKE_WRIST_POSITION_EXTEND);

        waitForStart();

        runtime.reset();

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


            //SETS MOVEMENT OF ROBOT (FORWARD +- SIDE TO SIDE +- ROTATION)/TOTAL POWER
            frontLeft.setPower(((y + x + rx) / denominator) * speed * direction);
            backLeft.setPower(((y - x + rx) / denominator) * speed * direction);
            frontRight.setPower(((y - x - rx) / denominator) * speed * direction);
            backRight.setPower(((y + x - rx) / denominator) * speed * direction);

            //SET VARIABLE FOR TRIGGERS. THEY RETURN ONLY POSITIVE VALUES FROM 0-1 DEPENDING ON HOW
            //MUCH THEY'RE PRESSED
            double leftTrigger = gamepad2.left_trigger;
            double rightTrigger = gamepad2.right_trigger;

            //RIGHT GOES UP, LEFT GOES DOWN.
            double slidePower = (rightTrigger - leftTrigger);
            slideLeft.setPower(slidePower * .8);
            slideRight.setPower(slidePower * .8);

            //SPEED TOGGLE
            if (gamepad1.x && currentGamepad1.x && !previousGamepad1.x) {
                if (speed == 1) {
                    speed = 0.6;
                } else {
                    speed = 1;
                }
            }

            //DIRECTION TOGGLE
            if (gamepad1.a && currentGamepad1.a && !previousGamepad1.a){
                direction = -direction;
            }

            //TOGGLE OUTPUT CLAW - 0 IS OPEN, O.5 IS CLOSED
            if (gamepad1.b && currentGamepad1.b && !previousGamepad1.b){
                if (outClawClosed) {
                    outputClaw.setPosition(CLAW_OPEN);
                } else {
                    outputClaw.setPosition(CLAW_CLOSE);
                }
                outClawClosed = !outClawClosed;
            }

            // THROWS IT BACK! OUTPUT CLAW ROTATES IN AND OUT
            if (gamepad1.y && currentGamepad1.y && !previousGamepad1.y){
                if (thrownBack){
                    leftOutput.setPosition(OUTTAKE_ARM_POSITION_RETRACT);
                    rightOutput.setPosition(OUTTAKE_ARM_POSITION_RETRACT);
                    outputWrist.setPosition(OUTTAKE_WRIST_POSITION_RETRACT);
                }
                else{
                    leftOutput.setPosition(OUTTAKE_ARM_POSITION_EXTEND);
                    rightOutput.setPosition(OUTTAKE_ARM_POSITION_EXTEND);
                    outputWrist.setPosition(OUTTAKE_WRIST_POSITION_EXTEND);
                }
                thrownBack = !thrownBack;
            }

            //EXTENDS AND RETRACTS LINKAGES
            if (gamepad2.y && currentGamepad2.y && !previousGamepad2.y){
                if (linkRetract) {
                    //extendTime = runtime.time();
                    leftLinkage.setPosition(LINKAGE_POSITION_EXTEND);
                    rightLinkage.setPosition(LINKAGE_POSITION_EXTEND);
                    leftIntake.setPosition(INTAKE_ARM_POSITION_EXTEND);
                    rightIntake.setPosition(INTAKE_ARM_POSITION_EXTEND);
                    intakeWrist.setPosition(INTAKE_WRIST_POSITION_EXTEND);
                } else {
                    //retractTime = runtime.time();
                    leftLinkage.setPosition(LINKAGE_POSITION_RETRACT);
                    rightLinkage.setPosition(LINKAGE_POSITION_RETRACT);
                    leftIntake.setPosition(INTAKE_ARM_POSITION_RETRACT);
                    rightIntake.setPosition(INTAKE_ARM_POSITION_RETRACT);
                    intakeWrist.setPosition(INTAKE_WRIST_POSITION_RETRACT);
                }
                linkRetract = !linkRetract;
            }



            //TOGGLES INTAKE CLAW
            if (gamepad2.b && currentGamepad2.b && !previousGamepad2.b){
                if (inClawClosed) {
                    intakeClaw.setPosition(CLAW_OPEN);
                } else {
                    intakeClaw.setPosition(CLAW_CLOSE);
                }
                inClawClosed = !inClawClosed;
            }




            telemetry.addData("Intake Claw Closed: ", inClawClosed);
            telemetry.addData("Output Claw Closed: ", outClawClosed);
            telemetry.update();
        }

    }


}