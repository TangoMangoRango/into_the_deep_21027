package org.firstinspires.ftc.teamcode.subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VertSlide {
    DcMotorEx slide;

    public VertSlide(HardwareMap hardwareMap) {
        slide = hardwareMap.get(DcMotorEx.class, "slideMotor");
        slide.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        slide.setDirection(DcMotorEx.Direction.FORWARD);
    }

    public class Extend implements Action {
        private boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if(!initialized) {
                slide.setPower(.8);
                initialized = true;
            }

            double pos = slide.getCurrentPosition();
            packet.put("slidePos", pos);

            if(pos < 3000) {
                return true;
            } else {
                slide.setPower(0);
                return false;
            }
        }
    }
    public Action extend() {
        return new Extend();
    }

    public class Retract implements Action {
        private boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if(!initialized) {
                slide.setPower(-.8);
                initialized = true;
            }

            double pos = slide.getCurrentPosition();
            packet.put("slidePos", pos);

            if(pos > 150) {
                return true;
            } else {
                slide.setPower(0);
                return false;
            }
        }
    }
    public Action retract() {
        return new Retract();
    }

}
