// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.PathGenerator;
import frc.robot.commands.TeleOpControl;
import frc.robot.subsystems.SimulationChassis;

import java.io.IOException;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{
    private final Joystick joystick;
    private final SimulationChassis chassisSim;
    public SendableChooser<Integer> pathGeneration;

    public RobotContainer()
    {
        joystick = new Joystick(0);
        chassisSim = new SimulationChassis();
        SendableChooser<Boolean> isFieldOriented = new SendableChooser<>();
        isFieldOriented.setDefaultOption("Field Oriented", true);
        isFieldOriented.addOption("Robot Oriented", false);
        SmartDashboard.putData("Control Mode", isFieldOriented);

        SendableChooser<Integer> pathGeneration = new SendableChooser<>();
        pathGeneration.setDefaultOption("Pathweaver", 0);
        pathGeneration.addOption("Spline Point Generation", 1);
        pathGeneration.addOption("Straight Point Generation",2);
        SmartDashboard.putData("Path Generation", pathGeneration);
        this.pathGeneration = pathGeneration;


        chassisSim.setDefaultCommand(new TeleOpControl(
                chassisSim,
                () -> joystick.getRawAxis(1),
                () -> -joystick.getRawAxis(0),
                () -> joystick.getRawAxis(2),
                isFieldOriented::getSelected
        ));
        configureBindings();
    }


    /** Use this method to define your trigger->command mappings. */
    private void configureBindings()
    {

    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() throws IOException {
        if(pathGeneration.getSelected() == 0){
            return PathGenerator.fromPathweaverJSON(chassisSim, "paths/examplePath.wpilib.json");

        } else if(pathGeneration.getSelected() == 1) {
            return PathGenerator.fromSplinePoints(chassisSim,
                    new Pose2d(new Translation2d(3, 1),Rotation2d.fromDegrees(25)),
                    new Pose2d(new Translation2d(6, 1),Rotation2d.fromDegrees(25)),
                    new Pose2d(new Translation2d(8, 6),Rotation2d.fromDegrees(25)));
        }else{
            return PathGenerator.fromStraightPoints(chassisSim,
                    new Pose2d(new Translation2d(3, 1),Rotation2d.fromDegrees(25)),
                    new Pose2d(new Translation2d(6, 1),Rotation2d.fromDegrees(25)),
                    new Pose2d(new Translation2d(8, 6),Rotation2d.fromDegrees(25)));
        }
    }
}

