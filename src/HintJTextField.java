import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HintJTextField extends JTextField implements FocusListener {

    String hint;
    String currentText;

    public HintJTextField(String hint){
        addFocusListener(this);
        this.hint = hint;
        focus();
    }

    @Override
    public void focusGained(FocusEvent e) {
        setForeground(Color.BLACK);
        setText(currentText);
    }

    @Override
    public void focusLost(FocusEvent e) {
        focus();
    }

    private void focus(){
        currentText = getText();
        if (currentText.equals("")){
            setForeground(Color.GRAY);
            setText(hint);
        }
    }

}
