    import javax.swing.*;
    import javax.swing.border.Border;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.io.*;

    public class Cautare {
        private static JTextArea outputTextArea;

        public static void createTextFile(JFrame frame) {
            JPanel searchPanel = new JPanel(new GridBagLayout());
            stylizeSearchPage(searchPanel);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.insets = new Insets(10, 10, 10, 10);

            String[] options = {"Numar de ordine", "Numar de identificare", "Denumire", "Autor 1", "Autor 2", "Editura", "Status"};
            JComboBox<String> comboBox = new JComboBox<>(options);
            searchPanel.add(comboBox, constraints);

            constraints.gridy = 1;
            searchPanel.add(new JLabel("Cautare: "), constraints);

            JTextField searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(200, 30));
            constraints.gridy = 2;
            searchPanel.add(searchField, constraints);

            JButton searchButton = new JButton("Cauta");
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String field = (String) comboBox.getSelectedItem();
                    String searchValue = searchField.getText();
                    String[] foundOptions = searchInDatabase(field, searchValue);
                    displaySearchResults(foundOptions);
                }
            });
            constraints.gridy = 3;
            searchPanel.add(searchButton, constraints);



            outputTextArea = new JTextArea();
            outputTextArea.setEditable(false);
            JScrollPane scrollPane1 = new JScrollPane(outputTextArea);
            constraints.gridy = 5;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.PAGE_START;
            constraints.gridwidth = 2;
            searchPanel.add(scrollPane1, constraints);

            constraints.anchor = GridBagConstraints.NORTHEAST;
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.insets = new Insets(10, 10, 10, 10);





            frame.getContentPane().removeAll();
            frame.getContentPane().add(searchPanel);
            frame.revalidate();
            frame.repaint();
        }

        private static void stylizeSearchPage(JPanel panel) {
            panel.setOpaque(false);

            Font labelFont = new Font("Arial", Font.BOLD, 16);
            Font buttonFont = new Font("Arial", Font.BOLD, 12);
            Color buttonColor = new Color(32, 226, 0, 200);

            for (Component component : panel.getComponents()) {
                if (component instanceof JLabel) {
                    ((JLabel) component).setFont(labelFont);
                    ((JLabel) component).setForeground(Color.WHITE);
                } else if (component instanceof JButton) {
                    JButton button = (JButton) component;
                    button.setFont(buttonFont);
                    button.setForeground(Color.WHITE);
                    button.setBackground(buttonColor);
                    button.setFocusPainted(false);
                    button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                    int borderRadius = 15;
                    Border roundedBorder = new RoundedBorder(buttonColor, borderRadius);
                    button.setBorder(roundedBorder);
                } else if (component instanceof JComboBox) {
                    JComboBox<?> comboBox = (JComboBox<?>) component;
                    comboBox.setFont(labelFont);
                    comboBox.setForeground(Color.WHITE);
                    comboBox.setBackground(new Color(0, 0, 0, 150));
                    comboBox.setOpaque(true);
                } else if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    textField.setFont(labelFont);
                    textField.setForeground(Color.WHITE);
                    textField.setBackground(new Color(0, 0, 0, 150));
                    textField.setOpaque(true);
                }
            }
        }

        private static String[] searchInDatabase(String field, String searchValue) {
            String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";
            String[] foundOptions = new String[0];

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    String[] optionFields = currentLine.split(" \\| ");
                    if (optionFields.length >= 7) {
                        String fieldValue = "";
                        switch (field) {
                            case "Numar de ordine":
                                fieldValue = optionFields[0];
                                break;
                            case "Numar de identificare":
                                fieldValue = optionFields[1];
                                break;
                            case "Denumire":
                                fieldValue = optionFields[2];
                                break;
                            case "Autor 1":
                                fieldValue = optionFields[3];
                                break;
                            case "Autor 2":
                                fieldValue = optionFields[4];
                                break;
                            case "Editura":
                                fieldValue = optionFields[5];
                                break;
                            case "Status":
                                fieldValue = optionFields[6];
                                break;
                        }
                        if (fieldValue.equalsIgnoreCase(searchValue)) {
                            foundOptions = appendToArray(foundOptions, currentLine);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return foundOptions;
        }

        private static String[] appendToArray(String[] array, String element) {
            String[] newArray = new String[array.length + 1];
            System.arraycopy(array, 0, newArray, 0, array.length);
            newArray[array.length] = element;
            return newArray;
        }

        private static void displaySearchResults(String[] options) {
            outputTextArea.setText("Numar de ordine | Numar de identificare | Denumire | Autor 1 | Autor 2 | Editura | Status | Data | Ora | Persoana\n");
            outputTextArea.append("=====================================================================================\n");
            if (options.length > 0) {
                for (String option : options) {
                    outputTextArea.append(option + "\n");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nu exista astfel de element in baza de date!", "Cautare esuata!", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        static class RoundedBorder implements Border {
            private final Color borderColor;
            private final int borderRadius;

            public RoundedBorder(Color borderColor, int borderRadius) {
                this.borderColor = borderColor;
                this.borderRadius = borderRadius;
            }

            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(borderColor);
                g2d.drawRoundRect(x, y, width - 1, height - 1, borderRadius, borderRadius);
                g2d.dispose();
            }

            public Insets getBorderInsets(Component c) {
                return new Insets(borderRadius, borderRadius, borderRadius, borderRadius);
            }

            public boolean isBorderOpaque() {
                return true;
            }
        }
    }