import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// 🏥 Main Class
public class HospitalGUI extends JFrame {
    private JComboBox<String> staffTypeBox;
    private JTextField nameField, idField, deptField, extraField;
    private JLabel extraLabel;
    private JButton addButton, clearButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<Staff> staffList = new ArrayList<>();

    public HospitalGUI() {
        setTitle("🏥 Hospital Staff Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // 🔹 Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Staff"));
        inputPanel.setBackground(new Color(240, 248, 255));

        staffTypeBox = new JComboBox<>(new String[]{"Doctor", "Nurse", "Administrator"});
        nameField = new JTextField();
        idField = new JTextField();
        deptField = new JTextField();
        extraField = new JTextField();
        extraLabel = new JLabel("Specialization:");

        inputPanel.add(new JLabel("Staff Type:"));
        inputPanel.add(staffTypeBox);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Department:"));
        inputPanel.add(deptField);
        inputPanel.add(extraLabel);
        inputPanel.add(extraField);

        addButton = new JButton("➕ Add Staff");
        clearButton = new JButton("🧹 Clear");

        inputPanel.add(addButton);
        inputPanel.add(clearButton);

        // 🔹 Table Section
        String[] columnNames = {"Name", "ID", "Department", "Type", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        // 🔹 Event Listeners
        staffTypeBox.addActionListener(e -> updateExtraField());
        addButton.addActionListener(e -> addStaff());
        clearButton.addActionListener(e -> clearFields());

        // 🔹 Add Components
        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    // 🔧 Update extra label dynamically
    private void updateExtraField() {
        String type = (String) staffTypeBox.getSelectedItem();
        if (type.equals("Doctor")) extraLabel.setText("Specialization:");
        else if (type.equals("Nurse")) extraLabel.setText("Shift (1/2):");
        else extraLabel.setText("Role:");
    }

    // ➕ Add staff entry
    private void addStaff() {
        try {
            String name = nameField.getText().trim();
            int id = Integer.parseInt(idField.getText().trim());
            String department = deptField.getText().trim();
            String type = (String) staffTypeBox.getSelectedItem();
            String extra = extraField.getText().trim();

            if (name.isEmpty() || department.isEmpty() || extra.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Staff s;
            if (type.equals("Doctor")) {
                s = new Doctor(name, id, department, extra);
            } else if (type.equals("Nurse")) {
                int shift = Integer.parseInt(extra);
                s = new Nurse(name, id, department, shift);
            } else {
                s = new Administrator(name, id, department, extra);
            }

            staffList.add(s);
            tableModel.addRow(s.getInfo());
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for ID or shift!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 🧹 Clear input fields
    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        deptField.setText("");
        extraField.setText("");
    }

    // 🚀 Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalGUI().setVisible(true));
    }
}

// 🧩 Parent Class
class Staff {
    protected String name;
    protected int id;
    protected String department;

    public Staff(String name, int id, String department) {
        this.name = name;
        this.id = id;
        this.department = department;
    }

    public String[] getInfo() {
        return new String[]{name, String.valueOf(id), department, "Staff", "-"};
    }

    public String getType() {
        return "Staff";
    }
}

// 👨‍⚕️ Doctor Class
class Doctor extends Staff {
    private String specialization;

    public Doctor(String name, int id, String department, String specialization) {
        super(name, id, department);
        this.specialization = specialization;
    }

    @Override
    public String[] getInfo() {
        return new String[]{name, String.valueOf(id), department, "Doctor", specialization};
    }
}

// 👩‍⚕️ Nurse Class
class Nurse extends Staff {
    private int shift;

    public Nurse(String name, int id, String department, int shift) {
        super(name, id, department);
        this.shift = shift;
    }

    @Override
    public String[] getInfo() {
        return new String[]{name, String.valueOf(id), department, "Nurse", "Shift " + shift};
    }
}

// 🧑‍💼 Administrator Class
class Administrator extends Staff {
    private String role;

    public Administrator(String name, int id, String department, String role) {
        super(name, id, department);
        this.role = role;
    }

    @Override
    public String[] getInfo() {
        return new String[]{name, String.valueOf(id), department, "Administrator", role};
    }
}
