package org.firstinspires.ftc.teamcode.opmode.Auto;


import static org.firstinspires.ftc.teamcode.common.stuff.Globals.ALLIANCE;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.common.rrStuff.MecanumDrive;

public class Drive {
    private Pose2d beginPose;

    private MecanumDrive drive;



    public Drive(HardwareMap hardwareMap, int posX, int posY, Double posHeading){

        beginPose = new Pose2d(posX, posY, posHeading);
        drive = new MecanumDrive(hardwareMap, beginPose);
    }

    public Action toPark(){
        return new Action(){
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                switch (ALLIANCE) {
                    case RED:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .strafeTo(new Vector2d(60,-63))
                                        .build());
                        break;
                    case BLUE:
                        Actions.runBlocking(
                                drive.actionBuilder(drive.pose)
                                        .strafeTo(new Vector2d(-60,63))
                                        .build());
                        break;
                }
                return false;
            }
        };
    }

}
