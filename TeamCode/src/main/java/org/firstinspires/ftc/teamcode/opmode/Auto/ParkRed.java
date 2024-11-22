package org.firstinspires.ftc.teamcode.opmode.Auto;

import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.stuff.Globals;
import org.firstinspires.ftc.teamcode.common.stuff.Location;

@Autonomous(name = "ParkRed", preselectTeleOp = "IntoTheDeepTele")
public class ParkRed extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Globals.ALLIANCE = Location.RED;
        Drive drive = new Drive(hardwareMap, 36, 61, Math.toRadians(90));

        if(opModeIsActive()) {
            Actions.runBlocking(
                    //"SEQUENTIAL ACTION" RUNS ALL COMMANDS ONE AT A TIME
                    new SequentialAction(
                            //CALLS "TAPEPIXEL" METHOD IN "DRIVE.JAVA" CLASS
                            //FEEDS IT TWO VARIABLES: THE TARGET TAPE POSITION AND HOW MUCH TO ROTATE
                            drive.toPark()
                    )
            );
        }
    }

}

