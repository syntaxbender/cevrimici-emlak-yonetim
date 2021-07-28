package view.core;

import java.awt.*;

import javax.swing.*;

public class MainFrame extends JFrame {
	MainFrame currentFrame = this;
	public MainFrame(int xsize,int ysize,boolean close){
		this.setTitle("Çevrimiçi Emlak Yönetim Sistemi");		
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.decode("#000000"));
		this.setSize(ysize,xsize);
		this.centerFrame();
	}
	public MainFrame(int xsize,int ysize){
		this.setTitle("Çevrimiçi Emlak Yönetim Sistemi");		
		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setBackground(Color.decode("#000000"));
		this.setSize(ysize,xsize);
		this.centerFrame();
	}
    private void centerFrame() {

        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;    
        this.setLocation(dx, dy);
    }
}
