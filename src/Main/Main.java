package Main;
import java.awt.Color;
import java.awt.event.KeyEvent;
//import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

public class Main {
	 public static void main(String args[]) {
		 JFrame frame = new JFrame();
		 frame.setTitle("TexWing");
		 frame.setSize(600, 420);
		 frame.setResizable(true);
		 frame.setForeground(new Color(0x181818));
		 frame.setBackground(new Color(0xFFFFFF));
		 JLabel label = new JLabel("Welcome to TexWing");
		 label.setHorizontalAlignment(0);
		 JMenuBar menuBar = new JMenuBar();
		 JMenu fileMenu = new JMenu("File");
		 JMenuItem newItem = new JMenuItem("New File");
		 newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		 newItem.addActionListener(e -> newFile(label));
		 fileMenu.add(newItem);
		 menuBar.add(fileMenu);
		 frame.setJMenuBar(menuBar);
		 frame.add(label);
		 frame.show();
	 }

	 private static Object newFile(JLabel label) {
		label.setText("New File(^w^)");
		String path = "./test.txt";
		try {
			FileWriter fWriter = new FileWriter(path);
			fWriter.write("Hello From TexWing:)))");
			fWriter.close();
		} catch (IOException e) {
			System.out.println("Err: Can't write to file");
			e.printStackTrace();
		}
		return null;
	 }
}
