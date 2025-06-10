package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Beto_Tank", group="Drive & hello")

public class Beto_oi extends LinearOpMode {

    private static final double posicao1 = 0.0;
    private static final double posicao2 = 0.3;

    boolean acionarServo = false;
    ElapsedTime servoTimer = new ElapsedTime();

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        Servo servo = hardwareMap.get(Servo.class, "servo");

        servo.setPosition(posicao1);

        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "leftBackDrive");
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "rightBackDrive");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Tank drive: cada lado controlado por um stick
            double leftPower = -gamepad1.left_stick_y;
            double rightPower = -gamepad1.right_stick_y;

            // Aplicar potência aos motores
            leftFrontDrive.setPower(leftPower);
            leftBackDrive.setPower(leftPower);
            rightFrontDrive.setPower(rightPower);
            rightBackDrive.setPower(rightPower);

            // Controle do servo
            if (gamepad1.a && !acionarServo) {
                servo.setPosition(posicao2);
                servoTimer.reset();
                acionarServo = true;
                sleep(100);
                servo.setPosition(posicao1);
                sleep(100);
                servo.setPosition(posicao2);
            }

            if (acionarServo && servoTimer.milliseconds() > 1000) {
                servo.setPosition(posicao1);
                acionarServo = false;
            }

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
