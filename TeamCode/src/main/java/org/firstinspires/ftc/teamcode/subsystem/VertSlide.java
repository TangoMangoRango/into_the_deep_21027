package org.firstinspires.ftc.teamcode.subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VertSlide {

    DcMotorEx slideMotor1;
    DcMotorEx slideMotor2;
    double p;
    double i;
    double d;
    int target;

    public VertSlide(HardwareMap hardwareMap) {
        slideMotor1 = hardwareMap.get(DcMotorEx.class, "slideLeft");
        slideMotor2 = hardwareMap.get(DcMotorEx.class, "slideRight");
        slideMotor1.setDirection(DcMotorSimple.Direction.REVERSE);

        p = 0.02;
        i = 0;
        d = 0.0001;
        target = 0;
    }

    public static class Extend implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            return false;
        }
    }
    public Action extend() {
        return new Extend();
    }

    public static class Retract implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            return false;
        }
    }
    public Action retract() {
        return new Retract();
    }

}
