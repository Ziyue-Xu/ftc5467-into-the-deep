package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.mechanics.*;
import org.firstinspires.ftc.teamcode.stuff.*;
import org.firstinspires.ftc.teamcode.mathemagics.*;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import static org.firstinspires.ftc.teamcode.tuning.Constants.*;
import static org.firstinspires.ftc.teamcode.stuff.Robot.*;
import static org.firstinspires.ftc.teamcode.tuning.Recipes.*;
import static org.firstinspires.ftc.teamcode.mechanics.Drivetrain.*;
import static org.firstinspires.ftc.teamcode.navigation.Motion.*;

import com.acmerobotics.dashboard.config.Config;

import java.util.*;

@Config
@Autonomous(name = "🥵 lqr tune", group = "meh")

public class lqrTune extends LinearOpMode {

    public boolean cooked = false;
    public boolean cooking = false;

    public double begin_wait = -1;
    public double wait = 0.5;



    @Override
    public void runOpMode() throws InterruptedException {
        // setup
        setup(
                hardwareMap.get(DcMotorEx.class, "front_left_motor"),
                hardwareMap.get(DcMotorEx.class, "back_right_motor"),
                hardwareMap.get(DcMotorEx.class, "front_right_motor"),
                hardwareMap.get(DcMotorEx.class, "back_left_motor"),
                hardwareMap.get(DcMotorEx.class, "slide1"),
                hardwareMap.get(DcMotorEx.class, "slide2"),
                hardwareMap.get(IMU.class, "imu"),
                new ArrayList<>(hardwareMap.getAll(LynxModule.class)),
                false
        );

        // initial position
        Robot.X = -3 * TILE_LENGTH + LENGTH/2;
        Robot.Y = -0.5 * TILE_LENGTH;
        desired_position = new Point(X, Y);

        // initial theta
        //0 is facing right then goes clockwise
        initial_theta = 0;
        theta = 0;
        desired_theta = 0;

        // COOK 🗣‼️
        Queue<Ingredient[]> POT = new LinkedList<>();


        waitForStart();
        runtime.reset();


        reset(hardwareMap);

        // yellow/purple pixel (2+)

        // cycle (+4) first stack
//        for (int i = 0; i < 2; i = i + 1) {
//            locations.add(new Point(-0.4 * TILE_LENGTH, -1.5 * TILE_LENGTH));
//            thetas.add(3 * Math.PI / 2);
//            scoring.add("grab_stack_one");
//
//            locations.add(new Point(-0.5 * TILE_LENGTH, 1 * TILE_LENGTH));
//            thetas.add(3 * Math.PI / 2);
//            scoring.add("initial");
//
//            locations.add(new Point(-1.5 * TILE_LENGTH, 1.5 * TILE_LENGTH));
//            thetas.add(3 * Math.PI / 2);
//            scoring.add("backdrop_first_mark");
//        }

        // cycle (+2) second stack
//        for (int i = 0; i < 1; i = i + 1) {
//            locations.add(new Point(0.5 * TILE_LENGTH, -1.75 * TILE_LENGTH));
//            thetas.add(4.925 * Math.PI / 3);
//            intake.add("?");
//
//            locations.add(new Point(1.5 * TILE_LENGTH, 1.5 * TILE_LENGTH));
//            thetas.add(3 * Math.PI / 2);
//            intake.add("backdrop_first_mark");
//        }

        POT.addAll(Arrays.asList(lqr));

        Thread.sleep(250);

        begin_wait = -1;
        wait = 0.5;

        cooked = false;
        cooking = false;

        // Queue thread
//        new Thread(() -> {
//            while (opModeIsActive()) {
//            }
//        }).start();

        while (opModeIsActive()) {
            begin();

            if (!POT.isEmpty() && Robot.time - begin_wait > wait) {
                Ingredient[] recipe = POT.peek();

                // always wait 0.5 seconds before each recipe
                wait = 0.5;

                for (Ingredient ingredient : recipe) {
                    switch (ingredient.type) {
                        case "position":
                            desired_position = ingredient.position;
                            break;
                        case "theta":
                            desired_theta = ingredient.value;
                            break;
                        case "wait":
                            begin_wait = Robot.time;
                            wait = ingredient.value;

                            break;
                    }
                }

                cooking = true;
            }

            cooked = go_to(desired_position, TELEOP_FIELD);
            telemetry.addData("desired_position.x", desired_position.x);
            telemetry.addData("desired_position.y", desired_position.y);
            cooked = turn_to(desired_theta, 1) && cooked;
            telemetry.addData("desired_theta", cooked);
            telemetry.addData("desired_theta", desired_theta);

            if (cooking && cooked) {
                // establish wait first
                begin_wait = Robot.time;

                POT.poll();

                cooking = false;

                path = null;
            }

            absolute_drive(y_power, x_power, rotational_power);

            debug();

            end();
        }
    }

    public void debug() {

        telemetry.addData("X: ", Robot.X);
        telemetry.addData("Y: ", Robot.Y);

        telemetry.addData("x power: ", final_x_power);
        telemetry.addData("y power: ", final_y_power);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        dashboardTelemetry.addData("front left power: ", front_left_motor_power);
        dashboardTelemetry.addData("front right power: ", front_right_motor_power);
        dashboardTelemetry.addData("back left power: ", back_left_motor_power);
        dashboardTelemetry.addData("back right power: ", back_right_motor_power);

        dashboardTelemetry.addData("DestX: ", desired_position.x);
        dashboardTelemetry.addData("DestY: ", desired_position.y);

        dashboardTelemetry.addData("X: ", X);
        dashboardTelemetry.addData("Y: ", Y);
        dashboardTelemetry.addData("imu: ", theta);

        dashboardTelemetry.update();

        telemetry.update();
    }
}