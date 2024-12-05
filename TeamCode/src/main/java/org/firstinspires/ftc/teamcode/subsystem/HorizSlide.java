package org.firstinspires.ftc.teamcode.subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HorizSlide {
    Servo leftLink;
    Servo rightLink;

    public HorizSlide(HardwareMap hardwareMap) {
        leftLink = hardwareMap.get(Servo.class, "leftLink");
        rightLink = hardwareMap.get(Servo.class, "rightLink");
    }

    public class Extend implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftLink.setPosition(1);
            rightLink.setPosition(0);
            return false;
        }
    }
    public Action extend() {
        return new HorizSlide.Extend();
    }

    public class Retract implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftLink.setPosition(1);
            rightLink.setPosition(0);
            return false;
        }
    }
    public Action retract() {
        return new HorizSlide.Retract();
    }
}
