package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

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

@TeleOp(name = "🥶 TeleOp", group = "Linear OpMode")

public class BlueT extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // setup everything
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

        // initial position
        X = -3 * TILE_LENGTH + LENGTH / 2;
        Y = 0.5 * TILE_LENGTH;

        // initial theta
        initial_theta = Math.toRadians(0);
        theta = Math.toRadians(0);
        desired_theta = Math.toRadians(0);

        Queue<Point> locations = new LinkedList<>();

        path = null;

        // key presses
        boolean b1 = false;
        boolean y1 = false;
        boolean x1 = false;

        boolean up1 = false;
        boolean down1 = false;

        while (opModeIsActive()) {
            begin();

            settings(gamepad1.left_stick_button, gamepad1.right_stick_button);

            // macro instruction(s)
            if (gamepad1.b && !b1) {
                desired_theta = theta - Math.toRadians(45);

                b1 = true;
            } else if (!gamepad1.b) {
                b1 = false;
            }

            if (gamepad1.y && !y1) {
                desired_theta = theta + Math.toRadians(45);

                y1 = true;
            } else if (!gamepad1.y) {
                y1 = false;
            }

            if (gamepad1.dpad_up && !up1) {
                locations.add(new Point(-1.5 * TILE_LENGTH, -1.5 * TILE_LENGTH));

                up1 = true;
            } else if (!gamepad1.dpad_up) {
                up1 = false;
            }

            if (gamepad1.dpad_down && !down1) {
                locations.add(new Point(1.5 * TILE_LENGTH, 1.5 * TILE_LENGTH));

                down1 = true;
            } else if (!gamepad1.dpad_down) {
                down1 = false;
            }

            if (gamepad1.x && !x1) {
                locations.clear();
                Motion.path = null;

                x_power = 0;
                y_power = 0;
                rotational_power = 0;

                x1 = true;
            } else if (!gamepad1.x) {
                x1 = false;
            }

            // angular

            rotational_power = Drivetrain.rotational_power + gamepad1.right_stick_x;
            turn_to(desired_theta, gamepad1.right_trigger);

            if (locations.isEmpty()) {
                // translational
                accelerate(gamepad1.left_stick_y, gamepad1.left_stick_x);

                if (FIELD_CENTRIC) {
                    field_centric_drive(
                            y_power * (1 - 0.5 * gamepad1.left_trigger),
                            x_power * (1 - 0.5 * gamepad1.left_trigger),
                            rotational_power * (1 - 0.5 * gamepad1.left_trigger)
                    );
                } else {
                    robot_centric_drive(
                            y_power * (1 - 0.5 * gamepad1.left_trigger),
                            x_power * (1 - 0.5 * gamepad1.left_trigger),
                            rotational_power * (1 - 0.5 * gamepad1.left_trigger)
                    );
                }
            } else {
                if (go_to(locations.peek(), TELEOP_FIELD)){
                    locations.poll();

                    Motion.path = null;
                }

                absolute_drive(y_power, x_power, rotational_power);
            }

            end();

            if (gamepad1.right_stick_x != 0) {
                desired_theta = theta;
            }

            debug();
        }
    }

    public void debug() {
        telemetry.addData("IMU: ", Robot.imu.getRobotYawPitchRollAngles());

        telemetry.addData("X: ", Robot.X);
        telemetry.addData("Y: ", Robot.Y);

        telemetry.addData("theta: ", Robot.theta);

        telemetry.addData("field centric?", Drivetrain.FIELD_CENTRIC);

        telemetry.update();
    }
}