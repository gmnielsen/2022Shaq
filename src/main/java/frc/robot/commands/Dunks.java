// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Dunks extends SequentialCommandGroup {
  /** Creates a new Dunks. */
  public Dunks(DriveSubsystem m_robotDrive, ShooterSubsystem m_shooter) {
    // Add your commands in the addCommands() call, e.g.
    addCommands(
      //shoot
      new RunCommand(() -> m_shooter.intakeOn(-ShooterConstants.kOutTakePower, false), m_shooter).withTimeout(0.1),
      new RunCommand(() -> m_shooter.intakeOn(0.0, true), m_shooter).withTimeout(0.1),
      new RunCommand(() -> m_robotDrive.simpleDrive(0.0), m_robotDrive).withTimeout(0.2),
      //reverse out of zone
      new RunCommand(() -> m_robotDrive.arcadeDrive(0.0, AutoConstants.kPower), m_robotDrive).withTimeout(1.0)
      );
  }
}