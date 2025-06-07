package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Beto", group="Launcher & hello")

public class Beto_oi extends LinearOpMode {

    private static final double SERVO_PRONTO = 0.0; //o servo está na posicao correta para lancao o aviao
    private static final double SERVO_LANCOU = 0.5; //o servo lancou o aviao

    boolean acionarServo = false;
    ElapsedTime servoTimer = new ElapsedTime();

    // nao esquecer de colocar os mesmos nomes no driver hub
    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        Servo servo = hardwareMap.get(Servo.class, "servo");

        servo.setPosition(SERVO_PRONTO);

        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "leftBackDrive");
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "rightBackDrive");


        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // quando o piloto aperta start
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double max;

            // direito gira e esquerdo anda
            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            //potencia
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            if (gamepad1.a && !acionarServo) {
                servo.setPosition(SERVO_LANCOU);
                servoTimer.reset();
                acionarServo = true;
                sleep(100);
                servo.setPosition(SERVO_PRONTO);
                sleep(100);
                servo.setPosition(SERVO_LANCOU);
            }

            if (acionarServo && servoTimer.milliseconds() > 1000) { // 200ms depois
                servo.setPosition(SERVO_PRONTO);
                acionarServo = false;
            }

            // define a potencia dos motores com o que ja foi calculado
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            // tempo de jogo
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }}