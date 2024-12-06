package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(25, 25, Math.toRadians(180), Math.toRadians(180), 16)
                .setDriveTrainType(DriveTrainType.MECANUM)
                .setDimensions(16.875, 16.69)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(40, 64, Math.toRadians(270)))

                .splineToLinearHeading(new Pose2d(55, 56, Math.toRadians(225)), Math.toRadians(225))
                .splineToLinearHeading(new Pose2d(48.75,36, Math.toRadians(270)), Math.toRadians(270))
                //.waitSeconds(1)
                .splineToLinearHeading(new Pose2d(56, 56, Math.toRadians(225)), Math.toRadians(0))
                .build());

                /*
                .lineToX(30)
                .turn(Math.toRadians(90))
                .lineToY(30)
                .turn(Math.toRadians(90))
                .lineToX(0)
                .turn(Math.toRadians(90))
                .lineToY(0)
                .turn(Math.toRadians(90))
                .build());
                 */

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}