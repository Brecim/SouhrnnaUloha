import javax.swing.*;
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
    private int currentLine = 0;
    private List<String> lines = new ArrayList<>();

    public Window() {
        initComponents();
    }

    private void initComponents() {
        setContentPane(mainPn);
        setTitle("Deskovky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        scan();
        prevBt.addActionListener(e -> {
            if (currentLine > 0) {
                currentLine--;
                scan();
            }
        });
        nextBt.addActionListener(e -> {
            if (currentLine < lines.size() - 1) {
                currentLine++;
                scan();
            }
        });
        saveBt.addActionListener(e -> saveToFile());
        addBt.addActionListener(e -> addDeskovka());

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
                Deskovka deskovka = new Deskovka(data[0], Boolean.parseBoolean(data[1]), Integer.parseInt(data[2]));
                nazevHry.setText(deskovka.getNazev());
                jeKoupenaChBx.setSelected(deskovka.isJeKoupena());
                obl1.setSelected(deskovka.getOblibenost() == 1);
                obl2.setSelected(deskovka.getOblibenost() == 2);
                obl3.setSelected(deskovka.getOblibenost() == 3);
            }
        } catch (FileNotFoundException e) {
            e.getMessage();
            System.exit(404);
        } catch (NumberFormatException e) {
            e.getMessage();
            System.exit(101);
        } catch (IllegalFormatException e) {
            e.getMessage();
            System.exit(102);
        } catch (IOException e) {
            e.getMessage();
            System.exit(403);
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
                e.getMessage();
                System.exit(101);
            } catch (IllegalFormatException e) {
                e.getMessage();
                System.exit(102);
            } catch (IOException e) {
                e.getMessage();
                System.exit(403);
            }
        } catch (FileNotFoundException e) {
            e.getMessage();
            System.exit(404);
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
            e.getMessage();
            System.exit(403);
        }
    }


}



