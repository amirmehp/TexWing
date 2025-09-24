package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
	ArrayList<String> openFiles = new ArrayList<String>();
	openFiles.add("Test File");
	openFiles.add("Test File 2");
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
	JMenuItem newItem = new JMenuItem("New File");
	JMenuItem openItem = new JMenuItem("Open File");
	JMenuItem quitItem = new JMenuItem("Quit File");
	JMenuItem saveItem = new JMenuItem("Save File");
	
	newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
	newItem.addActionListener(e -> newFile(openFiles, this));
	// openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
	// openItem.addActionListener(e -> openFile(editor, this));
	// saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
	// saveItem.addActionListener(e -> saveFile(editor, this));
	// quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
	// quitItem.addActionListener(e -> dispose());
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
	menuBar.add(fileMenu);
	setJMenuBar(menuBar);
	

	
	commandPanel.add(commandLine, BorderLayout.CENTER);
	commandPanel.add(commandOutPut, BorderLayout.SOUTH);
	add(commandPanel, BorderLayout.SOUTH);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
    }

    private static Object newFile(ArrayList openFiles, JFrame frame) {
	String path = saveFileDialog(frame);
	try {
	    File myFile = new File(path);
	    boolean created = myFile.createNewFile();
            if (created) {
                System.out.println("File created successfully");
		openFiles.add(myFile.getName());
		updateTabs(openFiles, frame);
            } else {
                System.out.println("File already exists");
            }
	    

	} catch (IOException e) {
	    System.out.println("Err: Can't write to file");
	    e.printStackTrace();
	}
	return null;
    }

    private static Object saveFile(JTextPane editor, JFrame frame) {
	String path = saveFileDialog(frame);
	try {
	    FileWriter fWriter = new FileWriter(path);
	    fWriter.write(editor.getText());
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
    private static String updateTabs(ArrayList<String> openFiles, JFrame frame){
	ArrayList<String> tabsMade = new ArrayList<String>();
	
	for (String t: openFiles){
	    if (!tabsMade.contains(t)){
		JPanel panel = new JPanel(new BorderLayout());
		JTextPane editor = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(editor);
		JTabbedPane tabbedPane = new JTabbedPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		tabbedPane.addTab(t, panel);//TODO: make an array of open files and make tabs closable
		frame.add(tabbedPane);
		tabsMade.add(t);
	    }
	}
	return null;
    }
}
