import javax.swing.*;

public class Popup extends JFrame {
    private JButton exitBtn;
    private JLabel gameCount;
    private JPanel ppPanel;

    public Popup() {
        initComponents();
        getCount();
    }

    private void initComponents() {
        setTitle("Statistiky");
        setContentPane(ppPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();

        exitBtn.addActionListener(e -> dispose());
    }

    private void getCount() {
        Window window = new Window();
        gameCount.setText("Počet her: " + window.lines.size());
    }

}
