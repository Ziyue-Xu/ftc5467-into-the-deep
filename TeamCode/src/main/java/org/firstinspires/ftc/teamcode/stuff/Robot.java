package org.firstinspires.ftc.teamcode.stuff;

import static org.firstinspires.ftc.teamcode.navigation.Motion.*;

import org.firstinspires.ftc.teamcode.navigation.Localization;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.control.*;
import org.firstinspires.ftc.teamcode.mechanics.*;

import java.util.*;

public class Robot {

    // (Up + Right) force vector motors
    public static DcMotorEx front_left_motor = null;
    public static DcMotorEx back_right_motor = null;

    // (Up + Left) force vector motors
    public static DcMotorEx front_right_motor = null;
    public static DcMotorEx back_left_motor = null;

    // Intake/Outtake motors
    public static DcMotorEx slide1 = null;
    public static DcMotorEx slide2 = null;


    // our lovely imu
    public static IMU imu = null;
    public static IMU.Parameters parameters = null;

    // arraylist of the hubs
    public static ArrayList<LynxModule> hubs = null;

    // robot quantities
    public static double X;
    public static double Y;
    public static double theta;

    public static double initial_theta;

    // high-precision time (in seconds with a lotta sig figs)
    public static double time_previous = 0;
    public static double time = 0;

    // PID controllers
    public static PID rotational_pid;

    // control time, control everything
    public static ElapsedTime runtime;

    public static ServoErmSigma servos = null;


    public static void setup(DcMotorEx front_left,
                             DcMotorEx back_right,
                             DcMotorEx front_right,
                             DcMotorEx back_left,
                             DcMotorEx linear_slide_left,
                             DcMotorEx linear_slide_right,
                             IMU imu_,
                             ArrayList<LynxModule> hubs_,
                             boolean field_centric
    ) {

        front_left_motor = front_left;
        back_right_motor = back_right;

        front_right_motor = front_right;
        back_left_motor = back_left;
//
        slide1 = linear_slide_left;
        slide2 = linear_slide_right;

		/* Note:

		Positive is clockwise for right motors
		Negative is clockwise for left motors

		*/

        // We shall reverse the direction of the left-side motors so positive is clockwise
        front_left_motor.setDirection(DcMotorEx.Direction.REVERSE);
        back_left_motor.setDirection(DcMotorEx.Direction.REVERSE);


        slide1.setDirection(DcMotorEx.Direction.REVERSE);
        slide2.setDirection(DcMotorEx.Direction.REVERSE);
        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = imu_;

        parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP));

        imu.initialize(parameters);

        hubs = hubs_;

        // set caching mode to manual for every hub
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        // field centric?
        Drivetrain.FIELD_CENTRIC = field_centric;

        runtime = new ElapsedTime();
    }

    public static void begin() {
        // one bulk read only, as caches are manually cleared
        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }

        Robot.time = Robot.runtime.seconds();
    }

    public static void update() {
        Localization.localize(
                front_left_motor.getCurrentPosition(),
                front_right_motor.getCurrentPosition()
        );

        // [0, PI]
       // Arm.POSITION = -arm_motor.getCurrentPosition() / CPR * 2 * Math.PI / 5.6 - 0.025;

    }

    public static void end() {
        update();

        Drivetrain.y_power = 0;
        Drivetrain.x_power = 0;
        Drivetrain.rotational_power = 0;

        time_previous = time;
    }

    public static void settings(boolean left_stick_button, boolean right_stick_button) {
        if (right_stick_button) {
            theta = 0;
        }
    }

    public static void reset(HardwareMap hardware_map) {
        imu.resetYaw();

        front_left_motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        front_left_motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        front_left_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        back_right_motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        back_right_motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        back_right_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        front_right_motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        front_right_motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        front_right_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        back_left_motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        back_left_motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        back_left_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        slide1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slide1.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        slide2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//
        servos = new ServoErmSigma(hardware_map);

        // START OF FTC SKILL ISSUE (static variables DO NOT CHANGE unless robot is rebooted)

        time_previous = 0;
        time = 0;

        Localization.ABSOLUTE_X_VELOCITY = 0;
        Localization.ABSOLUTE_Y_VELOCITY = 0;

        Localization.RELATIVE_X_VELOCITY = 0;
        Localization.RELATIVE_Y_VELOCITY = 0;

        Localization.FL = 0;
        Localization.BR = 0;

        Localization.FR = 0;
        Localization.BL = 0;

        Drivetrain.x_power = 0;
        Drivetrain.y_power = 0;
        Drivetrain.rotational_power = 0;



        Localization.X_POD = 0;
        Localization.Y_POD = 0;


        path = null;

        // END OF FTC SKILL ISSUE

        // pid controllers
        rotational_pid = new PID(1, 0, 0.1, 0.05);
    }

    public static void teleop_reset(HardwareMap hardwareMap) {

    }
}
