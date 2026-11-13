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
    public DcMotor intakeMotorSus, intakeMotorJos;
    // public DcMotor shooterStanga, shooterDreapta;
    // public Servo intakeServo;
    public DcMotor lantMT;
//    public Servo bila;
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

    public static double VITEZA = 1.0;

    MultipleTelemetry tele;

    public void init_hardware(HardwareMap ahwMap) {
        tele = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        motorFs = ahwMap.get(DcMotor.class, "motor Fs");
        motorSs = ahwMap.get(DcMotor.class, "motor Ss");
        motorSd = ahwMap.get(DcMotor.class, "motor Sd");
        motorFd = ahwMap.get(DcMotor.class, "motor Fd");
        intakeMotorSus = ahwMap.get(DcMotor.class, "motor is");
        intakeMotorJos = ahwMap.get(DcMotor.class, "motor ij");




       // odoWheelX = ahwMap.get(Servo.class, "odometry X");
       // odoWheelY = ahwMap.get(Servo.class, "odometry Y");

      //  odoWheelX.setDirection(Servo.Direction.FORWARD);
      //  odoWheelY.setDirection(Servo.Direction.REVERSE);

      //  setOdoPositions(Utils.ODOMETRY_UP);

        wheelMotors = new DcMotor[]{motorFs, motorFd, motorSs, motorSd};

        for (DcMotor motor : wheelMotors)
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        for (DcMotor motor : wheelMotors)
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorFs.setDirection(DcMotor.Direction.REVERSE);
        motorFd.setDirection(DcMotor.Direction.REVERSE);
        motorSs.setDirection(DcMotor.Direction.REVERSE);
        motorSd.setDirection(DcMotor.Direction.FORWARD);
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

    int intakeState = 0; // 0 = oprit, 1 = baga bila, -1 = scoate bila

    public void intake() {
        boolean apasatX = gamepad2.x && !prev_pressedX;
        boolean apasatDown = gamepad2.dpad_down && !prev_pressedDpad_DOWN;

        if (apasatX) {
            intakeState = (intakeState == 1) ? 0 : 1;
        } else if (apasatDown) {
            intakeState = (intakeState == -1) ? 0 : -1;
        }

        if (intakeState == 1) {
            intakeMotorSus.setPower(-1);
            intakeMotorJos.setPower(-1);
        } else if (intakeState == -1) {
            intakeMotorSus.setPower(0.87);
            intakeMotorJos.setPower(0.87);
        } else {
            intakeMotorSus.setPower(0);
            intakeMotorJos.setPower(0);
        }

        prev_pressedX = gamepad2.x;
        prev_pressedDpad_DOWN = gamepad2.dpad_down;
    }

    //---------------------Launch------ -------------------------

    //----------------WHEELS----------------

    // joystick-urile nu revin niciodata fix la 0, ignoram valorile mici
    public static double DEADZONE = 0.1;

    public double deadzone(double valoare) {
        return Math.abs(valoare) < DEADZONE ? 0 : valoare;
    }

    public void wheelMovement() {

        if (gamepad1.a) {
            frana();
            return;
        }

        double x, y, t;
        t = -deadzone(gamepad1.right_stick_x) * VITEZA;
        y = gamepad1.right_trigger - gamepad1.left_trigger; // fata/spate pe triggere = viteza maxima
        x = deadzone(gamepad1.left_stick_x) * VITEZA;

        double fd, fs, sd, ss;
        fs = y + x - t;
        fd = y - x + t;
        ss = y - x - t;
        sd = y + x + t;

        // normalizare: daca vreo putere trece de 1, le micsoram pe toate proportional
        double max = Math.max(1.0, Math.max(Math.max(Math.abs(fs), Math.abs(fd)),
                                            Math.max(Math.abs(ss), Math.abs(sd))));
        fs /= max;
        fd /= max;
        ss /= max;
        sd /= max;

        motorFs.setPower(fs);
        motorFd.setPower(fd);
        motorSs.setPower(ss);
        motorSd.setPower(sd);

        tele.addData("stick stang x", gamepad1.left_stick_x);
        tele.addData("stick drept x", gamepad1.right_stick_x);
        tele.addData("Fs / Fd", "%.2f / %.2f", fs, fd);
        tele.addData("Ss / Sd", "%.2f / %.2f", ss, sd);

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


    //--------------------AUTO------------------

    public void initAuto(){
        setOdoPositions(Utils.ODOMETRY_DOWN);
    }

    @Override//trb ca sa nu dea eroare
    public void runOpMode() throws InterruptedException {}
}