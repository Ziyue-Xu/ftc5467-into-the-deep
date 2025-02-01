package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mathemagics.Point;
import org.firstinspires.ftc.teamcode.navigation.*;
import org.firstinspires.ftc.teamcode.mechanics.*;
import org.firstinspires.ftc.teamcode.stuff.*;

import static org.firstinspires.ftc.teamcode.tuning.Constants.*;
import static org.firstinspires.ftc.teamcode.stuff.Robot.*;
import static org.firstinspires.ftc.teamcode.tuning.Recipes.*;
import static org.firstinspires.ftc.teamcode.mechanics.Drivetrain.*;
import static org.firstinspires.ftc.teamcode.navigation.Motion.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Config
@TeleOp(name = "Servo Test", group = "Linear OpMode")

public class servoTest extends LinearOpMode {

    public static Servo diffyLeft;
    public static Servo diffyRight;
    public static Servo finger;
    public static Servo linkage;
    public static Servo arm1;
    public static Servo arm2;

    public static double arm1pos = 0;
    public static double arm2pos = 0;
    public static double diffyleftpos = 0;
    public static double diffyrightpos = 0;
    public static double fingerpos = 0;
    public static double linkagePos = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        // setup everything

        diffyLeft = hardwareMap.servo.get("diffyL");
        diffyRight = hardwareMap.servo.get("diffyR");
        finger = hardwareMap.servo.get("finger");
        linkage = hardwareMap.servo.get("link");
        arm1 = hardwareMap.servo.get("armL");
        arm2 = hardwareMap.servo.get("armR");


        Robot.setup(
                hardwareMap.get(DcMotorEx.class, "front_left_motor"),
                hardwareMap.get(DcMotorEx.class, "back_right_motor"),
                hardwareMap.get(DcMotorEx.class, "front_right_motor"),
                hardwareMap.get(DcMotorEx.class, "back_left_motor"),
                hardwareMap.get(DcMotorEx.class, "slide1"),
                hardwareMap.get(DcMotorEx.class, "slide2"),
                hardwareMap.get(IMU.class, "imu"),
                new ArrayList<>(hardwareMap.getAll(LynxModule.class)),
                true
        );

        // waiting...
        waitForStart();
        runtime.reset();
        reset(hardwareMap);

        arm1.setPosition(arm1pos);
        arm2.setPosition(arm2pos);
        diffyLeft.setPosition(diffyleftpos);
        diffyRight.setPosition(diffyrightpos);
        finger.setPosition(fingerpos);
        linkage.setPosition(linkagePos);
        
    }
    public void debug () {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        dashboardTelemetry.addData("arm 1 pos: ", arm1pos);
        dashboardTelemetry.addData("arm 2 pos: ", arm2pos);
        dashboardTelemetry.addData("diffy left pos: ", diffyleftpos);
        dashboardTelemetry.addData("diffy right pos: ", diffyrightpos);
        dashboardTelemetry.addData("finger pos: ", fingerpos);

        dashboardTelemetry.update();

    }
}

