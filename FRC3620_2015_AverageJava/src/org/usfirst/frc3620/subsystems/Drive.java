// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3620.subsystems;

import org.usfirst.frc3620.PreferencesNames;
import org.usfirst.frc3620.Robot;
import org.usfirst.frc3620.RobotMap;
import org.usfirst.frc3620.RobotMode;
import org.usfirst.frc3620.commands.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class Drive extends Subsystem implements PIDSource, PIDOutput {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    DoubleSolenoid strafeSolenoid = RobotMap.driveStrafeSolenoid;
    SpeedController speedController2 = RobotMap.driveSpeedController2;
    SpeedController speedController1 = RobotMap.driveSpeedController1;
    SpeedController speedController3 = RobotMap.driveSpeedController3;
    SpeedController speedController0 = RobotMap.driveSpeedController0;
    RobotDrive robotDrive4 = RobotMap.driveRobotDrive4;
    Gyro driveGyro = RobotMap.drivedriveGyro;
    SpeedController strafeMotor = RobotMap.driveStrafeMotor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    double speedYlimit = .65;
    double speedXlimit = .65;
    double pT = .075;
    double iT = .000002;
    double dT = 0;
    double sideStick = 0;
    long timeStart;
    long time2;
    public boolean assistEnabled = false;
    JoystickStabilization joystickStabilization = null;
    
    PIDController teleOpDriveAssist = new PIDController(pT, iT, dT, this, this);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new DriveArcadeCommand());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        teleOpDriveAssist.enable();
        gyroReset();

        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    enum DesiredStrafeState {
    	LEAVE_IT, UP, DOWN
    }
    
    /*
     * pull this out to a separate method so that we can get to it from telemetry and other places
     */
    public double getJoystickY(GenericHID hid) {
    	return hid.getRawAxis(1);
    }
    
    /*
     * pull this out to a separate method so that we can get to it from telemetry and other places
     */
    public double getJoystickX(GenericHID hid) {
    	return hid.getRawAxis(4);
    }
    
    public void arcadeDrive(GenericHID hid)
    {
    	 double move = getJoystickY(hid);
         double  rX= getJoystickX(hid);
         
    	double joystickStrafe = getJoystickStrafe(hid);
    	setStrafeMotor(joystickStrafe);
    	DesiredStrafeState desiredStrafeState = DesiredStrafeState.LEAVE_IT;
    	if(Math.abs(joystickStrafe) > 0.2)
    	{
    		desiredStrafeState = DesiredStrafeState.DOWN;
    	}
    	else
    	{
    		if((move > .5) || (hid.getRawButton(4)))
    		{
    			desiredStrafeState = DesiredStrafeState.UP;
    		}
    	}
    	switch (desiredStrafeState) {
		case UP:
			strafeUp();
			break;
		case DOWN:
			strafeDown();
			break;
		
		default:
			break;
		}
    	
    	teleOpDriveAssist.setOutputRange(-.5, .5);
    	// robotDrive4.arcadeDrive(hid.getY(), hid.getX());
        // double  x = hid.getRawAxis(1); //left x axis

        JoystickPosition joystickPosition = joystickStabilization.stabilizeJoystick(rX, move);
        	  
        //System.out.printf ("Raw Joysitck %f, %f Processed Joystick %f, %f \n", rX, move, joystickPosition.getX(), joystickPosition.getY());
        
       isTurning();
        if (isTurning() == true)
        {
        	robotDrive4.arcadeDrive(joystickPosition.getY(), joystickPosition.getX());
        	isTurning();
        	
        }
        else if (assistEnabled == true)
        {
        	robotDrive4.arcadeDrive(joystickPosition.getY(), sideStick);  
        	isTurning();
        }
        else
        {
        	robotDrive4.arcadeDrive(joystickPosition.getY(), joystickPosition.getX());
        }
        
        //robotDrive4.arcadeDrive(m3, r3);
    }
    public void turnMotorsOn()
    {
    	robotDrive4.arcadeDrive(1, 0);
    	
    }


    public void turnMotorsOff()
    {
    	robotDrive4.arcadeDrive(0, 0);
    }
    /**
     * sets the arcade drive to the parameters. 
     * @param move. the y axis. passing in a positive number is forward.
     * @param rotate. the x axis.
     */
    public void setDrive(double move, double rotate)
    {
    	robotDrive4.arcadeDrive(-move, rotate);   
    }
    
    public double gyroAngle = 0;
    public double gyroRate = 0;
    public double getGyroAngle()
    {
    	gyroAngle = driveGyro.getAngle();
    	return gyroAngle;
    	
    }
    public double getGyroRate()
    {
    	gyroRate = driveGyro.getRate();
    	return gyroRate;
    }
    public void gyroReset()
    {
    	
    	driveGyro.reset();
    	
    }
    private double p;
    private double i;
    private double d;
    public double getPIDp()
    {
    	
    	 double p = SmartDashboard.getNumber("p", .075);
    	 SmartDashboard.putNumber("P value", p);
    	 return p;
    }
    public double getPIDi()
    {
    	i = SmartDashboard.getNumber("i", 0);
    	SmartDashboard.putNumber("I value", i);
    	return i;
    }
    public double getPIDd()
    {
    	
    	d = SmartDashboard.getNumber("d", 0);
    	SmartDashboard.putNumber("D value", d);
    	return d;
    }
    int n = 0;
    public boolean isTurning()
    {
    	if (Robot.oi.driverJoystick.getRawAxis(4) < .2 && Robot.oi.driverJoystick.getRawAxis(4) > -.2)
        {
    		if (n == 0)
    		{
        		timeStart = logTime();
        		n++;
    		}
    		time2 = logTime();
    		if (time2 - timeStart >= 200 && n == 1)
    		{
    			gyroReset();
    			n++;
    		}
        	return false;
        }
    	else
    	{
    		n = 0;
    		return true;
    	}
    }
	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		sideStick = -output;
	}
	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		return getGyroAngle();
	}
	public long logTime()
	{
		return System.currentTimeMillis();
	}
	public void resetEncoders()
	{
		Robot.encoderSubsystem.rightEncoder.reset();
		Robot.encoderSubsystem.leftEncoder.reset();
	}
	public void strafeDown(){
		strafeSolenoid.set(Value.kReverse);
	}
	public void strafeUp(){
		strafeSolenoid.set(Value.kForward);
	}
	public void setStrafeMotor(double power)
	{
		strafeMotor.set(power);
	}
	public double getJoystickStrafe(GenericHID hid)
	{
		double leftTrigger = hid.getRawAxis(2);
		double rightTrigger = hid.getRawAxis(3);
		return rightTrigger-leftTrigger;
	}
	
	public void allInit(RobotMode mode)
	{
		Robot.dumpPreferences();
		Robot.drive.setJoyStabalType(Robot.preferences.getString(PreferencesNames.JOY_STABAL_CHOICE, "xx"));
		strafeUp();
	}
	
	public void setJoyStabalType(String type)
	{
		if(type.equals(PreferencesNames.JOY_STABAL_RAISE_TO_POWER))
		{
			joystickStabilization = new RaiseToPowerJoystickStabilization();
			
		}
		else if(type.equals(PreferencesNames.JOY_STABAL_SLEW_LIMIT)) {
			
			joystickStabilization = new SlewLimitJoystickStabilization();
		}
		else
		{
			joystickStabilization = new DoNothingJoystickStabilization();
		}
		System.out.println("Stabilization = " + joystickStabilization + " " + type);
    }
}

