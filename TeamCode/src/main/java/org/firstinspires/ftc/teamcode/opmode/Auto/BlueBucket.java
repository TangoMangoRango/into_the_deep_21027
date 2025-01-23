package org.firstinspires.ftc.teamcode.opmode.Auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.rrStuff.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.stuff.Globals;
import org.firstinspires.ftc.teamcode.common.stuff.Location;
import org.firstinspires.ftc.teamcode.subsystem.*;

@Autonomous(name = "BlueBucket", preselectTeleOp = "NEWIntoTheDeepTele")
public class BlueBucket extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Globals.ALLIANCE = Location.BLUE;
        Pose2d initialPose = new Pose2d(40, 64, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        HorizSlide hori = new HorizSlide(hardwareMap);
        VertSlide vert = new VertSlide(hardwareMap);


        TrajectoryActionBuilder s1 = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(56,56),Math.toRadians(225));
        TrajectoryActionBuilder s2 = s1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(48.75,36),Math.toRadians(270));



        Action step1 = s1.build();
        Action step2 = s2.build();

        if(opModeIsActive()) {
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    step1,
                                    vert.moveSlides(3000)
                            ),
                            new ParallelAction(
                                    step2,
                                    vert.moveSlides(25)
                            )
                    )
            );
        }
    }

}

