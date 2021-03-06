// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.




package frc.robot.subsystems;

import java.util.Map;

import com.playingwithfusion.CANVenom;
import com.playingwithfusion.CANVenom.BrakeCoastMode;
import com.playingwithfusion.CANVenom.ControlMode;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;

/**
 * Add your docs here.
 */
public class ShooterSubsystem extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private final Spark m_intakeSpark = new Spark(ShooterConstants.kShooterMotorPWM);
  private final CANVenom m_arm = new CANVenom(ShooterConstants.kArmMotor05CanBusID);
  //private boolean ARMUP = false;

  private final ShuffleboardTab sbConfig = Shuffleboard.getTab("Config");
  public final NetworkTableEntry sbPos = sbConfig.add("pos", m_arm.getPosition())
    .withPosition(0, 0).getEntry();
  public final NetworkTableEntry sbKp = sbConfig.add("kP Up", Constants.ShooterConstants.KpUp)
    .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0.0, "Max", 2.0))
    .withPosition(0, 1).getEntry();
  public final NetworkTableEntry sbKd = sbConfig.add("kD Up", Constants.ShooterConstants.KdUp)
    .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0.0, "max", 2.0))
    .withPosition(2, 1).getEntry();
  public final NetworkTableEntry sbKpDown = sbConfig.add("kP Down", Constants.ShooterConstants.KpDown)
    .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0.0, "max", 2.0))
    .withPosition(0, 2).getEntry();
  public final NetworkTableEntry sbKdDown = sbConfig.add("kD Down", Constants.ShooterConstants.KdDown)
    .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0.0, "max", 2.0))
    .withPosition(2, 2).getEntry();
  public final NetworkTableEntry sbBDown = sbConfig.add("B Down", Constants.ShooterConstants.KBDown)
    .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0.0, "max", 1.0))
    .withPosition(4, 2).getEntry();


  public ShooterSubsystem() {
    // initialization methods here
    m_arm.setBrakeCoastMode(BrakeCoastMode.Brake);
    m_arm.resetPosition();
    m_arm.setControlMode(ControlMode.Disabled);
    m_arm.setMaxSpeed(Constants.ShooterConstants.kMaxVenomPower);
  }

  // on/off switch for the intake
  public void intakeOn(double power, boolean state){
    if (!state) {
      m_intakeSpark.set(power);}
    else m_intakeSpark.set(0.0);
    Constants.currentIntakeState = !state;
  }
  // set arm position command
  public void setPositionRaise(double pos) {
    armTestRaise();
    m_arm.setCommand(ControlMode.PositionControl, pos);
  }
  public void setPositionLower(double pos){
    // test for pos > -2, yes call reset
    if(m_arm.getPosition() > -2){
      m_arm.resetPosition();
    }
    armTestLower();
    m_arm.setCommand(ControlMode.PositionControl, pos);
  }
  // print out
  public void getPositionConsole() {
    System.out.println(m_arm.getPosition());
    System.out.println(m_arm.getMaxAcceleration());
    System.out.println(m_arm.getMaxSpeed());
    System.out.println(m_arm.getBrakeCoastMode());
    
  }

  // shuffleboard display components
  // return current position
  public double getPosition(){
    return m_arm.getPosition();
  }
  public double getCurrentP(){
    return m_arm.getKP();
  }
  public double getCurrentI(){
    return m_arm.getKI();
  }
  public double getCurrentD(){
    return m_arm.getKD();
  }
  public double getCurrentSetPosition(){
    return m_arm.getMotionProfilePositionTarget();
  }


  // reset arm encoder position to 0.0
  public void resetPosition(double pos) {
    m_arm.setControlMode(ControlMode.Disabled);
    m_arm.resetPosition();
    m_arm.setCommand(ControlMode.Proportional, 0.0);
   // ARMUP = false;
  }
 
  // powers arm full
  public void armRaiseFull(double power){
    m_arm.set(testFullArmPower(power));
    m_arm.setBrakeCoastMode(BrakeCoastMode.Brake);
  }
  // power arm pulse
  public void armRaisePulse(double power){
    m_arm.set(testPulseArmPower(power));
    m_arm.setBrakeCoastMode(BrakeCoastMode.Brake);
  }

  // testing
  public void armTestRaise(){
    //m_arm.
    //m_arm.setSafetyEnabled(false);
    //m_arm.setExpiration(2.0);
    m_arm.setPID(sbKp.getDouble(1.6), 0, sbKd.getDouble(0.01), 0.184, 0.0); //(1.6, 0,0.01, 0.184, 0)

    // drn -- change control mode from position to proportional and no power
    // m_arm.setCommand(ControlMode.Proportional, 0.0);
    //m_arm.setMaxSpeed(0.0);
    //m_arm.setMaxAcceleration(20000.0);
    //m_arm.clearMotionProfilePoints();
    //System.out.println(m_arm.getPosition());
  }
  public void armTestLower(){
    m_arm.setPID(sbKpDown.getDouble(0.2), 0.0, sbKdDown.getDouble(0.0), 0.184, sbBDown.getDouble(0.088)); //(0.2, 0.0, 0.0, 0.184, 0.088) B needs to be smaller than P
   System.out.println(m_arm.getKD());

  }

  //power lift safety (full)
  public double testFullArmPower(double power){

      if(power == 0.0){
        return (0.0);
      }
      if((power > 0.0) && (m_arm.getPosition() < -19)){
        return (power);
      }
      if((power < 0.0) && (m_arm.getPosition() > -1)){
        return (power);
      }

      return 0.0;
  }
  // position limiter and safety
  public double testArmPosition(double pos){
    if(m_arm.getPosition() < -19){
      return (pos);
    }
    if(m_arm.getPosition() > -1){
      return (pos);
    }

    return pos;
  }
  //power lift safety (pulse)
  public double testPulseArmPower(double power){

    if(power == 0.0){
      return (0.0);
    }
    if((power > 0.0) && (m_arm.getPosition() < 0)){
      return (power);
    }
    if((power < 0.0) && (m_arm.getPosition() > -20)){
      return (power);
    }

    return 0.0;
}


  @Override
  public void periodic() {
    //if (ARMUP) m_arm.set(-5.0);
    // This method will be called once per scheduler run
    if(m_arm.getPosition() > 0.0){
      
      m_arm.setCommand(ControlMode.Proportional, 0.0);
     
      //System.out.println(m_arm.getPosition());
      }

    if(m_arm.getPosition() < -18.0){
     
      m_arm.setControlMode(ControlMode.Disabled);
     
    }
    
    if(m_arm.getPosition() < -14.0){
      
      m_arm.setB(0.0);
      
     // System.out.println(m_arm.getB());
    }
    SmartDashboard.putNumber("pos", m_arm.getPosition());
  }
}
