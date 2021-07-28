package view.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class CustomPasswordField extends JPasswordField {
	public JLabel label = null;
	public CustomPasswordField(String placeHolder,JPanel panel){
		setOpaque(false);
		setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				label.setVisible(false);
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if(new String(getPassword()).isEmpty()) label.setVisible(true);
			}
		});
		label = new JLabel(placeHolder);
		label.setForeground(this.getForeground());
		label.setFont(this.getFont());
		panel.add(label);
	}
	public void setText(String text) {
		super.setText(text);
		label.setVisible(false);
	}
	public void setBounds(int a, int b, int c, int d) {
		super.setBounds(a,b,c,d);
		label.setBounds(a,b,c,d);
	}
	public void setVisible(boolean value) {
		super.setVisible(value);
		label.setVisible(value);
	}
}