package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Dani e de vina pentru orice x3 2026", group="OpMode")
public class CoduOficial extends RobotHardware {
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {
        init_hardware(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            tele.addData("Status", "Run Time: " + runtime.toString());

//            intake();

            // if(STOP())
              // continue;

            wheelMovement();

            tele.update();
        }
    }
}


