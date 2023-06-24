import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MeniuPrincipal {



    public void generateWindow() {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Biblioteca Baraboi Database - dezvoltat de Spinu Andrei");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1113, 500));

        ImageIcon backgroundImageIcon = new ImageIcon(MeniuPrincipal.class.getResource("background.png"));
        JLabel backgroundLabel = new JLabel(backgroundImageIcon);
        backgroundLabel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("Biblioteca Baraboi Database");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 24f)); // Set label font to bold and size 24

        JLabel label2 = new JLabel("\n");

        JButton button1 = new JButton("                Cauta carti!                ");
        stylizeButton(button1); // Apply custom button styling
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cautare.createTextFile(frame);
            }
        });

        JButton button3 = new JButton("Administreaza Baza de Date!");
        stylizeButton(button3);

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Administrare.createTextFile(frame);


            }
        });


        JButton button2 = new JButton("                  Iesire                     ");
        stylizeButton(button2); // Apply custom button styling
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your functionality here for the "Iesire" button
                int response = JOptionPane.showConfirmDialog(frame, "Doriti sa inchideti programul?", "Iesire", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set background color with alpha value for transparency
                g.setColor(new Color(255, 255, 255, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false); // Set panel to be non-opaque for transparency

        // Add a black line border around the panel
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        panel.add(label, constraints);
        panel.add(label2, constraints);

        constraints.gridy = 1;
        panel.add(button1, constraints);

        constraints.gridy = 2;
        panel.add(button3, constraints);

        constraints.gridy = 3;
        panel.add(button2, constraints);

        backgroundLabel.add(panel);
       frame.getContentPane().add(backgroundLabel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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



}
