// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.




package frc.robot;


import edu.wpi.first.wpilibj.XboxController;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    // on / off switches
    public static boolean currentIntakeState = false;
    public static boolean cameraState = false;
    public static boolean armUp = true;
    public static boolean winchState = false;
	public static boolean powerState = false;
    public static boolean driveState = false;

/*

// everybot constants
    public static final double armHoldUp = 0.08;
    public static final double armHoldDown = 0.13;
    public static final double armTravel = 0.5;
  
    public static final double armTimeUp = 0.5;
    public static final double armTimeDown = 0.35;
  
    //Varibles needed for the code
    public static boolean burstMode = false;
    public static double lastBurstTime = 0;
  
    public static double autoStart = 0;
    public static boolean goForAuto = false;
  
    */
	
    // XBox mappings
    /*  kBumperLeft(5)  --> arm down
        kBumperRight(6) --> arm up
        kStickLeft(9)   --> conveyor empty
        kStickRight(10) --> 
        kA(1)           --> intake on
        kB(2)           --> intake reverse
        kX(3)           --> disable climbarm
        kY(4)           --> switch camera
        kBack(7)        --> 
        kStart(8)       --> half speed drive
    */
    public static int kIntakeButton = XboxController.Button.kA.value;   
    public static int kIntakeReverseButton = XboxController.Button.kB.value;   
    public static int kInvertDrive = XboxController.Button.kX.value;
//    public static int kSlowDown = XboxController.Button.kY.value;   
    public static int kArmPulseDown = XboxController.Button.kStart.value;
    public static int kArmUp = XboxController.Button.kRightBumper.value;
    public static int kArmDown = XboxController.Button.kLeftBumper.value;
	public static int kArmPulseUp = XboxController.Button.kBack.value;
    public static int kPrintPosition = XboxController.Button.kLeftStick.value;
    public static int kResetPosition = XboxController.Button.kY.value;
    

    
    public final class OIConstants{
        public static final int kDriverControllerPort = 0;
  
    }
    public final class AutoConstants {
        public static final double kTimeOut = 1.0;
		public static final double kPower = -0.8; //-0.8
    }
    public final class DriveConstants{
        public static final int kLeftMotor01CanBusID = 11;
        public static final int kLeftMotor02CanBusID = 12;
        public static final int kRightMotor03CanBusID = 13;
        public static final int kRightMotor04CanBusID = 14;
        public static final double kSlowMaxSpeed = 0.5;
		public static final double kMaxSpeed = 0.9; // was 0.6
        public static final double kRampRate = 0.65; // 0.25 in github
    }
    public final class ShooterConstants{        
        public static final int kArmMotor05CanBusID = 15;
        public static final double kMaxVenomPower = 2000.0;
        public static final int kShooterMotorPWM = 0;
        // kPID and B etc
        public static final double KpUp = 1.6;
        public static final double KdUp = 0.0;
        public static final double KpDown = 0.3;
        public static final double KdDown = 0.0;
        public static final double KBDown = 0.09;
        public static final double kIntakePower = 0.28;
        public static final double kOutTakePower = 0.7;
        /*
        
        public static final double kConveyorPulsePower = -0.4;
		public static final double kConveyorFullPower = -0.6; //-0.6
		public static final double kConveyorFiveBallEmpty = 2.0;
        public static final double kConveyorOneBallPulse = 0.13;
		public static final double kUpperLowerRatio = 1.5; // original 1.0, changed to put top spin on ball
        public static final double kIntakeTime = 2.0;
        public static final double kUpperTime = 0.15;
        
        public static final double kUpperPower = -0.6;
        public static final double kLowerPower = -0.6; // -0.4 upped for arm passage
		*/
    }

}
