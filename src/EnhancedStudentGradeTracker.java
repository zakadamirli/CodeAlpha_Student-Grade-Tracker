import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced Student Grade Tracker Application
 * A comprehensive grade management system with modern UI and advanced features
 * <p>
 * Features:
 * - Modern, responsive user interface
 * - Data persistence (save/load)
 * - Advanced filtering and search
 * - Grade statistics and analytics
 * - Export functionality
 * - Input validation and error handling
 *
 * @author Zeka Demirli
 * @version 2.0
 */
public class EnhancedStudentGradeTracker extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(255, 255, 255);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);

    private static final String[] COLUMN_NAMES = {"ID", "Student Name", "Grade", "Letter Grade", "Status"};
    private static final String DATA_FILE = "student_grades.dat";

    private final List<Student> student;
    private int nextStudentId;

    private JTextField nameField;
    private JSpinner gradeSpinner;
    private JTextField searchField;
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JLabel totalStudentsLabel;
    private JLabel averageGradeLabel;
    private JLabel highestGradeLabel;
    private JLabel lowestGradeLabel;
    private JProgressBar averageProgressBar;

    public EnhancedStudentGradeTracker() {
        student = new ArrayList<>();
        nextStudentId = 1;
        initializeComponents();
        setupLayout();
        attachEventListeners();
        loadData();
        updateStatistics();

        setTitle("Enhanced Student Grade Tracker v2.0");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                handleApplicationExit();
            }
        });
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        gradeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        gradeSpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) gradeSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);

        searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setToolTipText("Search by student name...");

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentsTable = new JTable(tableModel);
        studentsTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        studentsTable.setRowHeight(25);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsTable.setRowSorter(new TableRowSorter<>(tableModel));

        studentsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        studentsTable.getColumnModel().getColumn(2).setMaxWidth(80);
        studentsTable.getColumnModel().getColumn(3).setMaxWidth(100);
        studentsTable.getColumnModel().getColumn(4).setMaxWidth(100);

        totalStudentsLabel = createStatLabel("Total Students: 0");
        averageGradeLabel = createStatLabel("Average Grade: N/A");
        highestGradeLabel = createStatLabel("Highest Grade: N/A");
        lowestGradeLabel = createStatLabel("Lowest Grade: N/A");

        averageProgressBar = new JProgressBar(0, 100);
        averageProgressBar.setStringPainted(true);
        averageProgressBar.setForeground(PRIMARY_COLOR);
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(SECONDARY_COLOR);
        label.setBorder(new EmptyBorder(8, 12, 8, 12));
        return label;
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel statsPanel = createStatisticsPanel();
        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Add New Student"));
        panel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Grade (0-100):"), gbc);
        gbc.gridx = 3;
        formPanel.add(gradeSpinner, gbc);

        gbc.gridx = 4;
        JButton addButton = createStyledButton("Add Student", SUCCESS_COLOR);
        formPanel.add(addButton, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Student Records"));
        panel.setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JButton clearSearchButton = createStyledButton("Clear", WARNING_COLOR);
        searchPanel.add(clearSearchButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(Color.WHITE);

        JButton updateButton = createStyledButton("Update Selected", PRIMARY_COLOR);
        JButton deleteButton = createStyledButton("Delete Selected", DANGER_COLOR);
        JButton exportButton = createStyledButton("Export Data", new Color(142, 68, 173));

        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        actionPanel.add(exportButton);

        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Statistics Overview"));
        panel.setBackground(Color.WHITE);

        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 10, 10));
        statsGrid.setBackground(Color.WHITE);
        statsGrid.setBorder(new EmptyBorder(10, 10, 10, 10));

        statsGrid.add(totalStudentsLabel);
        statsGrid.add(averageGradeLabel);
        statsGrid.add(highestGradeLabel);
        statsGrid.add(lowestGradeLabel);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.add(new JLabel("Class Average Progress:"), BorderLayout.WEST);
        progressPanel.add(averageProgressBar, BorderLayout.CENTER);
        progressPanel.setBackground(Color.WHITE);
        statsGrid.add(progressPanel);

        panel.add(statsGrid, BorderLayout.CENTER);

        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(createMenuItem("Save Data", e -> saveData()));
        fileMenu.add(createMenuItem("Load Data", e -> loadData()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Export to CSV", e -> exportToCsv()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Exit", e -> handleApplicationExit()));

        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.add(createMenuItem("Clear All Data", e -> clearAllData()));
        toolsMenu.add(createMenuItem("Generate Sample Data", e -> generateSampleData()));

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(createMenuItem("About", e -> showAboutDialog()));

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void attachEventListeners() {
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addStudent();
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });

        findButtonByText("Add Student").addActionListener(e -> addStudent());
        findButtonByText("Clear").addActionListener(e -> {
            searchField.setText("");
            filterTable();
        });
        findButtonByText("Update Selected").addActionListener(e -> updateSelectedStudent());
        findButtonByText("Delete Selected").addActionListener(e -> deleteSelectedStudent());
        findButtonByText("Export Data").addActionListener(e -> exportToCsv());
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        int grade = (Integer) gradeSpinner.getValue();

        if (name.isEmpty()) {
            showErrorDialog("Please enter a student name.");
            nameField.requestFocus();
            return;
        }

        boolean nameExists = this.student.stream()
                .anyMatch(s -> s.getName().equalsIgnoreCase(name));

        if (nameExists) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "A student with this name already exists. Add anyway?",
                    "Duplicate Name",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        Student student = new Student(nextStudentId++, name, grade);
        this.student.add(student);
        addStudentToTable(student);
        updateStatistics();

        nameField.setText("");
        gradeSpinner.setValue(0);
        nameField.requestFocus();

        showSuccessMessage("Student added successfully!");
    }

    private void addStudentToTable(Student student) {
        Object[] rowData = {
                student.getId(),
                student.getName(),
                student.getGrade(),
                student.getLetterGrade(),
                student.getStatus()
        };
        tableModel.addRow(rowData);
    }

    private void updateSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorDialog("Please select a student to update.");
            return;
        }

        int modelRow = studentsTable.convertRowIndexToModel(selectedRow);
        int studentId = (Integer) tableModel.getValueAt(modelRow, 0);

        Student student = findStudentById(studentId);
        if (student == null) return;

        UpdateStudentDialog dialog = new UpdateStudentDialog(this, student);
        if (dialog.showDialog()) {
            refreshTable();
            updateStatistics();
            showSuccessMessage("Student updated successfully!");
        }
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorDialog("Please select a student to delete.");
            return;
        }

        int modelRow = studentsTable.convertRowIndexToModel(selectedRow);
        int studentId = (Integer) tableModel.getValueAt(modelRow, 0);
        String studentName = (String) tableModel.getValueAt(modelRow, 1);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + studentName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            student.removeIf(s -> s.getId() == studentId);
            tableModel.removeRow(modelRow);
            updateStatistics();
            showSuccessMessage("Student deleted successfully!");
        }
    }

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter =
                (TableRowSorter<DefaultTableModel>) studentsTable.getRowSorter();

        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void updateStatistics() {
        if (student.isEmpty()) {
            totalStudentsLabel.setText("Total Students: 0");
            averageGradeLabel.setText("Average Grade: N/A");
            highestGradeLabel.setText("Highest Grade: N/A");
            lowestGradeLabel.setText("Lowest Grade: N/A");
            averageProgressBar.setValue(0);
            averageProgressBar.setString("No Data");
            return;
        }

        int total = student.size();
        double average = student.stream().mapToInt(Student::getGrade).average().orElse(0);
        int highest = student.stream().mapToInt(Student::getGrade).max().orElse(0);
        int lowest = student.stream().mapToInt(Student::getGrade).min().orElse(0);

        totalStudentsLabel.setText("Total Students: " + total);
        averageGradeLabel.setText(String.format("Average Grade: %.1f", average));
        highestGradeLabel.setText("Highest Grade: " + highest);
        lowestGradeLabel.setText("Lowest Grade: " + lowest);

        averageProgressBar.setValue((int) average);
        averageProgressBar.setString(String.format("%.1f%%", average));

        if (average >= 90) {
            averageProgressBar.setForeground(SUCCESS_COLOR);
        } else if (average >= 70) {
            averageProgressBar.setForeground(WARNING_COLOR);
        } else {
            averageProgressBar.setForeground(DANGER_COLOR);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student student : this.student) {
            addStudentToTable(student);
        }
    }

    private void clearAllData() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "This will delete all student data. Are you sure?",
                "Clear All Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            student.clear();
            tableModel.setRowCount(0);
            nextStudentId = 1;
            updateStatistics();
            showSuccessMessage("All data cleared successfully!");
        }
    }

    private void generateSampleData() {
        String[] names = {"Alice Johnson", "Bob Smith", "Carol Davis", "David Wilson",
                "Emma Brown", "Frank Miller", "Grace Lee", "Henry Taylor"};
        int[] grades = {95, 87, 92, 78, 85, 90, 88, 93};

        for (int i = 0; i < names.length; i++) {
            Student student = new Student(nextStudentId++, names[i], grades[i]);
            this.student.add(student);
            addStudentToTable(student);
        }

        updateStatistics();
        showSuccessMessage("Sample data generated successfully!");
    }

    private void exportToCsv() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("student_grades_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.println("ID,Student Name,Grade,Letter Grade,Status");

                for (Student student : this.student) {
                    writer.printf("%d,\"%s\",%d,%s,%s%n",
                            student.getId(),
                            student.getName(),
                            student.getGrade(),
                            student.getLetterGrade(),
                            student.getStatus()
                    );
                }

                showSuccessMessage("Data exported successfully to " + fileChooser.getSelectedFile().getName());
            } catch (IOException e) {
                showErrorDialog("Error exporting data: " + e.getMessage());
            }
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(student);
            oos.writeInt(nextStudentId);
            showSuccessMessage("Data saved successfully!");
        } catch (IOException e) {
            showErrorDialog("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            List<Student> loadedStudents = (List<Student>) ois.readObject();
            nextStudentId = ois.readInt();

            student.clear();
            student.addAll(loadedStudents);
            refreshTable();
            updateStatistics();

        } catch (IOException | ClassNotFoundException e) {
            showErrorDialog("Error loading data: " + e.getMessage());
        }
    }

    private void handleApplicationExit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save your data before exiting?",
                "Save Before Exit",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            saveData();
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void showAboutDialog() {
        String message = """
                Enhanced Student Grade Tracker v2.0
                
                A comprehensive grade management system with:
                • Modern, intuitive user interface
                • Data persistence and export functionality
                • Advanced search and filtering
                • Real-time statistics and analytics
                • Professional-grade error handling
                """;

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        border.setTitleColor(PRIMARY_COLOR);
        return border;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        return button;
    }

    private JMenuItem createMenuItem(String text, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(listener);
        return item;
    }

    private JButton findButtonByText(String text) {
        return findComponentByText(this, JButton.class, text);
    }

    @SuppressWarnings("unchecked")
    private <T extends Component> T findComponentByText(Container container, Class<T> type, String text) {
        for (Component component : container.getComponents()) {
            if (type.isInstance(component)) {
                if (component instanceof JButton && ((JButton) component).getText().equals(text)) {
                    return (T) component;
                }
            } else if (component instanceof Container) {
                T found = findComponentByText((Container) component, type, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    private Student findStudentById(int id) {
        return student.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class UpdateStudentDialog extends JDialog {
        private final Student student;
        private JTextField nameField;
        private JSpinner gradeSpinner;
        private boolean confirmed = false;

        public UpdateStudentDialog(JFrame parent, Student student) {
            super(parent, "Update Student", true);
            this.student = student;
            initComponents();
            setupLayout();

            setSize(300, 150);
            setLocationRelativeTo(parent);
        }

        private void initComponents() {
            nameField = new JTextField(student.getName(), 15);
            gradeSpinner = new JSpinner(new SpinnerNumberModel(student.getGrade(), 0, 100, 1));
        }

        private void setupLayout() {
            setLayout(new BorderLayout());

            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(new JLabel("Name:"), gbc);
            gbc.gridx = 1;
            formPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(new JLabel("Grade:"), gbc);
            gbc.gridx = 1;
            formPanel.add(gradeSpinner, gbc);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton okButton = new JButton("Update");
            JButton cancelButton = new JButton("Cancel");

            okButton.addActionListener(e -> {
                student.setName(nameField.getText().trim());
                student.setGrade((Integer) gradeSpinner.getValue());
                confirmed = true;
                dispose();
            });

            cancelButton.addActionListener(e -> dispose());

            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        public boolean showDialog() {
            setVisible(true);
            return confirmed;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }

        SwingUtilities.invokeLater(() -> {
            new EnhancedStudentGradeTracker().setVisible(true);
        });
    }
}

