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

import org.usfirst.frc3620.RobotMap;
import org.usfirst.frc3620.commands.*;

import edu.wpi.first.wpilibj.CounterBase.EncodingType; import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class encoderSubsystem extends Subsystem {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    Encoder leftEncoder = RobotMap.encoderSubsystemleftEncoder;
    Encoder rightEncoder = RobotMap.encoderSubsystemrightEncoder;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public double leftEncoderValue = 0;
    public double getLeftEncoder()
    {
    	leftEncoderValue = leftEncoder.getDistance();
    	return leftEncoderValue;
    }
    
    public double rightEncoderValue = 0;
    public double getRightEncoder()
    {
    	rightEncoderValue = rightEncoder.getDistance();
    	return rightEncoderValue;
    }
    
    public void resetEncoders()
    {
    	leftEncoder.reset();
    	rightEncoder.reset();
    }
    
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
    	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
