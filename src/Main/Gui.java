package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;



public class Gui extends JFrame{

    public Gui(String title){
	super(title);
        ArrayList<Tab> tabs = new ArrayList<Tab>();
	setTitle("TexWing");
	setSize(600, 400);
	setResizable(true);
	setForeground(new Color(0x181818));
	setBackground(new Color(0xFFFFFF));
	JTextField commandLine = new JTextField("");
	JPanel commandPanel = new JPanel();
	commandPanel.setLayout(new BorderLayout());
	JLabel commandOutPut = new JLabel("Welcome to TexWing");

	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	JMenu editMenu = new JMenu("Edit");
	JMenuItem newItem = new JMenuItem("New File");
	JMenuItem openItem = new JMenuItem("Open File");
	JMenuItem quitItem = new JMenuItem("Quit File");
	JMenuItem saveItem = new JMenuItem("Save File");
	JMenuItem nextTab = new JMenuItem("Next Tab");
	JMenuItem prevTab = new JMenuItem("Previous Tab");

	JTabbedPane tabbedPane = new JTabbedPane();
	tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);//TODO: make tabs closable
	tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

	add(tabbedPane);
	
	newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
	newItem.addActionListener(e -> newFile(tabs, tabbedPane, this));
        nextTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
        nextTab.addActionListener(e -> {
		if (tabbedPane.getSelectedIndex() + 1 < tabbedPane.getTabCount()){
		    tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
		}else{
		    tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
		}
	    });
	prevTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        prevTab.addActionListener(e -> {
		if (tabbedPane.getSelectedIndex() - 1 > 0){
		    tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);//TODO: Make this shit work
		}else{
		    tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
		}
	    });

	// openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
	// openItem.addActionListener(e -> openFile(editor, this));
	
	saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
	saveItem.addActionListener(e -> saveFile(tabs, tabbedPane, this));
	
	quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
	quitItem.addActionListener(e -> dispose());
	
	commandLine.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    String input = commandLine.getText();
		    commandOutPut.setText(executeCommand(input));
		    commandLine.setText("");
		}
	    });
	fileMenu.add(newItem);
	fileMenu.add(openItem);
	fileMenu.add(saveItem);
	fileMenu.add(quitItem);
	editMenu.add(nextTab);
	editMenu.add(prevTab);
	menuBar.add(fileMenu);
	menuBar.add(editMenu);
	setJMenuBar(menuBar);
	

	
	commandPanel.add(commandLine, BorderLayout.CENTER);
	commandPanel.add(commandOutPut, BorderLayout.SOUTH);
	add(commandPanel, BorderLayout.SOUTH);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
    }
    private static Object newFile(ArrayList<Tab> tabs, JTabbedPane tabbedPane, JFrame frame) {
	String path = saveFileDialog(frame);
	try {
	    File myFile = new File(path);
	    boolean created = myFile.createNewFile();
            if (created) {
                System.out.println("File created successfully");
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(new JTextPane()), BorderLayout.CENTER);
		tabbedPane.addTab(myFile.getName(), panel);
		tabs.add(new Tab(myFile.getName(), false, "", path));
            } else {
                System.out.println("File already exists");
            }
	    
	} catch (IOException e) {
	    System.out.println("Err: Can't write to file");
	    e.printStackTrace();
	}
	return null;
    }

    private static Object saveFile(ArrayList<Tab> tabs, JTabbedPane tabbedPane, JFrame frame) {
	try {
	    String path = tabs.get(tabbedPane.getSelectedIndex()).path;
	    JPanel panel = (JPanel) tabbedPane.getSelectedComponent();
	    JTextPane textPane =  panel.getComponent(1);
	    FileWriter fWriter = new FileWriter(path);
	    fWriter.write(text);
	    System.out.println(text);
	    fWriter.close();
     
	} catch (IOException e) {
	    System.out.println("Err: Can't write to file");
	    e.printStackTrace();
	}
	return null;
    }// TODO: Make this work like save and add a save as option

    private static Object openFile(JTextPane editor, JFrame frame) {
	try {
	    File myFile = new File(openFileDialog(frame));
	    Scanner fReader = new Scanner(myFile);
	    String fileContent = new String();
	    while (fReader.hasNextLine()) {
		fileContent += fReader.nextLine() + "\n";
	    }
	    editor.setText(fileContent);
	    fReader.close();
	} catch (IOException e) {
	    System.out.println("Err: Can't read file");
	    e.printStackTrace();
	}
	return null;
    }

    private static String executeCommand(String command) {
	StringBuilder output = new StringBuilder();
	ProcessBuilder processBuilder = new ProcessBuilder();
		
	if (System.getProperty("os.name").toLowerCase().contains("win")) {
	    processBuilder.command("cmd.exe", "/c", command);
	} else {
	    processBuilder.command("bash", "-c", command);
	}

	try {
	    processBuilder.redirectErrorStream(true); 
	    Process process = processBuilder.start();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line;
	    while ((line = reader.readLine()) != null) {
		output.append(line).append("\n");
	    }

	    int exitCode = process.waitFor();
	    output.append("Exit Code: ").append(exitCode);
	} catch (IOException | InterruptedException e) {
	    output.append("Error: ").append(e.getMessage());
	}

	return output.toString();
    }

    private static String openFileDialog(JFrame frame) {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"), "TexWing"));
	int returnValue = fileChooser.showOpenDialog(frame);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    return fileChooser.getSelectedFile().getAbsolutePath();
	}
	return null;
    }

    private static String saveFileDialog(JFrame frame) {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"), "TexWing"));
	int returnValue = fileChooser.showSaveDialog(frame);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    return fileChooser.getSelectedFile().getAbsolutePath();
	}
	return null;
    }
}
