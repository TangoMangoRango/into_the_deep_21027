package org.firstinspires.ftc.teamcode.common.stuff.PIDTuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@Config
@TeleOp
public class PIDTesting extends LinearOpMode {
    public static double p = 0.02;
    public static double i = 0;
    public static double d = 0.0001;
    public static int target = 0;

    public void runOpMode() {
        DcMotor frontLeft = hardwareMap.dcMotor.get("leftFront");
        DcMotor backLeft = hardwareMap.dcMotor.get("leftBack");
        DcMotor frontRight = hardwareMap.dcMotor.get("rightFront");
        DcMotor backRight = hardwareMap.dcMotor.get("rightBack");
        Servo leftLinkage = hardwareMap.get(Servo.class, "leftLinkage");
        Servo rightLinkage = hardwareMap.get(Servo.class, "rightLinkage");

        //Currently the one I care about
        DcMotorEx slideMotor1 = hardwareMap.get(DcMotorEx.class, "slideMotor1");
        DcMotorEx slideMotor2 = hardwareMap.get(DcMotorEx.class, "slideMotor2");
        slideMotor1.setDirection(DcMotorSimple.Direction.REVERSE);

        //Slides look good at 0.02, 0, 0.0001

        PIDController controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        while(opModeIsActive()) {
            controller.setPID(p, i, d);
            int slidePos = slideMotor1.getCurrentPosition();
            double pid = controller.calculate(slidePos, target);
            
            slideMotor1.setPower(pid * .8);
            slideMotor2.setPower(pid * .8);

            telemetry.addData("CurrPos: ", slidePos);
            telemetry.addData("Target: ", target);
            telemetry.update();
        }
    }
}
