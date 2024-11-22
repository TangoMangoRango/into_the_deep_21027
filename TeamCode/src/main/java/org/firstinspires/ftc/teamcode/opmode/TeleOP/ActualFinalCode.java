package org.firstinspires.ftc.teamcode.opmode.TeleOP;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp
public class ActualFinalCode extends LinearOpMode {


  private Servo planeServo;
  private Servo leftClawServo;

  private Servo rightClawServo;
  private CRServo liftServo;
  private DcMotor leftFront;
  private DcMotor rightFront;
  private DcMotor leftBack;
  private DcMotor rightBack;
  private DcMotor armMotor;

  @Override

  public void runOpMode() {
    planeServo = hardwareMap.get(Servo.class, "planeServo");
    leftClawServo = hardwareMap.get(Servo.class, "leftClawServo");
    rightClawServo = hardwareMap.get(Servo.class, "rightClawServo");
    liftServo = hardwareMap.get(CRServo.class,"liftServo");
    rightFront = hardwareMap.dcMotor.get("rightFront");
    leftBack = hardwareMap.dcMotor.get("leftBack");
    leftFront = hardwareMap.dcMotor.get("leftFront");
    rightBack = hardwareMap.dcMotor.get("rightBack");
    armMotor = hardwareMap.dcMotor.get("armMotor");
    //RIGHT MOTORS MUST BE ROTATED BECAUSE THE GEARS CHANGE THEIR ROTATION
    //ALSO I DID THE MATH WRONG DO THIS ACCOUNTS FOR THAT
    rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
    rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
    //BRAKES THE ROBOT WHEN THERE IS NO POWER PROVIDED (NO SLIPPY-SLIDING)
    leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    //LETS THE "ROTATE()" METHOD DO THE REST
    rotate();
    //UPDATES ROBOT'S POSITION (BUT WE DON'T REALLY USE IT)
    telemetry.update();
  }

  private void rotate() {
    waitForStart();

    //SETS SERVO TO LOADED POSITION
    planeServo.setPosition(0.47);
    //SCALES THE RANGE SO IT ONLY TURNS ~90 DEGREES
    planeServo.scaleRange(0.47, 1);
    //OPENS CLAW
    leftClawServo.setPosition(0.25);
    rightClawServo.setPosition(0.75);

    //THESE THINGS ARE SO THE TOGGLE WORKS
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();

    Gamepad previousGamepad1 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();

    //STARTS AT FULL SPEED
    double speed = 1;
    //STARTS WITH PUSHY THING FORWARD
    double direction = 1;

    if (opModeIsActive()) {
      while (opModeIsActive()) {
        //LETS YOU SEE ROBOT'S POSITION IN THE DASHBOARD
        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket packet1 = new TelemetryPacket();
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        /* STORES THE GAMEPAD INPUT FROM THE PREVIOUS LOOP
        STOPS GAMEPAD VALUES FROM SWITCHING BETWEEN BEING USED AND
        STORED IN PREVIOUSGAMEPAD1/2 */
        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);
        //FORWARD AND BACKWARD (NEGATIVE BC Y IS REVERSED)(DON'T ASK WHY)
        double y = -gamepad1.left_stick_y*direction;
        //SETS RIGHT STICK ROTATION (MULTIPLIED TO GET THE FULL RANGE OF ROTATION)
        double rx = gamepad1.right_stick_x * 1.1;
        //SIDE-TO-SIDE
        double x = gamepad1.left_stick_x*direction;

        //GETS DENOMINATOR THAT WILL DIVIDE BY TOTAL POWER (SO ROBOT DOESN'T GO SUPER-FAST)
        double denominator = Math.max(Math.abs(y)+Math.abs(x)+Math.abs(rx),1);
        //HALF-SPEED TOGGLE
        if (gamepad1.x && !previousGamepad1.x && currentGamepad1.x){
          if (speed != 1)
            speed = 1;
          else
            speed = 0.5;
        }
        //SWITCHED TO FORWARD MODE IF "A" IS PRESSED ON GAMEPAD 1
        if (gamepad1.a){
          direction = 1;
        }
        //SWITCHED TO BACKWARD MODE IF "B" IS PRESSED ON GAMEPAD 1
        if (gamepad1.b){
          direction = -1;
        }
        //SETS MOVEMENT OF ROBOT (FORWARD +- SIDE TO SIDE +- ROTATION)/TOTAL POWER
        leftFront.setPower(((y + x + rx) / denominator)*speed);
        leftBack.setPower(((y - x + rx) / denominator)*speed);
        rightFront.setPower(((y - x - rx) / denominator)*speed);
        rightBack.setPower(((y + x - rx) / denominator)*speed);
        //SET VARIABLE FOR TRIGGERS. THEY RETURN ONLY POSITIVE VALUES FROM 0-1 DEPENDING ON HOW
        //MUCH THEY'RE PRESSED
        double leftTrigger = gamepad2.left_trigger;
        double rightTrigger = gamepad2.right_trigger;
        //RIGHT GOES UP, LEFT GOES DOWN. DIVIDE BY 2 FOR HALF SPEED.
        double powerTotal = (rightTrigger - leftTrigger)/2;
        //FEEDS POWER TO ARM
        armMotor.setPower(powerTotal);
        //SETS VARIABLES TO CURRENT CLAW POSITIONS
        double lclaw = leftClawServo.getPosition();
        double rclaw = rightClawServo.getPosition();
        //TOGGLE LEFT CLAW ON "A"
        //CURRENT/PREVIOUS MAKES SURE THAT THIS LOOP DOESN'T RUN EVERY LIKE 1/10000000 SECONDS
        if (gamepad2.a && currentGamepad2.a && !previousGamepad2.a) {
          if (lclaw != 0)
            leftClawServo.setPosition(0);
          else
            leftClawServo.setPosition(0.25);
        }
        //TOGGLE RIGHT CLAW ON "B"
        if(gamepad2.b && currentGamepad2.b && !previousGamepad2.b) {
          if (rclaw != 1)
            rightClawServo.setPosition(1);
          else {
            rightClawServo.setPosition(0.75);
          }
       }
        //TOGGLE BOTH CLAWS
        if(gamepad2.x && currentGamepad2.x && !previousGamepad2.x){
          if (rclaw !=1){
            rightClawServo.setPosition(1);
            leftClawServo.setPosition(0);
          }
          else {
            rightClawServo.setPosition(0.75);
            leftClawServo.setPosition(0.25);
          }
        }



        //GET POSITION OF PLANE SERVO
        double plane = planeServo.getPosition();
        //LAUNCH PLANE ON RIGHT BUMPER
        if (gamepad1.right_bumper) {
          planeServo.setPosition(plane + 0.1);
        }
        //RESET SERVO ON LEFT BUMPER
        else if (gamepad1.left_bumper) {
          planeServo.setPosition(plane - 0.1);
        }


        telemetry.addData("Arm currently at",  " at %7d",
                armMotor.getCurrentPosition());
        telemetry.addData("Left Claw currently at", leftClawServo.getPosition());
        packet1.fieldOverlay();
        telemetry.update();
        dashboard.sendTelemetryPacket(packet1);
      }
    }
  }
}
