package org.firstinspires.ftc.teamcode.mechanics;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//import org.firstinspires.ftc.teamcode.TeleOp.Main;

public class ServoErmSigma {
    //     Ports: 0         1           2         3
    public static Servo diffyLeft;
    public static Servo diffyRight;
    public static Servo finger;
    public static Servo linkage;
    public static Servo arm1;
    public static Servo arm2;



    //public double intakeLeft = 300, intakeRight = 0, outtakeLeft = 200, outtakeRight = 100;

    public ServoErmSigma(HardwareMap hardwareMap){
        diffyLeft = hardwareMap.servo.get("diffyL");
        diffyRight = hardwareMap.servo.get("diffyR");
        finger = hardwareMap.servo.get("finger");
        linkage = hardwareMap.servo.get("link");
        arm1 = hardwareMap.servo.get("armL");
        arm2 = hardwareMap.servo.get("armR");
    }
    public static void openClaw() {
        finger.setPosition(1);
    }
    public static void closeClaw() {
        finger.setPosition(0);
    }
    public static void upArm() {
        arm1.setPosition(0);
        arm2.setPosition(1);
    }
    public static void midArm() {
        arm1.setPosition(.5);
        arm2.setPosition(.5);
    }
    public static void downArm() {
        arm1.setPosition(.9);
        arm2.setPosition(0.1);
    }

    public static void extend() {
        linkage.setPosition((45+180)/300.0);
    }
    public static void retract() {
        linkage.setPosition(.5);
    }
    public static void pass(){ linkage.setPosition((180-20)/300.0);}

    public static void pivot(double pos) {
        diffyLeft.setPosition(diffyLeft.getPosition()+pos);
        diffyRight.setPosition(diffyRight.getPosition()-pos);
    }

    public static void turnRight(double pos) {
        diffyLeft.setPosition(pos);
        diffyRight.setPosition(pos);
    }

    public static void turnLeft(double pos) {
        diffyLeft.setPosition(-pos);
        diffyRight.setPosition(-pos);
    }



}