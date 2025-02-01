package org.firstinspires.ftc.teamcode.stuff;

import org.firstinspires.ftc.teamcode.mathemagics.*;

public class Ingredient {

    public String type;

    // theta + arm + linear slide
    public double value;
    public Ingredient(String type, double value) {
        this.type = type;
        this.value = value;
    }

    // position
    public Point position;
    public Ingredient(String type, Point position) {
        this.type = type;
        this.position = position;
    }

    // claw
    public String claw;
    public Ingredient(String type, String claw, String bruh) {
        this.type = type;
        this.claw = claw;
    }
}
