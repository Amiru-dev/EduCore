package lk.educore.schoolms.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Student Information Dialog
 * Dialog wrapper for StudentInformation panel
 */
public class StudentInformationDialog extends JDialog {

    public StudentInformationDialog(Frame parent, String studentId) {
        super(parent, "Student Information", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        add(new StudentInformation(studentId));
    }
}