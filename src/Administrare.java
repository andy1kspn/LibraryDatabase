import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Administrare {
    private static JTextArea outputTextArea;  // Added JTextArea field

    public static void createTextFile() {

        String content = "Numar de ordine | Numar de identificare | Denumire | Autor 1 | Autor 2 | Editura | Status | Data | Ora | Persoana";
        String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";

        File file = new File(filePath);

        if (file.exists() && containsLine(file, content)) {
            System.out.println("Fisierul deja exista!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            System.out.println("Fisierul este prezent la adresa: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTextFile(JFrame frame) {
        JPanel adminPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        adminPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        //stylizeAdminPage(adminPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);

        String[] options = {"Adauga carti", "Modifica carti", "Sterge carti"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        adminPanel.add(new JLabel("Operatiune:"));
        adminPanel.add(comboBox);

        adminPanel.add(createLabeledTextField("Numar de ordine:"));
        adminPanel.add(createLabeledTextField("Numar de identificare:"));
        adminPanel.add(createLabeledTextField("Denumire:"));
        adminPanel.add(createLabeledTextField("Editura:"));
        adminPanel.add(createLabeledTextField("Autor 1:"));
        adminPanel.add(createLabeledTextField("Autor 2:"));


        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton imprumutatRadioButton = new JRadioButton("Imprumutat");
        JRadioButton prezentRadioButton = new JRadioButton("Prezent");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(imprumutatRadioButton);
        buttonGroup.add(prezentRadioButton);
        radioPanel.add(new JLabel("Status:"));
        radioPanel.add(imprumutatRadioButton);
        radioPanel.add(prezentRadioButton);
        adminPanel.add(radioPanel);

        adminPanel.add(createLabeledTextField("Data:"));
        adminPanel.add(createLabeledTextField("Ora:"));
        adminPanel.add(createLabeledTextField("Persoana:"));

        JButton submitButton = new JButton("Aplica!");
        stylizeButton(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String operatiune = (String) comboBox.getSelectedItem();
                String numarOrdine = getTextFieldValue(adminPanel, "Numar de ordine:");
                String numarIdentificare = getTextFieldValue(adminPanel, "Numar de identificare:");
                String denumire = getTextFieldValue(adminPanel, "Denumire:");
                String autor1 = getTextFieldValue(adminPanel, "Autor 1:");
                String autor2 = getTextFieldValue(adminPanel, "Autor 2:");
                String editura = getTextFieldValue(adminPanel, "Editura:");

                String status;
                if (imprumutatRadioButton.isSelected()) {
                    String data = getTextFieldValue(adminPanel, "Data:");
                    String ora = getTextFieldValue(adminPanel, "Ora:");
                    String persoana = getTextFieldValue(adminPanel, "Persoana:");
                    status = "Imprumutat | " + data + " | " + ora + " | " + persoana;
                } else {
                    status = "Prezent | - | - | -";
                }


                if (operatiune.equals("Adauga carti")) {
                    String content = numarOrdine + " | " + numarIdentificare + " | " + denumire + " | " + autor1 + " | " + autor2 + " | " + editura + " | " + status;
                    appendToFile(content);
                    resetTextFields(adminPanel);
                } else if (operatiune.equals("Modifica carti")) {
                    modifyLine(numarOrdine, numarIdentificare, denumire, autor1, autor2, editura, status);
                    resetTextFields(adminPanel);
                } else if (operatiune.equals("Sterge carti")) {
                    deleteLineByNumarOrdine(numarOrdine);
                    resetTextFields(adminPanel);
                }
            }
        });

        adminPanel.add(submitButton);


//=============================================================================================================
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints detailsConstraints = new GridBagConstraints();
        detailsConstraints.gridx = 0;
        detailsConstraints.gridy = 0;
        detailsConstraints.insets = new Insets(10, 10, 10, 10);

        // Add components to the detailsPanel
        // Example:
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        detailsConstraints.weightx = 1.0;
        detailsConstraints.weighty = 1.0;
        detailsConstraints.fill = GridBagConstraints.BOTH;
        detailsConstraints.gridwidth = GridBagConstraints.REMAINDER;
        detailsPanel.add(scrollPane, detailsConstraints);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, adminPanel, detailsPanel);
        splitPane.setDividerLocation(464); // Set the initial width of the adminPanel

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshDetailsPanel();
            }
        });
        timer.start();


        frame.getContentPane().removeAll();
        frame.getContentPane().add(splitPane);
        frame.revalidate();
        frame.repaint();
    }

        // Schedule a task to refresh the content every 5 seconds




    private static void refreshDetailsPanel() {
            String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                outputTextArea.setText(content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private static JPanel createLabeledTextField(String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(jLabel.getFont().deriveFont(Font.PLAIN, 14f));
        panel.add(jLabel, BorderLayout.NORTH);

        JTextField textField = new JTextField();
        stylizeInputField(textField);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    private static void stylizeInputField(JTextField textField) {
        textField.setPreferredSize(new Dimension(200, 50));
        textField.setFont(textField.getFont().deriveFont(Font.PLAIN, 14f));
    }

    private static void stylizeButton(JButton button) {
        button.setFont(button.getFont().deriveFont(Font.BOLD, 14f));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(32, 226, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Apply gradient effect to button background
        GradientPaint gradient = new GradientPaint(0, 0, new Color(32, 226, 0), 0, button.getHeight(), new Color(41, 128, 185));
        ButtonModel model = button.getModel();
        model.addChangeListener(e -> {
            if (model.isRollover()) {
                button.setBackground(new Color(32, 226, 0));
            } else {
                button.setBackground(new Color(32, 226, 0));
            }
        });
        button.setOpaque(true);
        button.setBackground(new Color(32, 226, 0));
        button.setBorderPainted(false);
        button.setBackground(new Color(32, 226, 0));
    }

    private static boolean containsLine(File file, String line) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(line)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void appendToFile(String content) {
        String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.newLine();
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTextFieldValue(Container container, String labelText) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                JLabel label = (JLabel) panel.getComponent(0);
                JTextField textField = (JTextField) panel.getComponent(1);
                if (label.getText().equals(labelText)) {
                    return textField.getText();
                }
            }
        }
        return "";
    }

    private static void resetTextFields(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                JTextField textField = (JTextField) panel.getComponent(1);
                textField.setText("");
            }
        }
    }

    private static boolean isNumarOrdineExists(String numarOrdine) {
        String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith(numarOrdine + " |")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void modifyLine(String numarOrdine, String numarIdentificare, String denumire, String autor1, String autor2, String editura, String status) {
        String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";
        File tempFile = new File(filePath + ".tmp");

        if (!isNumarOrdineExists(numarOrdine)) {
            System.out.println("Numar de ordine nu exista.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith(numarOrdine + " |")) {
                    String newLine = numarOrdine + " | " + numarIdentificare + " | " + denumire + " | " + autor1 + " | " + autor2 + " | " + editura + " | " + status;
                    writer.write(newLine);
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename the temporary file to replace the original file
        File originalFile = new File(filePath);
        if (originalFile.exists()) {
            if (originalFile.delete()) {
                tempFile.renameTo(originalFile);
            } else {
                System.out.println("Failed to modify the line.");
            }
        } else {
            System.out.println("The database file doesn't exist.");
        }
    }
    private static void deleteLineByNumarOrdine(String numarOrdine) {
        String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";
        String tempFilePath = System.getProperty("user.home") + File.separator + "TEMP_DATABASE.txt";

        File inputFile = new File(filePath);
        File tempFile = new File(tempFilePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;

            // Copy all lines except the one to be deleted to the temporary file
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.startsWith(numarOrdine + " |")) {
                    writer.write(currentLine);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename the temporary file to the original file
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Failed to rename the temporary file.");
            }
        } else {
            System.out.println("Failed to delete the original file.");
        }
    }
    private static void refreshOutputTextArea() {
        String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            outputTextArea.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getComponentValue(Container container, String componentName) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField && component.getName().equals(componentName)) {
                JTextField textField = (JTextField) component;
                return textField.getText();
            } else if (component instanceof JRadioButton && component.getName().equals(componentName)) {
                JRadioButton radioButton = (JRadioButton) component;
                return String.valueOf(radioButton.isSelected());
            } else if (component instanceof Container) {
                String value = getComponentValue((Container) component, componentName);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }


}
