// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.


package frc.robot;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.Dunks;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // drn -- robot's subsystems and commands are defined here...

  // drn -- drive & intake & arm subsystem declarations
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();

  private final ShooterSubsystem m_shooter = new ShooterSubsystem();

  // drn --- declaring an instance of the XBox controller
  public final XboxController m_xboxController = new XboxController(OIConstants.kDriverControllerPort);


  // drn -- A chooser for autonomous commands
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();

  // drn -- shuffleboard tabs
  private final ShuffleboardTab sbCamera = Shuffleboard.getTab("Camera");

  // drn -- 

  // Camera
  private UsbCamera camera01;
  private VideoSink videoServer;

  // drn -- while driving diagnostics
  public final PowerDistribution pdp = new PowerDistribution(0,ModuleType.kCTRE);
  // drn -- simple autonomous driving
  // use ()-> to specify the start and end commands
  private final Command m_simpleDriveForward = new RunCommand(() -> m_robotDrive.arcadeDrive(AutoConstants.kPower, 0.0),
      m_robotDrive).withTimeout(AutoConstants.kTimeOut);

  private final Command m_simpleDriveReverse = new StartEndCommand(() -> m_robotDrive.arcadeDrive(-AutoConstants.kPower, 0.0), () -> m_robotDrive.arcadeDrive(0.0, 0.0),
      m_robotDrive).withTimeout(AutoConstants.kTimeOut);

  private final Command m_simpleShoot = new RunCommand(() -> m_shooter.intakeOn(-ShooterConstants.kIntakePower, false)); //needs to be false
  
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    // prepare the camera
    cameraInit();

    // drive command to split-stick arcade drive
    // split stick is left and right sticks on the XBox
    m_robotDrive
        .setDefaultCommand(new RunCommand(() -> m_robotDrive.arcadeDrive(-m_xboxController.getRightY(),
            -m_xboxController.getLeftX()), m_robotDrive));


    // drn -- sets up the driver's station to have options for autonomous
    m_chooser.addOption("Forward Auto", m_simpleDriveForward);
    m_chooser.addOption("Reverse Auto", m_simpleDriveReverse);
    m_chooser.addOption("Shoot Auto", m_simpleShoot);
    m_chooser.addOption("Auto Dunks", new Dunks(m_robotDrive, m_shooter));
    m_chooser.setDefaultOption("Auto Dunks", new Dunks(m_robotDrive, m_shooter));
    sbCamera.add(m_chooser).withSize(2, 1).withPosition(0, 0);

    // drn -- put power onto shuffleboard
    sbCamera.add("PDP voltage", pdp.getVoltage())
      .withSize(1, 1).withPosition(0, 2);
    // drn -- put camera on shuffleboard
    sbCamera.add(camera01)
      .withSize(6, 4).withPosition(2, 0);

  } // end RobotContainer initialization methods

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // arm
    final JoystickButton armUp = new JoystickButton(m_xboxController, Constants.kArmUp);
    //armUp.whenPressed(()-> m_shooter.setPosition(0.0));
    armUp.whileHeld(() -> m_shooter.setPositionRaise(-2.0)); //whileheld
    final JoystickButton armDown = new JoystickButton(m_xboxController, Constants.kArmDown);
    armDown.whileHeld(() -> m_shooter.setPositionLower(-19.0));
    //armDown.whileHeld(new StartEndCommand (()-> m_shooter.armRaiseFull(-0.50),()->m_shooter.armRaiseFull(0.0),m_shooter).withTimeout(0.10));
    final JoystickButton armPulseUp = new JoystickButton(m_xboxController, Constants.kArmPulseUp);
    armPulseUp.whenPressed(new StartEndCommand (()-> m_shooter.armRaisePulse(0.1),()->m_shooter.armRaisePulse(0.0),m_shooter).withTimeout(0.05));
    final JoystickButton armPulseDown = new JoystickButton(m_xboxController, Constants.kArmPulseDown);
    armPulseDown.whileHeld(new StartEndCommand (()-> m_shooter.armRaisePulse(-1.0),()->m_shooter.armRaisePulse(0.0),m_shooter).withTimeout(0.05));

    
    final JoystickButton positionGet = new JoystickButton(m_xboxController, Constants.kPrintPosition);
    //positionGet.whenPressed(() -> sbUpdatePID());
    // drn -- changed to add them to shuffleboard as well. Original command below  
    positionGet.whenPressed(() -> m_shooter.getPositionConsole());
    
    final JoystickButton positionReset = new JoystickButton(m_xboxController, Constants.kResetPostion);
    positionReset.whenPressed(() -> m_shooter.resetPostion(0.0));

    // intake/outake
    final JoystickButton intakeOn = new JoystickButton(m_xboxController, Constants.kIntakeButton);
    intakeOn.whenPressed(() -> m_shooter.intakeOn(ShooterConstants.kIntakePower, Constants.currentIntakeState)); //need to test on/off and length of on time
    final  JoystickButton intakeReverse = new JoystickButton(m_xboxController, Constants.kIntakeReverseButton);
    intakeReverse.whenPressed(() -> m_shooter.intakeOn(-ShooterConstants.kOutTakePower, Constants.currentIntakeState));

    // speed/drive
    /* final JoystickButton slowDown = new JoystickButton(m_xboxController, Constants.kSlowDown);
    slowDown.whenPressed(() -> m_robotDrive.halfPower());
    */
    final JoystickButton invertDrive = new JoystickButton(m_xboxController, Constants.kInvertDrive);
    invertDrive.whenPressed(() -> m_robotDrive.invertDrive());

  } // end configureButtonBindins

  // Starting and adjusting camera
  private void cameraInit() {
    camera01 = CameraServer.startAutomaticCapture(0);
    videoServer = CameraServer.getServer();
    camera01.setResolution(320, 240);
    camera01.setFPS(15);
    videoServer.setSource(camera01);
  } // end cameraInit

  /*
  // toggle switch for changing cameras
  private void cameraSwitch() {
    if (Constants.cameraState) {
      videoServer.setSource(camera01);
    } else {
      videoServer.setSource(camera02);
    }
    Constants.cameraState = !Constants.cameraState;
  } // end cameraSwitch
*/
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_chooser.getSelected();
  } // end getAutonomous

} // end RobotContainer
