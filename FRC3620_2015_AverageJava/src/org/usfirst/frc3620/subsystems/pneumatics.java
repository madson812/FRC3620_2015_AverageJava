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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.usfirst.frc3620.Robot;
import org.usfirst.frc3620.RobotMap;
import org.usfirst.frc3620.commands.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class pneumatics extends Subsystem {
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    Solenoid valveArm2_1 = RobotMap.pneumaticsvalveArm2_1;
    Solenoid valveArm1_1 = RobotMap.pneumaticsvalveArm1_1;
    Solenoid valveArm1_2 = RobotMap.pneumaticsvalveArm1_2;
    Solenoid valveArm2_2 = RobotMap.pneumaticsvalveArm2_2;
    Compressor compressor1 = RobotMap.pneumaticsCompressor1;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	public boolean havePneumatics;
	public boolean basiloidIn = true;
	public boolean intakeOpen= false;
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	
	
	public void intakeOpen()
	{
		valveArm1_1.set(false);
		//valveArm1_2.set(true);
		valveArm2_2.set(true);
		// valveArm2_1.set(false);
		intakeOpen = true;
	}
	
	public void intakeClose()
	{
		valveArm1_1.set(true);
		//valveArm1_2.set(false);
		valveArm2_2.set(false);
		// valveArm2_1.set(true);
		intakeOpen = false;
	}
	
	public void openAndClose()
	{
		if(intakeOpen == true)
		{
			valveArm1_1.set(true);
			//valveArm1_2.set(false);
			valveArm2_2.set(false);
			// valveArm2_1.set(true);
			intakeOpen = false;
		}
		else
		{
			valveArm1_1.set(false);
			//valveArm1_2.set(true);
			valveArm2_2.set(true);
			// valveArm2_1.set(false);
			intakeOpen = true;
		}
		
	}
	
	public boolean isBasiloidIn(){
		return basiloidIn;
	}
	
	/*
	public void pushOut() {
		valve1_1.set(true);
		valve1_2.set(false);
		valve2_1.set(true);
		valve2_2.set(false);
	}

	public void pushIn() {
		valve1_1.set(false);
		valve1_2.set(true);
		valve2_1.set(false);
		valve2_2.set(true);
	}
	**/
	String macString;
	public void checkForPneumatics() {
		try {

			for (Enumeration<NetworkInterface> e = NetworkInterface
					.getNetworkInterfaces(); e.hasMoreElements();) {
				NetworkInterface network = e.nextElement();
				System.out.println("network " + network);
				byte[] mac = network.getHardwareAddress();
				if (mac != null) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i],
								(i < mac.length - 1) ? "-" : ""));
					}
					//Mule Board (wegscheid) MAC: "00-80-2F-17-EA-A4"
					//						 MAC: "00-80-2F-17-EA-A5"
					//Test bot (3630KOP) MAC: "00-80-2F-17-93-IE"
					//Prototype (3620 Spare) MAC: "00-80-2F-17-EB-09"
					//						 MAC: "00-80-2F-17-EB-08"
					macString = sb.toString();
					System.out.println("Current MAC address: " + macString);
					if (macString.equals("00-80-2F-17-EB-09")
							|| macString.equals("00-80-2F-17-EB-08")) {
						havePneumatics = true;
						break;
					}
				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}

	}
}
