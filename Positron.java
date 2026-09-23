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
		setTitle("OS/RX Positron 0.2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		ActionListener chkCols = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleButtons();
			}
		};
		
		menuBar = new JMenuBar();
		menuBar.setBorder(null);
		menuBar.setBackground(Color.DARK_GRAY);
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
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
		rdRed.addActionListener(chkCols);
		colSelector.add(rdRed);
		rdRed.setBackground(new Color(255, 99, 71));
		rdRed.setOpaque(true);
		menuBar.add(rdRed);
		
		JRadioButton rdOrange = new JRadioButton("");
		colSelector.add(rdOrange);
		rdOrange.addActionListener(chkCols);
		rdOrange.setOpaque(true);
		rdOrange.setBackground(new Color(255, 140, 0));
		menuBar.add(rdOrange);
		
		JRadioButton rdYellow = new JRadioButton("");
		colSelector.add(rdYellow);
		rdYellow.setOpaque(true);
		rdYellow.addActionListener(chkCols);
		rdYellow.setBackground(new Color(240, 230, 140));
		menuBar.add(rdYellow);
		
		JRadioButton rdMint = new JRadioButton("");
		colSelector.add(rdMint);
		rdMint.setOpaque(true);
		rdMint.addActionListener(chkCols);
		rdMint.setBackground(new Color(144, 238, 144));
		menuBar.add(rdMint);
		
		JRadioButton rdAqua = new JRadioButton("");
		colSelector.add(rdAqua);
		rdAqua.setOpaque(true);
		rdAqua.addActionListener(chkCols);
		rdAqua.setBackground(new Color(0, 206, 209));
		menuBar.add(rdAqua);
		
		JRadioButton rdBlue = new JRadioButton("");
		colSelector.add(rdBlue);
		rdBlue.setOpaque(true);
		rdBlue.addActionListener(chkCols);
		rdBlue.setBackground(new Color(100, 149, 237));
		menuBar.add(rdBlue);
		
		JRadioButton rdPurple = new JRadioButton("");
		colSelector.add(rdPurple);
		rdPurple.setOpaque(true);
		rdPurple.addActionListener(chkCols);
		rdPurple.setBackground(new Color(106, 90, 205));
		menuBar.add(rdPurple);
		
		JRadioButton rdBlack = new JRadioButton("");
		colSelector.add(rdBlack);
		rdBlack.setOpaque(true);
		rdBlack.addActionListener(chkCols);
		rdBlack.setBackground(new Color(0, 0, 0));
		menuBar.add(rdBlack);
		
		JRadioButton rdGray = new JRadioButton("");
		colSelector.add(rdGray);
		rdGray.setOpaque(true);
		rdGray.addActionListener(chkCols);
		rdGray.setBackground(new Color(192, 192, 192));
		menuBar.add(rdGray);
		
		JRadioButton rdWhite = new JRadioButton("");
		rdWhite.setSelected(true);
		rdWhite.addActionListener(chkCols);
		colSelector.add(rdWhite);
		rdWhite.setOpaque(true);
		rdWhite.setBackground(new Color(255, 255, 255));
		menuBar.add(rdWhite);
		
		Component hozStrut = Box.createHorizontalStrut(20);
		menuBar.add(hozStrut);
		
		final JSpinner penSize = new JSpinner();
		penSize.setMinimumSize(new Dimension(50, 26));
		penSize.setModel(new SpinnerNumberModel(2,1,20,1));
		menuBar.add(penSize);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		board = new PWSBoard();
		contentPane.add(board, BorderLayout.CENTER);
		
		penSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int size = ((Number) penSize.getValue()).intValue();
				board.setLineThickness(size);
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
