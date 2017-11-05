import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PasswordHintTextField extends JPasswordField implements FocusListener {

    String currentText;
    String hint;

    public PasswordHintTextField(){
        addFocusListener(this);
        this.hint = "just letters";
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
