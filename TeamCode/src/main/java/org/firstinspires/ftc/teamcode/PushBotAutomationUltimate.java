package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.concurrent.TimeoutException;

/**
 * Created on 11/25/2016.
 */

abstract public class PushBotAutomationUltimate extends LinearOpMode {


    /* Declare OpMode constants for users */
    public static final double SPEED_FULL = 0.6;
    public static final double SPEED_DRIVE = 0.40;
    public static final double SPEED_APPROACH = 0.25;

    public static final double SPEED_TURN = 0.4;
    public static final double ANGLE_90 = 90.0;
    public static final double TURN_LEFT = -ANGLE_90;
    public static final double TURN_RIGHT = ANGLE_90;
    public static final double WHITE_THRESHOLD = 0.6;  // background spans between 0.1 - 0.5 from dark to light

    public static final double SPEED_ARM = 0.25;
    public static final double TOUT_ARM = 5;

    public static final double TOUT_SHORT = 3;
    public static final double TOUT_MEDIUM = 5;
    public static final double TOUT_LONG = 10;

    /* Declare OpMode data members */
    UltimateSetupActuators robot = new UltimateSetupActuators();  // Use Pushbot's actuators
    UltimateSetupSensors sensors = new UltimateSetupSensors();    // Use Pushbot's sensors
    /* Timeout variable */
    private ElapsedTime runtime = new ElapsedTime();

    public void setupHardware() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        telemetry.addData("Status", "Init Automation");
        telemetry.addData("Status", "Init Mat Actuators");
        telemetry.update();
        robot.init(hardwareMap);
        telemetry.addData("Status", "Init Automation");
        telemetry.addData("Status", "Init Mat Sensors");
        sensors.init(hardwareMap);
        telemetry.addData("Status", "Init Automation Done");
        telemetry.update();
    }

    public void calibrateNoGyro() {
        telemetry.addData("Status", "calibrateNoGyro");
        telemetry.update();
        sensors.gyroSensor = null;
    }

    // Display the sensor levels while we are waiting to start
    public void waitForStartAndDisplayWhileWaiting() {
        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("Light Level  ", sensors.lightSensor.getLightDetected());
            telemetry.addData("Color:       ",
                    "RED " + sensors.colorSensor.red() +
                            "  GRN " + sensors.colorSensor.green() +
                            "  BLU " + sensors.colorSensor.blue()
            );
            telemetry.addData("Bumper Front ", sensors.touchSensorFront.isPressed());
            telemetry.addData("Bumper Arm   ", sensors.touchSensorArmPush.isPressed());
            telemetry.addData("Limit Switch ",
                    "IN " + sensors.touchSensorArmIn.isPressed() +
                            " OUT " + sensors.touchSensorArmOut.isPressed()
            );
            if (sensors.gyroSensor != null) {
                telemetry.addData("Gyro Z      ", sensors.gyroSensor.getIntegratedZValue());
            }
            telemetry.update();
            idle();
        }
    }

    public void fullStop()
    {
        telemetry.addData("Status", "fullStop");
        telemetry.update();
        robot.FrontLeft.setPower(0);
        robot.FrontRight.setPower(0);
        robot.RearLeft.setPower(0);
        robot.RearRight.setPower(0);
    }


    public void encoderReset()
    {
        telemetry.addData("Status", "encoderReset");
        telemetry.update();
        robot.FrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.FrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    /* Declare OpMode members. */
    private static final double HEADING_THRESHOLD = 1;       // As tight as we can make it with an integer gyro
    private static final double P_TURN_COEFF = 0.1;      // Larger is more responsive, but also less stable
    private static final double P_DRIVE_COEFF = 0.05;     // Larger is more responsive, but also less stable
}
