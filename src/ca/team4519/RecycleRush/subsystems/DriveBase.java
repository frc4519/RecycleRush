package ca.team4519.RecycleRush.subsystems;

import java.util.Hashtable;

import ca.team4519.lib.Loopable;
import ca.team4519.lib.Subsystem;
import ca.team4519.lib.MechaGyro;
import ca.team4519.lib.RioAcceleromiter;

//import ca.team4519.lib.pid.PIDDrive;
//import ca.team4519.lib.pid.PIDSource;
import ca.team4519.RecycleRush.Constants;
import ca.team4519.RecycleRush.MechaRobot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DriveBase extends Subsystem implements Loopable {
	
		double strafeOut = 0;
		double turningOut = 0;
	double drivingOut = 0;
	
		//THIS NEEDS TO BE TWEAKED AND ADJUSTED AND ALL SORTS OF BUM
		//^rt
	  	public final double RIGHT_ENCOCDER_TO_DISTANCE_RATIO = (Math.PI*6)/2176 /*(Constants.wheelSize.getDouble() * Math.PI) / (13.0)*/;
	  	public final double LEFT_ENCOCDER_TO_DISTANCE_RATIO = (Math.PI*6)/2176 /*(Constants.wheelSize.getDouble() * Math.PI) / (13.0)*/;
	  
	  	//Speed Controllers
	  	public Talon leftDriveA = new Talon(Constants.leftDriveA.getInt());
	  	public Talon leftDriveB = new Talon(Constants.leftDriveB.getInt());
	  	public Talon rightDriveA = new Talon(Constants.rightDriveA.getInt());
	  	public Talon rightDriveB = new Talon(Constants.rightDriveB.getInt());
	  	public Talon strafeA = new Talon(Constants.strafeA.getInt());
	  	public Joystick gamepad = new Joystick(0);
	  
	  	//Encoders
	  	public Encoder leftEncoder = new Encoder(Constants.leftDriveEncoderCHAN_A.getInt(), Constants.leftDriveEncoderCHAN_B.getInt(), false);
	  	public Encoder rightEncoder = new Encoder(Constants.rightDriveEncoderCHAN_A.getInt(), Constants.rightDriveEncoderCHAN_B.getInt(), true);
	  
		PIDController leftPid = new PIDController(0.0, 0.0, 0, MechaRobot.driveBase.leftEncoder, MechaRobot.driveBase.leftDriveA);
		PIDController rightPid = new PIDController(0.0, 0.0, 0, MechaRobot.driveBase.rightEncoder, MechaRobot.driveBase.rightDriveA);
	  	
	  	public MechaGyro gyro = new MechaGyro(Constants.gyro.getInt());
	  	
	  	//Solenoids 
	  	private Solenoid shift = new Solenoid(Constants.chassisShift.getInt());

	  	
	  	public RioAcceleromiter accelerometer = new RioAcceleromiter();
	  	
	    public void setLeftRightStrafePower(double forwardAxis, double turningAxis, double strafeAxis) {
	    	
	    	
	    	
	    	if(0.09 > forwardAxis && forwardAxis > -0.09) forwardAxis= 0;
	    	if(0.09 > turningAxis && turningAxis> -0.09) turningAxis = 0;
	    	if(0.09 > strafeAxis && strafeAxis > -0.09) strafeAxis = 0;
	    	
	    	if(strafeAxis > strafeOut) {
	    		strafeOut += Constants.rampingConstant.getDouble();
	    	}else if(strafeAxis < strafeOut) {
	    		strafeOut -= Constants.rampingConstant.getDouble();
	    	}
	    			
	    	if(turningAxis > turningOut) {
	    		turningOut += Constants.rampingConstant.getDouble();
	    	}else if(turningAxis < turningOut) {
	    		turningOut -= Constants.rampingConstant.getDouble();
	    	}
	    	
	    	if(forwardAxis > drivingOut) {
	    		drivingOut += Constants.rampingConstant.getDouble();
	    	}else if(forwardAxis < drivingOut) {
	    		drivingOut -= Constants.rampingConstant.getDouble();
	    	}
	    		
	    	leftDriveA.set(-drivingOut + turningOut);
	    	leftDriveB.set(-drivingOut + turningOut);
	    	rightDriveA.set(drivingOut + turningOut);
	    	rightDriveB.set(drivingOut + turningOut);
	 
	    	strafeA.set(-strafeOut);
	    }

	    public void fancyDrive(double forwardAxis, double rotationAxis, double strafeAxis, double secondStrafe, double gyro, float Kp) {
	    
	    	
	    	if(0.09 > forwardAxis && forwardAxis > -0.09) forwardAxis= 0;
	    	if(0.09 > rotationAxis && rotationAxis> -0.09) rotationAxis = 0;
	    	if(0.125 > strafeAxis && strafeAxis > -0.125) strafeAxis = 0;
	    	if(0.1 > secondStrafe && secondStrafe > -0.1) secondStrafe = 0;
	    	
	    	if(strafeAxis > strafeOut || secondStrafe > strafeOut) {
	    		if((secondStrafe != 0 && strafeAxis == 0) && ( rotationAxis == 0 && forwardAxis == 0)) {
	    			strafeOut += (Constants.rampingConstant.getDouble() + (gyro* Kp));	
	    		}else{
	    		strafeOut += Constants.rampingConstant.getDouble();
	    		}
	    	}else if(strafeAxis < strafeOut || secondStrafe > strafeOut) {
	    		strafeOut -= Constants.rampingConstant.getDouble();
	    	}
	    			
	    	if(rotationAxis > turningOut) {
	    		turningOut += Constants.rampingConstant.getDouble();
	    	}else if(rotationAxis < turningOut) {
	    		turningOut -= Constants.rampingConstant.getDouble();
	    	}
	    	
	    	if(forwardAxis > drivingOut) {
	    		drivingOut += Constants.rampingConstant.getDouble();
	    	}else if(forwardAxis < drivingOut) {
	    		drivingOut -= Constants.rampingConstant.getDouble();
	    	}
	    		    		
	    	leftDriveA.set(-drivingOut + turningOut);
	    	leftDriveB.set(-drivingOut + turningOut);
	    	rightDriveA.set(drivingOut + turningOut);
	    	rightDriveB.set(drivingOut + turningOut);
	 
	    	strafeA.set(-strafeOut);
	    	
	    }
	    
	    
	    public double forwardAxis() {
	    	return gamepad.getRawAxis(Constants.forwardAxis.getInt());
	    }
	    
	    public double turningAxis() {
	    	return gamepad.getRawAxis(Constants.turningAxis.getInt());
	    }
	    
	    public double strafeAxis() {
	    	return gamepad.getRawAxis(Constants.strafeAxis.getInt());
	    }
	    
	    public boolean getShift() {
	    	return gamepad.getRawButton(6);
	    }
	    
	    public void shiftGears(boolean state) {
	    	if(state){
	    		shift.set(true);
	    	}else{
	    		shift.set(false);
	    	}
	    }
	    
	    public DriveBase() {
	    	super("DriveBase");	
	    }

	
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public Hashtable serialize() {
		
	    Hashtable leftDrive = new Hashtable();
	    Hashtable rightDrive = new Hashtable();
	    Hashtable encoders = new Hashtable();

	    leftDrive.put("leftDriveA", new Double(leftDriveA.get()));
	    leftDrive.put("leftDriveB", new Double(leftDriveB.get()));

	    rightDrive.put("rightDriveA", new Double(rightDriveA.get()));
	    rightDrive.put("rightDriveB", new Double(rightDriveB.get()));

	    encoders.put("leftEncoder", new Double(leftEncoder.get()));
	    encoders.put("rightEncoder", new Double(rightEncoder.get()));    
	    data.put("leftDrive", leftDrive);
	    data.put("rightDrive", rightDrive);
	    data.put("encoders", encoders);
	    data.put("gyro", new Double(getGyroAngle()));
	    return data;
		}
	
	    public Encoder getLeftEncoder() {
		    return leftEncoder;
	   }

	   	public double getLeftEncoderDistance() { // in feet
		    return leftEncoder.get() * LEFT_ENCOCDER_TO_DISTANCE_RATIO;
	   }

	   	public double getLeftEncoderDistanceInMeters() {
		    return getLeftEncoderDistance() * 0.3048;
	   }

	   	public Encoder getRightEncoder() {
		    return rightEncoder;
	   }

	   	public double getRightEncoderDistance() {
		    return rightEncoder.get() * RIGHT_ENCOCDER_TO_DISTANCE_RATIO;
	   }

	   	public double getRightEncoderDistanceInMeters() {
		    return getRightEncoderDistance() * 0.3048;
	   }

	   	public double getGyroAngle(){
		      return gyro.getAngle();
	   }
		  
	   	public double getGyroRate(){
		      return gyro.getRate();
	   }  
		  
	   	public double nonContAngle(){
		   double angle = gyro.getAngle();
		   if(gyro.getAngle() > 0){
			   angle = gyro.getAngle()%360;
		          return angle;
		   }else if(gyro.getAngle() < 0){
			   angle = gyro.getAngle()%-360;
		          return angle;
		       }
		     return angle;
		  }
		  
	   	public double smartAngle(){ // swag420blazeit
		      double degRot = 0;
		        if(3 >= nonContAngle() && nonContAngle() <= -3){
		           degRot = 0;
		           return degRot;
		        }else{
		            degRot = nonContAngle();
		            return degRot;
		        }
	   	}
	   	
	   	public double XAcceleration() {
	   		return accelerometer.getX();
	   	}
	   	
	   	public double YAcceleration() {
	   		return accelerometer.getY();
	   	}
		  
	   	public void resetGyro(){
		      gyro.reset();
	   	}
		  
	   	public void configurePid(double distance, double percentTolerance) {
	   		
	   		double ticksL = distance * LEFT_ENCOCDER_TO_DISTANCE_RATIO;
	   		double ticksR = distance * RIGHT_ENCOCDER_TO_DISTANCE_RATIO;
	   		
	   		leftPid.setSetpoint(ticksL);
	   		leftPid.setPercentTolerance(percentTolerance);
	   		rightPid.setSetpoint(ticksR);
	   		rightPid.setPercentTolerance(percentTolerance);
	   	}
	   	
	   	public void pidGo() {
	   		leftPid.enable();
	   		rightPid.enable();
	   	}
	   	
	   	public void pidStop() {
	   		leftPid.disable();
	   		rightPid.disable();
	   	}
	   	
	   	public boolean pidArrived() {
	   		if(leftPid.onTarget() && rightPid.onTarget()){
	   			return true;
	   		}else{
	   		return false;
	   		}
	   	}
	   	
	   	public double getAverageDistance() {
		    return (getRightEncoderDistance() + getLeftEncoderDistance()) / 2.0;
	   	}
		  
	   	public void resetEncoders() {
		    leftEncoder.reset();
		    rightEncoder.reset();
	   	}
	   	
	   	public void resetPID() {
	   		leftPid.reset();
	   		rightPid.reset();
	   	}
	   	
	   	public void resetAll() {
	   		resetEncoders();
	   		resetGyro();
	   		resetPID();
	   	}
	   	
	   	public void update(){
	   		
	   	//	SmartDashboard.putint("Something?", 1);
		    SmartDashboard.putNumber("Left Drive Distance", getLeftEncoderDistance());
		    SmartDashboard.putNumber("Right Drive Distance", getRightEncoderDistance());
		    SmartDashboard.putNumber("Both Encoders, Average Distance", getAverageDistance());
		    SmartDashboard.putNumber("gyro", smartAngle());
		    super.update(); 
	   	}
}