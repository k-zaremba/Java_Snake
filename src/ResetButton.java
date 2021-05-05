import javax.swing.*;
import java.awt.*;

public class ResetButton  extends JButton{

    ResetButton(){
        this.setFocusable(false);
        this.setBackground(ColorSheet.gray);
        this.setText("RESET");
        this.setForeground(ColorSheet.orange);
    }
}
