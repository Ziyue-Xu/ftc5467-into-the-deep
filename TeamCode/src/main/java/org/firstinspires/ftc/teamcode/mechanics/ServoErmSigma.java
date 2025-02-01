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
        arm2.setDirection(Servo.Direction.REVERSE);
        diffyRight.setDirection(Servo.Direction.REVERSE);
    }
    public void openClaw() {
        finger.setPosition(.7);
    }
    public void closeClaw() {
        finger.setPosition(.3);
    }
    public void moveArm(double pos) {
        arm1.setPosition(pos);
        arm2.setPosition(pos);
    }
    public void extend() {
        linkage.setPosition(.5);
    }
    public void retract() {
        linkage.setPosition(.9);
    }
    public void pass(){ linkage.setPosition(1);}

    public void pivot(double posL, double posR) {
        diffyLeft.setPosition(posL);
        diffyRight.setPosition(posR);
    }


}