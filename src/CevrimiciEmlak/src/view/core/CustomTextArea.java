package view.core;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;
import javax.swing.border.MatteBorder;


public class CustomTextArea extends JTextArea {
	public JLabel label = null;
	public CustomTextArea(String placeHolder,JPanel panel){
		setOpaque(false);
		setBorder(new MatteBorder(2, 2, 2, 2, Color.decode("#034E5E")));
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				label.setVisible(false);
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if(getText().isEmpty()) label.setVisible(true);
			}
		});
		label = new JLabel(placeHolder);
		label.setForeground(this.getForeground());
		label.setFont(this.getFont());
		String text = this.getText();
		if(text.isEmpty() == false) label.setVisible(false);
		panel.add(label);
	}
	public void setText(String text) {
		super.setText(text);
		label.setVisible(false);
	}
	public void setBounds(int a, int b, int c, int d) {
		super.setBounds(a,b,c,d);
		label.setBounds(a+10,b,c,32);
	}
	public void setVisible(boolean value) {
		super.setVisible(value);
		label.setVisible(value);
	}
}
