package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

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

@TeleOp(name = "Red TeleOp", group = "Linear OpMode")

public class RedT extends LinearOpMode {

    public boolean b1 = false;
    public boolean y1 = false;
    public boolean x1 = false;
    public boolean a1 = false;

    public boolean up1 = false;
    public boolean down1 = false;

    public boolean a2 = false;
    public boolean b2 = false;
    public boolean y2 = false;
    public boolean x2 = false;

    public boolean right_bumper2 = false;
    public boolean left_bumper2 = false;
    public boolean up_bumper2 = false;
    public boolean down_bumper2 = false;


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

        // initial position
        X = 3 * TILE_LENGTH - LENGTH / 2;
        Y = 0.5 * TILE_LENGTH;

        // initial theta
        initial_theta = Math.PI;
        theta = Math.PI;
        desired_theta = Math.toRadians(180);

        // scoring
        desired_linear_slide_position = 0;

        Queue<Point> locations = new LinkedList<>();

        // waiting...
        waitForStart();
        runtime.reset();

        //when have auto put this in that file and remove from here
        reset(hardwareMap);

        // key presses
        b1 = false;
        y1 = false;
        x1 = false;
        a1 = false;

        up1 = false;
        down1 = false;


        // driver 1
        new Thread(() -> {
            while (opModeIsActive()) {
                settings(gamepad1.left_stick_button, gamepad1.right_stick_button);

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

                if (gamepad1.a && !a1) {
                    locations.add(new Point(1.5 * TILE_LENGTH, 0.5 * TILE_LENGTH));

                    a1 = true;
                } else if (!gamepad1.a) {
                    a1 = false;
                }

                if (gamepad1.dpad_up && !up1) {
                    locations.add(new Point(-0.5 * TILE_LENGTH, -1.5 * TILE_LENGTH));
                    locations.add(new Point(-1.5 * TILE_LENGTH, -1.5 * TILE_LENGTH));

                    desired_theta = Math.toRadians(225);

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

                if (gamepad1.right_stick_x != 0) {
                    desired_theta = theta;
                }
            }
        }).start();

        // driver 2
        new Thread(() -> {
            while (opModeIsActive()) {
                // first mark
                if (gamepad2.a && !a2) {

                    // goofy ahh sleep
                    try { Thread.sleep(300); } catch (Exception ignored) {}

                    desired_linear_slide_position = 0.7;
                    servos.pass();
                    //pivot is supposed to be 0
                    servos.pivot(0,0);
                    servos.moveArm(0);

                    a2 = true;
                } else if (!gamepad2.a) {
                    a2 = false;
                } if (gamepad2.x && !x2) {

                    // goofy ahh sleep
                    try { Thread.sleep(300); } catch (Exception ignored) {}

                    desired_linear_slide_position = 0.01;
                    servos.retract();
                    servos.moveArm(0);
                    servos.pivot(.5,.5);


                    x2 = true;
                } else if (!gamepad2.x) {
                    x2 = false;
                }
                if (gamepad2.y && !y2) {

                    // goofy ahh sleep
                    try { Thread.sleep(300); } catch (Exception ignored) {}

                    desired_linear_slide_position = 0.25;
                    servos.extend();
                    servos.moveArm(.58);
                    servos.pivot(.5,.5);

                    y2 = true;
                } else if (!gamepad2.y) {
                    y2 = false;
                }

                if (gamepad2.b && !b2) {

                    // goofy ahh sleep
                    try { Thread.sleep(300); } catch (Exception ignored) {}

                    desired_linear_slide_position = 0.01;
                    servos.pass();
                    servos.moveArm(0);
                    //supposed to pivot to 0
                    servos.pivot(0,0);

                    b2 = true;
                } else if (!gamepad2.b) {
                    b2 = false;
                }

                if (gamepad2.right_trigger > .5) {
                    try { Thread.sleep(300); } catch (Exception ignored) {}
                    servos.openClaw();
                }
                if (gamepad2.left_trigger > .5) {
                    try { Thread.sleep(300); } catch (Exception ignored) {}
                    servos.closeClaw();
                }

                if (gamepad2.right_bumper) {
                    try { Thread.sleep(300); } catch (Exception ignored) {}
                    servos.extend();
                    servos.moveArm(.58);
                    servos.pivot(.9,.9);
                    servos.openClaw();
                }

                if (gamepad2.left_bumper) {
                    try { Thread.sleep(300); } catch (Exception ignored) {}
                    servos.closeClaw();
                    try { Thread.sleep(300); } catch (Exception ignored) {}
                    servos.retract();
                    servos.pivot(.3,.3);
                    servos.moveArm(0);
                }
//                double left = (Claw.diffyLeft.getPosition() + +gamepad2.right_stick_y + gamepad2.right_stick_x);
//                double right = (Claw.diffyRight.getPosition() + -gamepad2.right_stick_y + gamepad2.right_stick_x);
//                double factor = Math.min(left, right)/(Math.max(left, right));
//                Claw.diffyLeft.setPosition(left * factor);
//                Claw.diffyRight.setPosition(right * factor);



            }
        }).start();

        while (opModeIsActive()) {
            begin();

            // angular
            rotational_power = Drivetrain.rotational_power + gamepad1.right_stick_x;
           // turn_to(desired_theta, gamepad1.right_trigger);

            if (locations.isEmpty()) {
                // translational
                accelerate(gamepad1.left_stick_y, gamepad1.left_stick_x);

                if (FIELD_CENTRIC) {
                    field_centric_drive(
                            y_power * (1 - 0.75 * gamepad1.left_trigger),
                            x_power * (1 - 0.75 * gamepad1.left_trigger),
                            rotational_power * (1 - 0.75 * gamepad1.left_trigger)
                    );
                } else {
                    robot_centric_drive(
                            y_power * (1 - 0.75 * gamepad1.left_trigger),
                            x_power * (1 - 0.75 * gamepad1.left_trigger),
                            rotational_power * (1 - 0.75 * gamepad1.left_trigger)
                    );
                }
            } else {
                if (go_to(locations.peek(), TELEOP_FIELD)){
                    locations.poll();

                    Motion.path = null;
                }

                absolute_drive(y_power, x_power, rotational_power);
            }


            //claw.rotToDeg((angle_to_horizontal + desired_claw_angle + 10) * Math.signum(Math.cos(Arm.POSITION)));

            LinearSlide.go_to(desired_linear_slide_position);

            end();

            debug();
        }
    }

    public void debug() {
        telemetry.addData("IMU: ", Robot.imu.getRobotYawPitchRollAngles());

        telemetry.addData("X: ", Robot.X);
        telemetry.addData("Y: ", Robot.Y);

        telemetry.addData("theta: ", Robot.theta);

        telemetry.addData("field centric?", Drivetrain.FIELD_CENTRIC);

        telemetry.addData("imu data", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        telemetry.addData("diffyL", ServoErmSigma.diffyLeft.getPosition());
        telemetry.addData("diffyR", ServoErmSigma.diffyRight.getPosition());

        telemetry.update();
    }
}