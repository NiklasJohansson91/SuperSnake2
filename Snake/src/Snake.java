import javax.swing.*;
import java.awt.*;

public class Snake extends RenderPanel{


    public Snake() {


        startGame();
        renderPanel = new RenderPanel();
        System.out.println("This is apple " + this.apple.x + " " + this.apple.y);
        toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        JFrame jframe = new JFrame("SuperSnake");
        jframe.add(renderPanel);
        jframe.setVisible(true);
        jframe.setSize(755, 520);
        jframe.getContentPane().setSize(640, 480);
        jframe.setLocation(((dim.width) - jframe.getWidth()) / 2, ((dim.height) - jframe.getHeight()) / 2);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.addKeyListener(this);


    }

}
