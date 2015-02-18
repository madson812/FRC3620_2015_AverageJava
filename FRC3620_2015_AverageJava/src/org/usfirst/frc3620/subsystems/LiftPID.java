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
    DigitalInput liftLimitBottom = RobotMap.liftPIDliftLimitBottom;
    DigitalInput liftLimitTop = RobotMap.liftPIDliftLimitTop;
    Encoder liftEncoder = RobotMap.liftPIDLiftEncoder;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final double bottom = 0;
    private final double basiloid = 12.0;
    private final double top = 24;
    private final double releaseStack = 5.0;
    public double setPoint = 0;
    static final int lift_Bottom = 0;
    static final int lift_Basiloid = 1;
    static final int lift_Top = 2;
    static final int lift_StackRelease = 3;
    static final int lift_TopMid = 4;
    static final int lift_BottomMid = 5;
    static final int lift_MiddleMid = 6;
    static final int lift_AboveTop = 7;
    // Initialize your subsystem here
    public LiftPID() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        super("LiftPID", -1.0, 0.0, 0.0);
        setAbsoluteTolerance(0.2);
        getPIDController().setContinuous(false);
        LiveWindow.addActuator("LiftPID", "PIDSubsystem Controller", getPIDController());
        getPIDController().setOutputRange(-0.7, 0.3);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        
        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
        setSetpoint(0);

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
    	if (isOkToMoveLift(output) && !getManualStop())
    	{
    		//System.out.println("Noting wrong");
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        liftMotor.pidWrite(output);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
    	}
    	else if (getManualStop())
    	{
    		liftMotor.set(0);
    		//System.out.println("Manual override of PID");
    	}
    	else
    	{
    		liftMotor.set(0);
    		//System.out.println("Limit switch not ok");
    	}
        //System.out.printf("sensor = %f, setPoint = %f, output = %f\n", returnPIDInput(), getPIDController().getSetpoint(), output);
    }
    public void positionBottom()
    {
    	positionToX(bottom);
    	getCondition();
    }
    public void positionBasiloid()
    {
    	positionToX(basiloid);
    	getCondition();
    }
    public void positionTop()
    {
    	positionToX(top);
    	getCondition();
    }
    public void positionToX (double x) {
    	enable();
    	setSetpoint(x);
    	getCondition();
    }
    public void positionReleaseStack()
    {
    	positionToX(releaseStack);
    	getCondition();
    }
    public void liftUp()
    {
    	if (getCondition() == lift_Bottom) //bottom
    	{
    		positionBasiloid(); //move to basiloid
    		System.out.println("lift from bottom to basiloid: " + setPoint);
    	}
    	else if (getCondition() == lift_Basiloid) //at basiloid
    	{
    		positionTop();
    		System.out.println("lift from basiloid to top: " + setPoint);
    	}
    	else if (getCondition() == lift_Top) //currently user controlled
    	{
    		System.out.println("Already at top!! Cannot move up!!");
    	}
    	else if (getCondition() == lift_StackRelease) //remove stack position
    	{
    		positionBasiloid(); //move to basiloid
    		System.out.println("Moving from remove to Basiloid: " + setPoint);
    	}
    	else if (getCondition() == lift_TopMid)
    	{
    		positionTop();
    	}
    	else if (getCondition() == lift_BottomMid)
    	{
    		positionBasiloid();
    	}
    	else if (getCondition() == lift_MiddleMid)
    	{
    		positionBasiloid();
    	}
    	else if (getCondition() == lift_AboveTop)
    	{
    		System.out.println("Above top, cannot move with PID");
    	}
    }
    
    public void liftDown()
    {
    	if (getCondition() == lift_Top) //user controlled
    	{
    		positionBasiloid(); //move to basiloid
    		System.out.println("drop from top to basiloid: "  + setPoint);
    	}
    	else if (getCondition() == lift_Basiloid) //at basiloid
    	{
    		positionBottom(); //move to bottom
    		System.out.println("drop from basiloid to bottom: " + setPoint);
    	}
    	else if (getCondition() == lift_Bottom) //on the bottom
    	{
    		System.out.println("Already at bottom: " + setPoint);
    	}
    	else if (getCondition() == lift_StackRelease) //release the stack
    	{
    		positionBottom();
    		System.out.println("Moving the stack to the bottom: " + setPoint);
    	}
    	else if (getCondition() == lift_TopMid)
    	{
    		positionBasiloid();
    	}
    	else if (getCondition() == lift_BottomMid)
    	{
    		positionBottom();
    	}
    	else if (getCondition() == lift_MiddleMid)
    	{
    		positionBottom();
    	}
    	else if (getCondition() == lift_AboveTop)
    	{
    		positionTop();
    	}
    }
    
    public void removeStack() //remove stack from the robot
    {
    	if (getCondition() == lift_Bottom)
    	{
    		positionReleaseStack();
    		System.out.println("Moving from bottom to remove stack: " + setPoint);
    	}
    }
    public double liftEncoderValue() //get value of encoder
    {
    	return Robot.liftPID.liftEncoder.getDistance();
    }

    public double getLiftMotorPower()
    {
    	return liftMotor.get();
    }
    
    public boolean getManualStop()
    {
    	if (getManual() < 0.2 && getManual() > -0.2)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
    public boolean isFinished()
    {
    	if (Robot.liftPID.onTarget() || getManualStop())
    	{
    		System.out.println("finished");
    		return true;
    	}
    	else
    	{
    		//System.out.println("not finished");
    		return false;
    	}
    }
    public void setMotor(double speed)
    {
    	liftMotor.set(speed);
    }
    public void resetLiftEncoder()
    {
    	liftEncoder.reset();
    }
    public void manualOverride()
    {
    	double speed = Robot.oi.operatorJoystick.getRawAxis(5);
    	if (isOkToMoveLift(speed) && (speed < -0.2 || speed > 0.2))
    	{
    		liftMotor.set(speed);
    	}
    	else
    	{
    		liftMotor.set(0);
    	}
    }
    public boolean limitBottom()
    {
    	return ! liftLimitBottom.get();
    }
    public boolean limitTop()
    {
    	return ! liftLimitTop.get();
    }
    
    public boolean isOkToMoveLift(double power)
    {
    	// System.out.println("isBasiloidIn: " + Robot.pneumatics.isBasiloidIn());
    	if (15 <= liftEncoder.getDistance() && liftEncoder.getDistance() <= 33 && ! Robot.pneumatics.isBasiloidIn())
    	{
    		return false;
    	}
    	else if (power < 0 && limitTop())
    	{
    		return false;
    	}
    	else if (power > 0 && limitBottom())
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
    
    
    public double getManual()
    {
    	if (Robot.oi != null && Robot.oi.operatorJoystick != null)
    	{
    		return Robot.oi.operatorJoystick.getRawAxis(5);
    	}
    	else
    	{
    		return 0;
    	}
    }
    
    public int getCondition()
    {
    	if (-.5 < liftEncoder.getDistance() && liftEncoder.getDistance() < 1)
    	{
    		return lift_Bottom;
    	}
    	else if (liftEncoder.getDistance() <= -0.5)
    	{
    		return lift_Bottom;
    	}
    	else if (4.5 < liftEncoder.getDistance() && liftEncoder.getDistance() < 5.5)
    	{
    		return lift_StackRelease;
    	}
    	else if (11.5 < liftEncoder.getDistance() && liftEncoder.getDistance() < 12.5)
    	{
    		return lift_Basiloid;
    	}
    	else if (23.5 < liftEncoder.getDistance() && liftEncoder.getDistance() < 24.5)
    	{
    		return lift_Top;
    	}
    	else if (1<= liftEncoder.getDistance() && liftEncoder.getDistance() <= 4.5)
    	{
    		return lift_BottomMid;
    	}
    	else if (5.5 <= liftEncoder.getDistance() && liftEncoder.getDistance() <= 11.5)
    	{
    		return lift_MiddleMid;
    	}
    	else if (12.5 <= liftEncoder.getDistance() && liftEncoder.getDistance() <= 23.5)
    	{
    		return lift_TopMid;
    	}
    	else
    	{
    		return lift_AboveTop;
    	}
    }
    public boolean safeToChangeBasiloid()
    {
    	if (liftEncoder.getDistance() >= 16 && liftEncoder.getDistance() <= 32)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
    
}
