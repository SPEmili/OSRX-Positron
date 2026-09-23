package positronws;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JRadioButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Cursor;

public class Positron extends JFrame {

	//serial added to fix a bug with some older JREs
	private static final long serialVersionUID = 12345L;
	
	private JPanel contentPane;
	private final ButtonGroup colSelector = new ButtonGroup();
	private final JMenuBar menuBar;
	private final PWSBoard board;
	private String workingFile = null;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Positron frame = new Positron();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void toggleButtons()
	{
		for(int i=0; i<menuBar.getComponentCount(); i++)
		{
			if(menuBar.getComponent(i) instanceof JRadioButton)
			{
				if(((JRadioButton) menuBar.getComponent(i)).isSelected())
					board.setLineColor(menuBar.getComponent(i).getBackground());
				System.out.println("Checked " + i + " of " + menuBar.getComponentCount());
			}
		}
	}
	
	public Positron() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
		        int choice = JOptionPane.showConfirmDialog( null, "Are you sure you want to close? Any unsaved progress will be lost.",  "Closing", JOptionPane.YES_NO_OPTION);

		        if (choice == JOptionPane.YES_OPTION) {
		            System.exit(0);
		        }
			}
		});
		setTitle("OS/RX Positron 0.3");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		ActionListener chkCols = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleButtons();
			}
		};
		
		menuBar = new JMenuBar();
		menuBar.setMinimumSize(new Dimension(0, 50));
		menuBar.setBorder(null);
		menuBar.setBackground(Color.DARK_GRAY);
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		menuBar.add(mnFile);
		
		JMenuItem mnNew = new JMenuItem("New Board...");
		mnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workingFile = null;
				board.clearCanvas();
			}
		});
		mnFile.add(mnNew);
		
		JMenuItem mnOpen = new JMenuItem("Open Board...");
		mnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//copied mostly from IS (which caused messy indentation)
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileFilter(new PNGFileFilter());
		        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		        	try {
		        		board.loadPng(chooser.getSelectedFile().getAbsolutePath());
		        		workingFile = chooser.getSelectedFile().getAbsolutePath();
		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(null, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		            }
			}
		}});
		
		mnFile.add(mnOpen);
		
		JMenuItem mnSave = new JMenuItem("Save Board...");
		mnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null;
				if(workingFile == null) {
					JFileChooser chooser = new JFileChooser();
	                chooser.setFileFilter(new PNGFileFilter());
	                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	                    file = chooser.getSelectedFile();
	                    if (!file.getName().toLowerCase().endsWith(".png")) {
	                        file = new File(file.getAbsolutePath() + ".png");
	                        workingFile = file.getAbsolutePath();
	                    }
	                }
				} else {
					file = new File(workingFile);
				}
				ISScreenshot.saveComponentAsPng(board, file.getAbsolutePath());
			}
		});
		mnFile.add(mnSave);
		
		
		JRadioButton rdRed = new JRadioButton("");
		rdRed.setBorder(new LineBorder(new Color(255, 255, 255)));
		rdRed.addActionListener(chkCols);
		colSelector.add(rdRed);
		rdRed.setBackground(new Color(255, 99, 71));
		rdRed.setOpaque(true);
		menuBar.add(rdRed);
		
		JRadioButton rdOrange = new JRadioButton("");
		rdOrange.setBorder(new LineBorder(new Color(255, 255, 255)));
		colSelector.add(rdOrange);
		rdOrange.addActionListener(chkCols);
		rdOrange.setOpaque(true);
		rdOrange.setBackground(new Color(255, 140, 0));
		menuBar.add(rdOrange);
		
		JRadioButton rdYellow = new JRadioButton("");
		rdYellow.setBorder(new LineBorder(new Color(255, 255, 255)));
		colSelector.add(rdYellow);
		rdYellow.setOpaque(true);
		rdYellow.addActionListener(chkCols);
		rdYellow.setBackground(new Color(240, 230, 140));
		menuBar.add(rdYellow);
		
		JRadioButton rdMint = new JRadioButton("");
		rdMint.setBorder(new LineBorder(new Color(255, 255, 255)));
		colSelector.add(rdMint);
		rdMint.setOpaque(true);
		rdMint.addActionListener(chkCols);
		rdMint.setBackground(new Color(144, 238, 144));
		menuBar.add(rdMint);
		
		JRadioButton rdAqua = new JRadioButton("");
		rdAqua.setBorder(new LineBorder(new Color(255, 255, 255)));
		colSelector.add(rdAqua);
		rdAqua.setOpaque(true);
		rdAqua.addActionListener(chkCols);
		rdAqua.setBackground(new Color(0, 206, 209));
		menuBar.add(rdAqua);
		
		JRadioButton rdBlue = new JRadioButton("");
		rdBlue.setBorder(new LineBorder(new Color(255, 255, 255)));
		colSelector.add(rdBlue);
		rdBlue.setOpaque(true);
		rdBlue.addActionListener(chkCols);
		rdBlue.setBackground(new Color(100, 149, 237));
		menuBar.add(rdBlue);
		
		JRadioButton rdPurple = new JRadioButton("");
		rdPurple.setBorder(new LineBorder(new Color(255, 255, 255)));
		colSelector.add(rdPurple);
		rdPurple.setOpaque(true);
		rdPurple.addActionListener(chkCols);
		rdPurple.setBackground(new Color(106, 90, 205));
		menuBar.add(rdPurple);
		
		JRadioButton rdBlack = new JRadioButton("");
		rdBlack.setBorder(new LineBorder(new Color(255, 255, 255)));
		colSelector.add(rdBlack);
		rdBlack.setOpaque(true);
		rdBlack.addActionListener(chkCols);
		rdBlack.setBackground(new Color(0, 0, 0));
		menuBar.add(rdBlack);
		
		JRadioButton rdGray = new JRadioButton("");
		rdGray.setBorder(new LineBorder(new Color(255, 255, 255)));
		colSelector.add(rdGray);
		rdGray.setOpaque(true);
		rdGray.addActionListener(chkCols);
		rdGray.setBackground(new Color(192, 192, 192));
		menuBar.add(rdGray);
		
		JRadioButton rdWhite = new JRadioButton("");
		rdWhite.setBorder(new LineBorder(new Color(255, 255, 255)));
		rdWhite.setSelected(true);
		rdWhite.addActionListener(chkCols);
		colSelector.add(rdWhite);
		rdWhite.setOpaque(true);
		rdWhite.setBackground(new Color(255, 255, 255));
		menuBar.add(rdWhite);
		
		Component hozStrut = Box.createHorizontalStrut(20);
		menuBar.add(hozStrut);
		
		JButton btnInc = new JButton("+");
		
		btnInc.setMaximumSize(new Dimension(15, 29));
		btnInc.setMinimumSize(new Dimension(15, 29));
		menuBar.add(btnInc);
		
		JButton btnDec = new JButton("-");
		btnDec.setMaximumSize(new Dimension(15, 29));
		btnDec.setMinimumSize(new Dimension(15, 29));
		menuBar.add(btnDec);
		
		final JSpinner penSize = new JSpinner();
		penSize.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		penSize.setMinimumSize(new Dimension(50, 26));
		penSize.setModel(new SpinnerNumberModel(3,1,20,1));
		menuBar.add(penSize);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		board = new PWSBoard();
		board.setLineThickness(3);
		board.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		contentPane.add(board, BorderLayout.CENTER);
		
		penSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int size = ((Number) penSize.getValue()).intValue();
				board.setLineThickness(size);
			}
		});
		
		btnInc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				penSize.setValue(penSize.getNextValue());
			}
		});
		
		btnDec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				penSize.setValue(penSize.getPreviousValue());
			}
		});
	}
	
	//file filter (from IronSlug)
	private static class PNGFileFilter extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory())
                return true;
            return f.getName().toLowerCase().endsWith(".png");
        }

        public String getDescription() {
            return "Portable Network Graphics (.png)";
        }
    }

}
