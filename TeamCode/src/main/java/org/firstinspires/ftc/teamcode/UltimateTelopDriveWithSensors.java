/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// import com.qualcomm.robotcore.util.Range;


/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="demo_bot_TeleoperationWithSensors", group="Teleop")
public class UltimateTelopDriveWithSensors extends OpMode
{

    /* Declare OpMode members. */
    private UltimateSetupActuators robot            = new UltimateSetupActuators(); // Use Pushbot's actuators
    private UltimateSetupSensors sensors            = new UltimateSetupSensors();   // Use Pushbot's sensors
    // use the class above that was created to define a Pushbot's hardware
//  private double              clawOffset      = 0.0 ;                     // Servo mid position

    //  private staticfinal double  CLAW_SPEED      = 0.02 ;                    // sets rate to move servo
    private static final double ARM_UP_POWER    =  0.8 ;                   //
    private static final double ARM_DOWN_POWER  = -0.8 ;
    private static final double Deadzone = 0.1;

    //This allows us to track the candy and ball arms
    //private boolean BallArmPressed = false ;



    //Used to move the robot on the vertical axis
    private void DriveVertical (double power)
    {
        robot.FrontLeft.setPower(power);
        robot.FrontRight.setPower(-power);
        robot.RearLeft.setPower(power);
        robot.RearRight.setPower(-power);
    }

    //Used to move the robot on the horizontal axis
    private void DriveHorizontal (double power)
    {
        robot.FrontLeft.setPower(power);
        robot.FrontRight.setPower(power);
        robot.RearLeft.setPower(-power);
        robot.RearRight.setPower(-power);
    }

    //Used to move the robot on the forword right axis
    private void DriveDiagonalRight (double power)
    {
        robot.FrontLeft.setPower(power);
        robot.FrontRight.setPower(0);
        robot.RearLeft.setPower(0);
        robot.RearRight.setPower(-power);
    }

    //Used to move the robot on the forword left axis
    private void DriveDiagonalLeft (double power)
    {
        robot.FrontLeft.setPower(0);
        robot.FrontRight.setPower(-power);
        robot.RearLeft.setPower(power);
        robot.RearRight.setPower(0);
    }

    //Used to rotate the robot around the center of the robot
    private void DriveRotate (double power)
    {
        robot.FrontLeft.setPower(power);
        robot.FrontRight.setPower(power);
        robot.RearLeft.setPower(power);
        robot.RearRight.setPower(power);
    }

    //Used to stop the robot
    private void StopDrive ()
    {
        robot.FrontLeft.setPower(0);
        robot.FrontRight.setPower(0);
        robot.RearLeft.setPower(0);
        robot.RearRight.setPower(0);
    }


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        sensors.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Master please control me!");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    boolean launchInProgress    = false;
    boolean BallPassed          = false;

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        final double SERVO_BALL_IN = 1;
        final double SERVO_BALL_OUT = 0.5;
        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        double Vertical = gamepad1.left_stick_y;
        Vertical = Vertical * 0.25;
        double Horizontal = -gamepad1.left_stick_x;
        Horizontal = Horizontal * 0.25;
        float LeftRotate = gamepad1.left_trigger;
        LeftRotate = LeftRotate * 0.25f;
        float RightRotate = gamepad1.right_trigger;
        RightRotate = RightRotate * 0.25f;
        boolean PressedX = gamepad1.x;
        boolean pressed_B = gamepad2.b;
        boolean pressed_A = gamepad2.a;
        boolean CandyArmed = gamepad2.right_bumper;
        String Mode = "";


        if (sensors.lightSensor.getLightDetected() >= 0.1)
        {
            BallPassed = true;
        }

        if (BallPassed && CandyArmed)
        {
            BallPassed = false;
            robot.CandyMotor.setPower(1);
        }
        else if (sensors.touchSensorCandyArm.isPressed())
        {
            //WARNING: Expirmental Code: MAY NOT WORK
            robot.CandyMotor.setPower(0);
        }



        //Starts servo motor and reverses servo motor for ball scooper
        if (pressed_B)
        {
            robot.Servo_1.setPosition(SERVO_BALL_OUT);
        }
        else if (pressed_A)
        {
            robot.Servo_1.setPosition(SERVO_BALL_IN);
        }

        // precision mode drive
        if (gamepad1.left_bumper || gamepad1.right_bumper)
        {
            Vertical = 4 * Vertical;
            Horizontal = 4 * Horizontal;
            LeftRotate = 4 * LeftRotate;
            RightRotate = 4 * RightRotate;
            Mode = "Fast ";
        }

        //See if joystick is in dead zone
        if ( Math.abs(Vertical) < Deadzone && Math.abs(Horizontal) < Deadzone)
        {
            if (Math.abs(LeftRotate) > Deadzone || Math.abs(RightRotate) > Deadzone)
            {
                DriveRotate(LeftRotate - RightRotate);
                Mode += "Rotate";
            }
            else
            {
                StopDrive();
                Mode += "Stop";
            }
        }
        //Sees if X on the gamepad is pressed
        else if (PressedX)
        {
            //Sees what diagonal is used
            if (Math.abs(Vertical) > Math.abs(Horizontal))
            {
                //Makes the robot move on the Vertical Left axis
                DriveDiagonalLeft(Vertical);
                Mode += "Diagonal Left";
            }
            else
            {
                //Makes the robot move on the Vertical Right axis
                DriveDiagonalRight(Horizontal);
                Mode += "Diagonal Right";
            }
        }
        else
        {
            //Sees if Vertical or Horizontal movement
            if (Math.abs(Vertical) > Math.abs(Horizontal))
            {
                //Makes the robot move on the Vertical axis
                DriveVertical(Vertical);
                Mode += "Vertical";
            }
            else
            {
                //Makes the robot move on the Horizontal axis
                DriveHorizontal(Horizontal);
                Mode += "Horizontal";
            }
        }


        if (gamepad2.right_trigger > 0.5) {

            robot.CandyMotor.setPower(robot.CANDYMOTORSPEED);
        }
        else
        if (sensors.touchSensorCandyArm.isPressed()) {
            robot.CandyMotor.setPower(0);
        }

        // robot.rightMotor.setPower(right);
        if (gamepad2.left_trigger > 0.5) {

            robot.BallLauncherMotor.setPower(.6);
        }
        else
        if (sensors.touchSensorBallArm.isPressed()) {
            robot.BallLauncherMotor.setPower(0);
        }


        // Send telemetry message to signify robot running;
//        telemetry.addData("claw",  "Offset = %.2f", clawOffset);
        telemetry.addData("Light Level ", sensors.lightSensor.getLightDetected() );
        telemetry.addData("Vertical",       "%.2f",     Vertical);
        telemetry.addData("Horizontal",      "%.2f",     Horizontal);
        telemetry.addData("left trig",  "%.2f",     LeftRotate);
        telemetry.addData("right trig",  "%.2f",    RightRotate);
        // telemetry.addData("Is X pressed" , "%b" , PressedX);
        telemetry.addData("Mode is" , "%s" , Mode);
        telemetry.addData("ball touch sensor  is" , sensors.touchSensorBallArm.isPressed());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Say", "Don't leave me, please!");    //
    }

}
