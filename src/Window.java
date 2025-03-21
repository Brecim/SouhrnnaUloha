import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Scanner;

public class Window extends JFrame {
    private JPanel mainPn;
    private JTextPane nazevHry;
    private JButton prevBt;
    private JButton nextBt;
    private JCheckBox jeKoupenaChBx;
    private JRadioButton obl1;
    private JRadioButton obl2;
    private JRadioButton obl3;
    private JButton saveBt;
    private JButton addBt;
    private JButton delBt;
    private int currentLine = 0;
    public final List<String> lines = new ArrayList<>();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("Soubor");
    private JMenuItem closeItem = new JMenuItem("Zavřít", KeyEvent.VK_Z);
    private JMenuItem testItem = new JMenuItem("Test");
    private JMenuItem statMenu = new JMenuItem("Statistiky");
    private JMenuItem pathMenu = new JMenuItem("Ukázat soubor");

    public Window() {
        initComponents();
    }

    private void initComponents() {
        setContentPane(mainPn);
        setTitle("Deskovky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuConfig();
        pack();
        scan();
        nazevHry.setBorder(BorderFactory.createLineBorder(Color.black));
        delBt.setBackground(Color.red);
        addBt.setBackground(Color.green);
        prevBt.addActionListener(e -> {
            if (currentLine > 0) {
                currentLine--;
                scan();
            }
        });
        nextBt.addActionListener(e -> {
            if (currentLine < lines.size()) {
                currentLine++;
                scan();
            } else {
            }
        });
        saveBt.addActionListener(e -> saveToFile());
        addBt.addActionListener(e -> addDeskovka());
        delBt.addActionListener(e -> delDeskovka());

    }

    private void menuConfig() {
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);

        fileMenu.add(closeItem);
        fileMenu.add(testItem);
        fileMenu.add(pathMenu);

        menuBar.add(statMenu);

        closeItem.addActionListener(e -> System.exit(0));
        closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));

        testItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Test", "Test", JOptionPane.INFORMATION_MESSAGE);
        });

        statMenu.addActionListener(e -> stats());

        pathMenu.addActionListener(e -> openFileGUI());




    }

    private void stats() {
        Popup popup = new Popup();
        popup.setVisible(true);
    }

    private void openFileGUI() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fc.setFileFilter(new FileNameExtensionFilter("Text", "txt"));
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Path: " + selectedFile, "Path", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void scan() {
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("deskovky.txt")))) {
            lines.clear();
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            if (currentLine < lines.size()) {
                String line = lines.get(currentLine);
                String[] data = line.split(";");
                Deskovka deskovka = new Deskovka(data[0].trim(), Boolean.parseBoolean(data[1].trim()), Integer.parseInt(data[2].trim()));
                nazevHry.setText(deskovka.getNazev());
                jeKoupenaChBx.setSelected(deskovka.isJeKoupena());
                obl1.setSelected(deskovka.getOblibenost() == 1);
                obl2.setSelected(deskovka.getOblibenost() == 2);
                obl3.setSelected(deskovka.getOblibenost() == 3);
            }
            prevBt.setEnabled(currentLine > 0);
            nextBt.setEnabled(currentLine < lines.size() - 1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(404);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            System.exit(101);
        } catch (IllegalFormatException e) {
            System.err.println(e.getMessage());
            System.exit(102);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
            System.exit(103);
        }
    }

    private void saveToFile() {
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("deskovky.txt")))) {
            List<String> newLines = new ArrayList<>();
            int lineNumber = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (lineNumber == currentLine) {
                    String[] data = line.split(";");
                    data[0] = nazevHry.getText();
                    data[1] = String.valueOf(jeKoupenaChBx.isSelected());
                    int oblibenost = 0;
                    if (obl1.isSelected()) {
                        oblibenost = 1;
                    } else if (obl2.isSelected()) {
                        oblibenost = 2;
                    } else if (obl3.isSelected()) {
                        oblibenost = 3;
                    }
                    data[2] = String.valueOf(oblibenost);
                    line = String.join(";", data);
                }
                newLines.add(line);
                lineNumber++;
            }
            sc.close();

            try (PrintWriter pw = new PrintWriter(new FileWriter("deskovky.txt"))) {
                for (String newLine : newLines) {
                    pw.println(newLine);
                }
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
                System.exit(101);
            } catch (IllegalFormatException e) {
                System.err.println(e.getMessage());
                System.exit(102);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
                System.exit(103);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(403);
        }
    }

    private void addDeskovka() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("deskovky.txt", true))) {
            String nazev = nazevHry.getText();
            boolean jeKoupena = jeKoupenaChBx.isSelected();
            int oblibenost = 0;
            if (obl1.isSelected()) {
                oblibenost = 1;
            } else if (obl2.isSelected()) {
                oblibenost = 2;
            } else if (obl3.isSelected()) {
                oblibenost = 3;
            }
            pw.println(nazev + ";" + jeKoupena + ";" + oblibenost);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(403);
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
            System.exit(103);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            System.exit(101);
        } catch (IllegalFormatException e) {
            System.err.println(e.getMessage());
            System.exit(102);
        }
    }

    private void delDeskovka() {
        if (!lines.isEmpty() && currentLine >= 0) {
            lines.remove(currentLine);
            try (PrintWriter pw = new PrintWriter(new FileWriter("deskovky.txt"))) {
                for (String line : lines) {
                    pw.println(line);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(403);
            } catch (IndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
                System.exit(103);
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
                System.exit(101);
            } catch (IllegalFormatException e) {
                System.err.println(e.getMessage());
                System.exit(102);
            }
        }
    }
}



