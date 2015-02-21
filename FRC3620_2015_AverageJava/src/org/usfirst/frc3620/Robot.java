// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3620;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.TimeZone;

import javax.print.attribute.standard.Compression;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.hal.CompressorJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3620.UDPReciever;
import org.usfirst.frc3620.commands.*;
import org.usfirst.frc3620.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	Command autonomousCommand;
	public static OI oi;
	
	static RobotMode currentRobotMode = RobotMode.INIT, previousRobotMode;
	PowerDistributionPanel pdp;
	
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static LiftPID liftPID;;
    public static Drive drive;
    public static pneumatics pneumatics;
    public static Intake intake;
    public static encoderSubsystem encoderSubsystem;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	DataLogger dataLogger;
	
	public static CameraServer cameraServer;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit()
	{

		RobotMap.init();

		try
		{
			new UDPReciever().start();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        liftPID = new LiftPID();
        drive = new Drive();
        pneumatics = new pneumatics();
        intake = new Intake();
        encoderSubsystem = new encoderSubsystem();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();
		Robot.pneumatics.checkForPneumatics();
		// instantiate the command used for the autonomous period
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        autonomousCommand = new autonomous();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

		// Check for pneumatics
		System.out.println("havePneumatics " + Robot.pneumatics.havePneumatics);
		if (Robot.pneumatics.havePneumatics)
		{
			RobotMap.pneumaticsCompressor1.start();
		}

		pdp = new PowerDistributionPanel();
		
		// Set dataLogger and Time information
		TimeZone.setDefault(TimeZone.getTimeZone("America/Detroit"));
		File logDirectory = new File("/home/lvuser/logs");
		if (!logDirectory.exists())
		{
			logDirectory.mkdir();
		}
		if (logDirectory.isDirectory())
		{
			dataLogger = new DataLogger(logDirectory);
			dataLogger.setMinimumInterval(1000);
		}
		
		// startup camera server
		cameraServer = CameraServer.getInstance();
		// 25 seems to be less laggy than 50. 10 does not seem to be much better lag wise
		// then 25, and is really poor quality.
        cameraServer.setQuality(25);
        //the camera name (ex "cam0") can be found through the roborio web interface
        cameraServer.startAutomaticCapture("cam0");

	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	@Override
	public void disabledInit()
	{
        allInit(RobotMode.DISABLED);
	}

	@Override
	public void disabledPeriodic()
	{
		allBeginningOfPeriodic();
		Scheduler.getInstance().run();
		allEndOfPeriodic();
	}

	@Override
	public void autonomousInit()
	{
		allInit(RobotMode.AUTONOMOUS);
		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic()
	{
		allBeginningOfPeriodic();
		Scheduler.getInstance().run();
		allEndOfPeriodic();
	}

	@Override
	public void teleopInit()
	{
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		
		// the cancel was *deliberately* before this!
		allInit(RobotMode.TELEOP);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic()
	{
		allBeginningOfPeriodic();
		Scheduler.getInstance().run();
		Robot.drive.isTurning();
		allEndOfPeriodic();
	}

	@Override
	public void testInit()
	{
		allInit(RobotMode.TEST);
	}
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic()
	{
		allBeginningOfPeriodic();
		LiveWindow.run();
		allEndOfPeriodic();
	}
	
	void allInit (RobotMode newMode)
	{
		previousRobotMode = currentRobotMode;
		currentRobotMode = newMode;
		
		// if anyone needs to know about mode changes, let
		// them know here.
		Robot.drive.allInit(newMode);
	}

	/**
	 * This gets called at the beginning of any *periodic().
	 * We're putting this here "just in case".
	 */
	void allBeginningOfPeriodic() {
		//
	}

	/**
	 * This gets called at the end of any *periodic().
	 * We're putting this here "just in case".
	 */
	void allEndOfPeriodic()
	{
		if (dataLogger.shouldLogData())
		{
			
			dataLogger.addDataItem("Right drive current", pdp.getCurrent(1));
			dataLogger.addDataItem("Right drive motor power", RobotMap.driveSpeedController3.get());
			dataLogger.addDataItem("Left drive current", pdp.getCurrent(14));
			dataLogger.addDataItem("left drive motor power", RobotMap.driveSpeedController2.get());
			dataLogger.addDataItem("Lift motor current", pdp.getCurrent(15));
			dataLogger.saveDataItems();
		}
		drive.getGyroAngle();
		SmartDashboard.putNumber("GyroAngle", drive.gyroAngle);
		encoderSubsystem.getLeftEncoder();
		SmartDashboard.putNumber("Left encoder. Inches traveled.",
				encoderSubsystem.leftEncoderValue);
		encoderSubsystem.getRightEncoder();
		SmartDashboard.putNumber("Right encoder. Inches traveled.",
				encoderSubsystem.leftEncoderValue);
		SmartDashboard.putNumber("Lift encoder", liftPID.liftEncoderValue());
		SmartDashboard.putNumber("Setpoint for lift: ", Robot.liftPID.getSetpoint());
		SmartDashboard.putNumber("DriveEncoder: ", Robot.encoderSubsystem.getRightEncoder());
		SmartDashboard.putBoolean("limit top", Robot.liftPID.limitTop());
		SmartDashboard.putBoolean("limit Bottom", Robot.liftPID.limitBottom());
		if (Robot.pneumatics.havePneumatics)
		{
			//System.out.println(String.format("switch=%s, couurent=%f",
					//RobotMap.pneumaticsCompressor1.getPressureSwitchValue(),
					//RobotMap.pneumaticsCompressor1.getCompressorCurrent()));
		}
		
		double liftPosition = liftPID.liftEncoderValue();
		double setPoint = liftPID.getPIDController().getSetpoint();
		double motorPower = RobotMap.liftPIDliftMotor.get();
		//System.out.printf("setpoint = %f, position = %f, pwoer = %f\n", setPoint, liftPosition, motorPower);
		//System.out.println("condition: " + Robot.liftPID.getCondition());
		
	}
	
	public static RobotMode getCurrentRobotMode() {
		return currentRobotMode;
	}
	
	public static RobotMode getPreviousRobotMode() {
		return previousRobotMode;
	}

}
