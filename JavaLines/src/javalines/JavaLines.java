package javalines;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;

public class JavaLines {

	JTextArea txtrHelloWorld;

	private static final Pattern TAG_REGEX = Pattern.compile("\"(.+?)\"");

	private static List<String> getTagValues(final String str) {
		final List<String> tagValues = new ArrayList<String>();
		final Matcher matcher = TAG_REGEX.matcher(str);
		while (matcher.find()) {
			tagValues.add(matcher.group(1));
		}
		return tagValues;
	}

	String load(File file) throws IOException {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			int count = 0;
			int count_empty = 0;
			int count_comment = 0;
			int count_single_comment = 0;
			boolean has_comment = false;
			String line;
			
			while ((line = br.readLine()) != null) {
				// Remove emtpy space
				line = line.replaceAll("\\s+", "");

				// Get strings
				List<String> items_to_remove = getTagValues(line);

				// Remove these strings to avoid conflict with comments
				for (int i = 0; i < items_to_remove.size(); i++) {
					line = line.replace("\"" + items_to_remove.get(i) + "\"", "");
				}

				// Start of multi-line comment
				if (line.indexOf("/*") != -1) {
					has_comment = true;
				}

				// Line with comment
				if (has_comment) {
					count_comment += 1;
				}

				// Line with single comment
				if ((!has_comment) && (line.indexOf("//") != -1)) {
					count_single_comment += 1;
				}

				// End of multi-line comment
				if (line.indexOf("*/") != -1) {
					has_comment = false;
				}

				// Empty space
				if (line.isEmpty()) {
					count_empty += 1;
				}

				// Any line
				count += 1;
			}
			
			br.close();

			return "Archivo '" + file.getName() + "'\n" + "Lineas en el archivo: " + count + "\n" + "Lineas en blanco: "
					+ count_empty + "\n" + "Comentarios //: " + count_single_comment + "\n"
					+ "Comentarios multi-línea /* */: " + count_comment;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private JFrame frmJavalines;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

		JButton btnAbrir = new JButton("Abrir archivo Java...");
		btnAbrir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

				fc.setFileFilter(new FileNameExtensionFilter("Java (*.java)", "java"));

				int returnVal = fc.showOpenDialog(btnAbrir);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						txtrHelloWorld.setText(load(file));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		frmJavalines.getContentPane().add(btnAbrir, BorderLayout.NORTH);

		txtrHelloWorld = new JTextArea();
		frmJavalines.getContentPane().add(txtrHelloWorld, BorderLayout.CENTER);
	}

}
