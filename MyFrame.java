import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyFrame extends JFrame {
    MyPanel panel;
    MyFrame() {


        panel = new MyPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }





}
