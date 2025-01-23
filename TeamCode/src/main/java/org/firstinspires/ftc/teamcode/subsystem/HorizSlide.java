package org.firstinspires.ftc.teamcode.subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import static org.firstinspires.ftc.teamcode.common.stuff.Globals.*;

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

        leftLinkage.setPosition(LINKAGE_POSITION_INIT);
        rightLinkage.setPosition(LINKAGE_POSITION_INIT);

        intakeClaw.setPosition(CLAW_OPEN);

        intakeWrist.setPosition(INTAKE_WRIST_POSITION_RETRACT);
        leftIntake.setPosition(INTAKE_ARM_POSITION_RETRACT);
        rightIntake.setPosition(INTAKE_ARM_POSITION_RETRACT);
    }

    public class Extend implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftLinkage.setPosition(LINKAGE_POSITION_EXTEND);
            rightLinkage.setPosition(LINKAGE_POSITION_EXTEND);
            leftIntake.setPosition(INTAKE_ARM_POSITION_EXTEND);
            rightIntake.setPosition(INTAKE_ARM_POSITION_EXTEND);
            intakeWrist.setPosition(INTAKE_WRIST_POSITION_EXTEND);
            return false;
        }
    }
    public class Retract implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftLinkage.setPosition(LINKAGE_POSITION_RETRACT);
            rightLinkage.setPosition(LINKAGE_POSITION_RETRACT);
            leftIntake.setPosition(INTAKE_ARM_POSITION_RETRACT);
            rightIntake.setPosition(INTAKE_ARM_POSITION_RETRACT);
            intakeWrist.setPosition(INTAKE_WRIST_POSITION_RETRACT);
            return false;
        }
    }
    public class OpenClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intakeClaw.setPosition(CLAW_OPEN);
            return false;
        }
    }
    public class CloseClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intakeClaw.setPosition(CLAW_CLOSE);
            return false;
        }
    }


    public Action extendIntake() {
        return new HorizSlide.Extend();
    }
    public Action retractIntake() {
        return new HorizSlide.Retract();
    }
    public Action openIntakeClaw() { return new HorizSlide.OpenClaw();}
    public Action closeIntakeClaw() { return new HorizSlide.CloseClaw();}
}
