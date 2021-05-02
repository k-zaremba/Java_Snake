import javax.swing.*;
import java.awt.*;

public class ResetButton  extends JButton{

    ResetButton(){
        this.setFocusable(false);
        this.setBackground(Color.black);
        this.setText("RESET");
        this.setForeground(new Color(200,0,100));
    }
}
