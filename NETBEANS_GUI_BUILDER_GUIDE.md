# NetBeans GUI Builder Conversion Guide

## ⚠️ Important Note

**Do NOT manually create .form files or edit guarded blocks.** The NetBeans GUI Builder is a visual tool that must be used directly in the NetBeans IDE. Manually creating .form files or editing the code inside `//GEN-BEGIN:initComponents` and `//GEN-END:initComponents` will corrupt the form.

## ✅ Correct Approach: Use NetBeans GUI Builder Directly

### Step 1: Open Existing GUI File in NetBeans
1. Open NetBeans IDE
2. Open the EduCore project
3. Navigate to `src/lk/educore/schoolms/gui/`
4. Double-click any .java file (e.g., SignIn.java)

### Step 2: Switch to Design Tab
1. Click the **Design** tab at the top of the editor
2. If the file doesn't have a .form file, NetBeans will offer to create one
3. Accept the offer to create the .form file

### Step 3: Recreate the UI in GUI Builder
1. Use the **Palette** on the right to drag components onto the form
2. Set component names in the **Properties** window (right side)
3. Use the **Layout** toolbar to arrange components
4. Set text, fonts, sizes, and other properties in the Properties window

### Step 4: Add Event Handlers
1. Right-click a component (e.g., a button)
2. Select **Events** → **Action** → **actionPerformed**
3. NetBeans will create an event handler method
4. Copy your business logic from the backup file into this method

### Step 5: Copy Business Logic
1. Open the backup file from `C:\Users\ASUS\Documents\EduCore\backup_gui\`
2. Copy the business logic methods (database operations, validation, etc.)
3. Paste them into the new file OUTSIDE the guarded block
4. Call these methods from the event handlers created by GUI Builder

## 📋 Conversion Checklist for Each Screen

- [ ] Open the existing .java file in NetBeans
- [ ] Switch to Design tab and create .form file
- [ ] Recreate the UI layout using GUI Builder
- [ ] Set component names to match the original code
- [ ] Add event handlers for buttons and other interactive components
- [ ] Copy business logic from backup file
- [ ] Paste business logic outside guarded blocks
- [ ] Test the screen functionality
- [ ] Verify the Design tab works correctly

## 🎯 Recommended Order for Conversion

### Priority 1: Simple Screens (Practice)
1. **SettingsDialog.java** - Simple settings form
2. **AboutDialog.java** - Simple information dialog
3. **HelpDialog.java** - Simple help dialog

### Priority 2: Core Screens
4. **SignIn.java** - Login screen (already working, just needs Design tab)
5. **Dashboard.java** - Main dashboard with sidebar
6. **StudentDashboard.java** - Student-specific dashboard

### Priority 3: Management Screens
7. **StudentManagement.java** - Student CRUD
8. **TeacherManagement.java** - Teacher CRUD
9. **FeeManagement.java** - Fee management
10. **GuardianManagement.java** - Guardian CRUD

### Priority 4: Complex Screens
11. **StudentRegistration.java** - Registration with auto ID
12. **ExamManagement.java** - Exam scheduling
13. **TransportManagement.java** - Transport management
14. **FinanceManagement.java** - Finance management

### Priority 5: Dialogs and Panels
15. All remaining dialogs and panels

## 🔧 Key Tips

### Component Naming
- Keep component names the same as in the original code
- Example: `txtUsername`, `btnSignIn`, `cmbRole`
- This minimizes the need to rewrite business logic

### Layout Management
- Use `BorderLayout` for main containers
- Use `GridBagLayout` for forms with labels and fields
- Use `FlowLayout` for button panels
- Use `GridLayout` for grids of equal-sized components

### Event Handlers
- NetBeans creates event handler methods automatically
- Call your business logic methods from these handlers
- Example:
```java
private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {
    handleSignIn(); // Your business logic method
}
```

### Custom Components
- For custom components like `RoundPanel`, use a regular `JPanel` as placeholder
- Add the custom component later by editing the code outside guarded blocks
- Or recreate the custom component as a standard component

## 📁 Backup Location

All original GUI files are backed up at:
```
C:\Users\ASUS\Documents\EduCore\backup_gui\
```

Use these files to reference the original business logic when converting.

## ⏱️ Estimated Time

- **First screen**: 45-60 minutes (learning curve)
- **Next 3-5 screens**: 30-45 minutes each
- **Remaining screens**: 20-30 minutes each
- **Total**: 20-27 hours for all 36 screens

## 🚀 Quick Start

1. Open NetBeans IDE
2. Open EduCore project
3. Navigate to `src/lk/educore/schoolms/gui/`
4. Open `SettingsDialog.java`
5. Click the **Design** tab
6. Accept to create .form file
7. Recreate the UI using the Palette
8. Add event handlers
9. Copy business logic from backup
10. Test

## 🆘 Troubleshooting

### Form Won't Open in Design Tab
- Delete the .form file
- Open the .java file
- Click Design tab again
- Let NetBeans recreate the .form file

### Components Not Appearing
- Check component names match the code
- Verify components are added to the correct parent
- Check layout constraints

### Event Handlers Not Working
- Verify event handler method names match
- Check that business logic methods are called
- Ensure methods are outside guarded blocks

## 📚 Resources

- NetBeans GUI Builder Documentation: https://netbeans.apache.org/kb/docs/java/swing.html
- Form Guarded Block Error: http://wiki.netbeans.org/FormGuardedBlockError

---

**Remember**: The GUI Builder is a visual tool that must be used directly in NetBeans IDE. Do not manually create or edit .form files or guarded blocks.