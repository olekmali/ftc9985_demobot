package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.hardware.Servo;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:       Left  drive motor:        "left_drive"
 * Motor channel:       Right drive motor:        "right_drive"
 * Motor channel:       Manipulator drive motor:  "left_arm"
 * N/A Servo channel:  Servo to open left claw:  "left_hand"
 * N/A Servo channel:  Servo to open right claw: "right_hand"
 */
public class UltimateSetupActuators
{
    /* Public actuator members. */
    public DcMotor FrontLeft = null;
    public DcMotor FrontRight = null;
    public DcMotor RearLeft = null;
    public DcMotor RearRight = null;

    public DcMotor CandyMotor    = null;
    public DcMotor BallLauncherMotor    = null;
    public Servo Servo_1  =   null;

    public static final double MID_SERVO =.53;

    public static final double CANDYMOTORSPEED          = (.8);
    /* local members. */
    HardwareMap hwMap           =  null;

    /* Constructor */
    public UltimateSetupActuators(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FrontLeft = hwMap.dcMotor.get("FrontLeft_Motor");
        FrontRight = hwMap.dcMotor.get("FrontRight_Motor");
        RearLeft = hwMap.dcMotor.get("RearLeft_Motor");
        RearRight = hwMap.dcMotor.get("RearRight_Motor");

        CandyMotor    = hwMap.dcMotor.get("Candy_Motor");
        BallLauncherMotor    = hwMap.dcMotor.get("BallLauncher_Motor");

        FrontLeft.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        FrontRight.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        RearLeft.setDirection(DcMotor.Direction.FORWARD);
        RearRight.setDirection(DcMotor.Direction.FORWARD);

        CandyMotor.setDirection(DcMotor.Direction.FORWARD);
        BallLauncherMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        RearLeft.setPower(0);
        RearRight.setPower(0);

        CandyMotor.setPower(0);
        BallLauncherMotor.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        CandyMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BallLauncherMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        Servo_1 = hwMap.servo.get("Servo1");
        Servo_1.setPosition(MID_SERVO);
    }

}
