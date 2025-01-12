package org.firstinspires.ftc.teamcode.subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HorizSlide {
    Servo leftLinkage;
    Servo rightLinkage;
    Servo leftIntake;
    Servo rightIntake;
    Servo intakeWrist;
    Servo intakeClaw;

    public HorizSlide(HardwareMap hardwareMap) {
        leftLinkage = hardwareMap.get(Servo.class, "leftLinkage");
        rightLinkage = hardwareMap.get(Servo.class, "rightLinkage");

        leftIntake = hardwareMap.get(Servo.class, "leftIntake");
        rightIntake = hardwareMap.get(Servo.class, "rightIntake");

        intakeWrist = hardwareMap.get(Servo.class, "intakeWrist");
        intakeClaw = hardwareMap.get(Servo.class, "intakeClaw");

        leftLinkage.setDirection(Servo.Direction.REVERSE);
        leftIntake.setDirection(Servo.Direction.REVERSE);
        intakeClaw.setDirection(Servo.Direction.REVERSE);

        intakeClaw.setPosition(0);
    }

    public class Extend implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftLinkage.setPosition(0.5);
            rightLinkage.setPosition(0.5);
            leftIntake.setPosition(0.83);
            rightIntake.setPosition(0.83);
            intakeWrist.setPosition(0.03);
            return false;
        }
    }
    public class Retract implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftLinkage.setPosition(0.06);
            rightLinkage.setPosition(0.06);
            leftIntake.setPosition(0);
            rightIntake.setPosition(0);
            intakeWrist.setPosition(0.35);
            return false;
        }
    }
    public class OpenClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intakeClaw.setPosition(0);
            return false;
        }
    }
    public class CloseClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intakeClaw.setPosition(.5);
            return false;
        }
    }


    public Action extendIntake() {
        return new HorizSlide.Extend();
    }
    public Action retractIntake() {
        return new HorizSlide.Retract();
    }
    public Action openClaw() { return new HorizSlide.OpenClaw();}
    public Action closeClaw() { return new HorizSlide.CloseClaw();}
}
