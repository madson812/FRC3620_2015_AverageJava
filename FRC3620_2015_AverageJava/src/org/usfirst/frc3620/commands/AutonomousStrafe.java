// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3620.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc3620.Robot;

/**
 *
 */
public class  AutonomousStrafe extends Command {
	
	final double strafePower;
	final double strafeTime;
	private long timeStart;
    public AutonomousStrafe(double timeInSeconds, double power) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	strafePower = power;
    	strafeTime = timeInSeconds*1000;
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timeStart = System.currentTimeMillis();
    	Robot.drive.strafeDown();
    	System.out.println("auto strafe start");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drive.setStrafeMotor(strafePower);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return System.currentTimeMillis() - timeStart >= strafeTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.strafeUp();
    	System.out.println("autoStrafe end");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("auto straf interrupted");
    	end();
    }
}