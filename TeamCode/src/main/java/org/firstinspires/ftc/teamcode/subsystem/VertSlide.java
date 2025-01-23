package org.firstinspires.ftc.teamcode.subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import static org.firstinspires.ftc.teamcode.common.stuff.Globals.*;

public class VertSlide {

    static DcMotorEx slideMotor1;
    static DcMotorEx slideMotor2;
    static Servo outputClaw;
    static Servo outputWrist;
    static Servo leftOutput;
    static Servo rightOutput;
    static double p;
    static double i;
    static double d;
    static int target;
    static PIDController controller;


    public VertSlide(HardwareMap hardwareMap) {
        slideMotor1 = hardwareMap.get(DcMotorEx.class, "slideLeft");
        slideMotor2 = hardwareMap.get(DcMotorEx.class, "slideRight");
        slideMotor1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        slideMotor2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        slideMotor1.setDirection(DcMotorEx.Direction.REVERSE);
        outputClaw = hardwareMap.get(Servo.class, "outputClaw");
        outputWrist = hardwareMap.get(Servo.class, "outputWrist");
        leftOutput = hardwareMap.get(Servo.class, "leftOutput");
        rightOutput = hardwareMap.get(Servo.class, "rightOutput");
        rightOutput.setDirection(Servo.Direction.REVERSE);

        p = 0.02;
        i = 0;
        d = 0.0001;
        target = 0;

        controller = new PIDController(p, i, d);

        outputClaw.setPosition(CLAW_CLOSE);

        leftOutput.setPosition(OUTTAKE_ARM_POSITION_EXTEND);
        rightOutput.setPosition(OUTTAKE_ARM_POSITION_EXTEND);
        outputWrist.setPosition(OUTTAKE_WRIST_POSITION_EXTEND);
    }

    public class Extend implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftOutput.setPosition(OUTTAKE_ARM_POSITION_EXTEND);
            rightOutput.setPosition(OUTTAKE_ARM_POSITION_EXTEND);
            outputWrist.setPosition(OUTTAKE_WRIST_POSITION_EXTEND);
            return false;
        }
    }
    public class Retract implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftOutput.setPosition(OUTTAKE_ARM_POSITION_RETRACT);
            rightOutput.setPosition(OUTTAKE_ARM_POSITION_RETRACT);
            outputWrist.setPosition(OUTTAKE_WRIST_POSITION_RETRACT);
            return false;
        }
    }
    public class OpenClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            outputClaw.setPosition(CLAW_OPEN);
            return false;
        }
    }
    public class CloseClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            outputClaw.setPosition(CLAW_CLOSE);
            return false;
        }
    }

    public static class MoveSlides implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            controller.setPID(p, i, d);
            int slidePos = slideMotor1.getCurrentPosition();
            double pid = controller.calculate(slidePos, target);

            slideMotor1.setPower(pid * .8);
            slideMotor2.setPower(pid * .8);
            return false;
        }
    }
    public Action moveSlides(int wantPos) {
        target = wantPos;
        return new MoveSlides();
    }

    public Action extendOuttake() {
        return new VertSlide.Extend();
    }
    public Action retractOuttake() {return new VertSlide.Retract();}
    public Action openOuttakeClaw() { return new VertSlide.OpenClaw();}
    public Action closeOuttakeClaw() { return new VertSlide.CloseClaw();}


}
