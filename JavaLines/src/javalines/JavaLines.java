package javalines;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JavaLines {
	
	void load(File file) {
		try {
			Scanner sc = new Scanner(file);
			int count = 0;
			int count_empty = 0;
			int count_comment = 0;
			boolean has_comment = false;
			
			while (sc.hasNextLine()) {
				
				String line = sc.nextLine();
				
				line = line.replaceAll("\\s+","");
				
				if (line.indexOf("/*") != -1) {
					has_comment = true;
				}
				
				if (has_comment) {
					count_comment +=1;
				}
				
				if (line.indexOf("*/") != -1) {
					has_comment = false;
				}
				
				if (line.isEmpty()) {
					count_empty += 1;
				}
				
				count += 1;
			}
			
			JOptionPane.showMessageDialog(null, "Archivo '"+ file.getName() + "'.\n" +
												"Lineas en el archivo: " + count + "\n" +
												"Lineas en blanco: " + count_empty + "\n" +
												"Comentarios multi-línea: " + count_comment);
			
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private JFrame frmJavalines;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaLines window = new JavaLines();
					window.frmJavalines.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JavaLines() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJavalines = new JFrame();
		frmJavalines.setTitle("JavaLines");
		frmJavalines.setBounds(100, 100, 450, 300);
		frmJavalines.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnAbrir = new JButton("Abrir...");
		btnAbrir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				fc.setFileFilter(new FileNameExtensionFilter("Java (*.java)", "java"));
				
				int returnVal = fc.showOpenDialog(btnAbrir);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					load(file);
				}
			}
		});
		frmJavalines.getContentPane().add(btnAbrir, BorderLayout.NORTH);
	}

}
