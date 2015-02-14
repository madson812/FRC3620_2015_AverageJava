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

import org.usfirst.frc3620.Robot;
import org.usfirst.frc3620.RobotMap;
import org.usfirst.frc3620.commands.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class LiftPID extends PIDSubsystem {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    SpeedController liftMotor = RobotMap.liftPIDliftMotor;
    DigitalInput liftLimitTop = RobotMap.liftPIDliftLimitTop;
    DigitalInput liftLimitBottom = RobotMap.liftPIDliftLimitBottom;
    Encoder liftEncoder = RobotMap.liftPIDLiftEncoder;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final double bottom = 0;
    private final double basiloid = 12.0;
    private final double top = 24;
    private final double releaseStack = 5.0;
    public double setPoint = 0;
    private int condition = 0;
    static final int lift_Bottom = 0;
    static final int lift_Basiloid = 1;
    static final int lift_Top = 2;
    static final int lift_StackRelease = 3;
    // Initialize your subsystem here
    public LiftPID() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        super("LiftPID", -1.0, 0.0, 0.0);
        setAbsoluteTolerance(0.2);
        getPIDController().setContinuous(false);
        LiveWindow.addActuator("LiftPID", "PIDSubsystem Controller", getPIDController());
        getPIDController().setOutputRange(-0.5, 0.5);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        
        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
       
        setSetpoint(0);
        enable();


    }
    
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new liftManual());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
    
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
        return liftEncoder.pidGet();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
    }
    
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
    	

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        liftMotor.pidWrite(output);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        
        //System.out.printf("sensor = %f, setPoint = %f, output = %f\n", returnPIDInput(), getPIDController().getSetpoint(), output);
    }
    public void positionBottom()
    {
    	positionToX(bottom);
    	condition = lift_Bottom;
    }
    public void positionBasiloid()
    {
    	positionToX(basiloid);
    	condition = lift_Basiloid;
    }
    public void positionTop()
    {
    	positionToX(top);
    	condition = lift_Top;
    }
    void positionToX (double x) {
    	enable();
    	setSetpoint(x);
    	setPoint = x;
    }
    public void positionReleaseStack()
    {
    	setSetpoint(releaseStack);
    	setPoint = releaseStack;
    	condition = lift_StackRelease;
    }
    public void liftUp()
    {
    	if (condition == lift_Bottom) //bottom
    	{
    		positionBasiloid(); //move to basiloid
    		System.out.println("lift from bottom to basiloid: " + setPoint);
    	}
    	else if (condition == lift_Basiloid) //at basiloid
    	{
    		positionTop();
    		System.out.println("lift from basiloid to top: " + setPoint);
    	}
    	else if (condition == lift_Top) //currently user controlled
    	{
    		System.out.println("Already at top!! Cannot move up!!");
    	}
    	else if (condition == lift_StackRelease) //remove stack position
    	{
    		positionBasiloid(); //move to basiloid
    		System.out.println("Moving from remove to Basiloid: " + setPoint);
    	}
    }
    
    public void liftDown()
    {
    	if (condition == lift_Top) //user controlled
    	{
    		positionBasiloid(); //move to basiloid
    		System.out.println("drop from to to basiloid: "  + setPoint);
    	}
    	else if (condition == lift_Basiloid) //at basiloid
    	{
    		positionBottom(); //move to bottom
    		System.out.println("drop from basiloid to bottom: " + setPoint);
    	}
    	else if (condition == lift_Bottom) //on the bottom
    	{
    		System.out.println("Already at bottom: " + setPoint);
    	}
    	else if (condition == lift_StackRelease) //release the stack
    	{
    		positionBottom();
    		System.out.println("Moving the stack to the bottom: " + setPoint);
    	}
    }
    
    public void removeStack() //remove stack from the robot
    {
    	if (condition == lift_Bottom)
    	{
    		positionReleaseStack();
    		System.out.println("Moving from bottom to remove stack: " + setPoint);
    	}
    }
    public double liftEncoderValue() //get value of encoder
    {
    	return Robot.liftPID.liftEncoder.getDistance();
    }
    public double getCondition()
    {
    	return condition;
    }
    public double getLiftMotorPower()
    {
    	return liftMotor.get();
    }
    public boolean isFinished()
    {
    	if (Robot.liftPID.onTarget())
    	{
    		Robot.liftPID.disable();
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    public void setMotor(double speed)
    {
    	liftMotor.set(speed);
    }
    public void manualOverride()
    {
    	
    }
    public boolean limitBottom()
    {
    	return ! liftLimitBottom.get();
    }
    public boolean limitTop()
    {
    	return ! liftLimitTop.get();
    }
    /*
    public boolean limitSwitchOk(double power)
    {
    	if 
    }
    */
}
