package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utils.Utils;


public class RobotHardware extends LinearOpMode {
    public DcMotor motorFs, motorFd, motorSs, motorSd;
    public DcMotor[] wheelMotors = new DcMotor[4];
    public DcMotor[] shooterMotors = new DcMotor[2];
    // public DcMotor intakeMotor;
    // public DcMotor shooterStanga, shooterDreapta;
    // public Servo intakeServo;
    public DcMotor lantMT;
    public Servo bila;
    boolean toggleB = false;
    boolean toggleY = false;
    boolean toggleX = false;
    boolean toggleDpad_Up = false;
    boolean toggleDpad_Down = false;
    boolean prev_pressedX = false;
    boolean prev_pressedDpad_UP = false;
    boolean prev_pressedDpad_DOWN = false;
    boolean prev_pressedY = false;
    boolean prev_pressedB = false;
    public Servo odoWheelY, odoWheelX;

    MultipleTelemetry tele;

    public void init_hardware(HardwareMap ahwMap) {
        tele = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        motorFs = ahwMap.get(DcMotor.class, "motor Fs");
        motorSs = ahwMap.get(DcMotor.class, "motor Ss");
        motorSd = ahwMap.get(DcMotor.class, "motor Sd");
        motorFd = ahwMap.get(DcMotor.class, "motor Fd");
        bila = ahwMap.get(Servo.class,"bila");


        odoWheelX = ahwMap.get(Servo.class, "odometry X");
        odoWheelY = ahwMap.get(Servo.class, "odometry Y");

        odoWheelX.setDirection(Servo.Direction.FORWARD);
        odoWheelY.setDirection(Servo.Direction.REVERSE);

        setOdoPositions(Utils.ODOMETRY_UP);

//        bila.setDirection(Servo.Direction.REVERSE);

        motorFs.setDirection(DcMotor.Direction.REVERSE);
        motorFd.setDirection(DcMotor.Direction.REVERSE);
        motorSs.setDirection(DcMotor.Direction.FORWARD);
        motorSd.setDirection(DcMotor.Direction.REVERSE);
    }

    public void frana() {
        for (DcMotor motor : wheelMotors)
            motor.setPower(0);
    }
    public void setOdoPositions(float position) {
        odoWheelX.setPosition(position);
        odoWheelY.setPosition(position + 0.01f);
    }

    //----------------INTAKEEEEE---------------


    //---------------------Launch------ -------------------------

    //----------------WHEELS----------------

    public void wheelMovement() {

        if (gamepad1.a) {
            frana();
            return;
        }

        float x, y, t;
        t = -gamepad1.right_stick_x;
        y = gamepad1.right_trigger - gamepad1.left_trigger;
        x = gamepad1.left_stick_x;

        float fd, fs, sd, ss;
        fs = y + x - t;
        fd = y - x + t;
        ss = y - x - t;
        sd = y + x + t;

        motorFs.setPower(fs);
        motorFd.setPower(fd);
        motorSs.setPower(ss);
        motorSd.setPower(sd);

        if(gamepad1.dpad_left)
        {
            motorFs.setPower(-1);
            motorFd.setPower(1);
            motorSs.setPower(1);
            motorSd.setPower(-1);
        }
        if(gamepad1.dpad_right)
        {
            motorFs.setPower(1);
            motorFd.setPower(-1);
            motorSs.setPower(-1);
            motorSd.setPower(1);



        }
    }

    //________________STOP_________________
    public int STOP(){
        if(gamepad2.a){
            toggleY = false;
            toggleX = false;
            toggleB = false;
            return 1;
        }

        return 0;
    }


    //--------------------AUTO------------------

    public void initAuto(){
        setOdoPositions(Utils.ODOMETRY_DOWN);
    }

    @Override//trb ca sa nu dea eroare
    public void runOpMode() throws InterruptedException {}
}