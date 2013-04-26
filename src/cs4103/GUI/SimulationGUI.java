package cs4103.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import cs4103.exceptions.DataException;
import cs4103.utils.logger.SystemLogger;

/**
 * This is a simple gui which allows for entering parameters such as probability
 * of node failure and network failure, number of nodes, etc. It also displays
 * the logged messages by the system
 * 
 * @author 120010516
 * 
 */
public class SimulationGUI implements Observer {
	private static final int WIDTH = 940;
	private static final int HEIGHT = 400;
	private static final int TEXT_H = 11;
	private static final int TEXT_W = 11;
	private static final String NUM_NODES = "10";
	private static final String NUM_REPLICAS = "1";
	private static final String INPUT_FILE = "find a file";
	private static final String NET_FAILURE = "0";
	private static final String NODE_FAILURE = "0";

	private JFrame mainFrame; // the main frame
	private JToolBar tools; // the toolbar

	private JTextField inputFileField; // the field displaying the input file
	private JTextField numberOfNodes; // number of nodes
	private JTextField numberOfReplicas; // number of replicas
	private JTextField nodeFailProb; // probability of node failure field
	private JTextField netFailProb; // probability of network failure field

	private JButton startSimulation; // starts the simulation
	private JButton browseForData; // opens a browsing tab to locate the data input

	private JScrollPane outputArea; // the scroll pane for the console displaying all the logged activity
	private JTextArea console; //console to which the content of the simulation logger is being printed

	private SimulationModel model; // the model that starts the simulation
	private SystemLogger logger; // an instance of the system logger

	//private Server server; //reference to the server

	/**
	 * Default constructor that retrieves an instance of the
	 * {@link SimulationModel} and intialises all the needed components of the
	 * UI, along with action listeners for the buttons
	 */
	public SimulationGUI(SimulationModel model) {

		this.model = model;
		this.logger = SystemLogger.getInstance();

		this.mainFrame = new JFrame();

		tools = new JToolBar(); // creating  toolbar with all the needed tools and buttons

		console = new JTextArea(TEXT_W, TEXT_H); //sets the text dimensions
		console.setEditable(false);
		outputArea = new JScrollPane(console);

		createToolBar();

		mainFrame.add(outputArea, BorderLayout.CENTER);
		mainFrame.setVisible(true);
		mainFrame.setSize(WIDTH, HEIGHT);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//setting defaults balues for fields

		this.inputFileField.setText(INPUT_FILE);
		this.numberOfReplicas.setText(NUM_REPLICAS);
		this.numberOfNodes.setText(NUM_NODES);
		this.nodeFailProb.setText(NODE_FAILURE);
		this.netFailProb.setText(NET_FAILURE);

		this.logger.addObserver(this);

	}

	/*
	 * Convinience function for creating a warning window
	 */
	private void createInfoWindow(String message) {

		Object[] options = { "OK", };
		JOptionPane.showOptionDialog(null, message, "Warning",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[0]);

	}

	/**
	 * Convenience method to avoid the clutter in the default constructor
	 */
	private void createToolBar() {
		// creating text fields
		inputFileField = new JTextField(50);

		//those fields are just for displaying the path
		inputFileField.setEditable(false);

		// those are editable
		numberOfNodes = new JTextField(10);
		numberOfReplicas = new JTextField(10);
		nodeFailProb = new JTextField(10);
		netFailProb = new JTextField(10);

		// Creating all the buttons
		browseForData = new JButton("Browse");
		startSimulation = new JButton("Start");

		//Creating all the labels
		JLabel numNodesLabel = new JLabel("Nodes ");
		JLabel numReplLabel = new JLabel("Replicas ");
		JLabel nodeFailProbLabel = new JLabel("Node Failure ");
		JLabel netFailProbLabel = new JLabel("Packet loss ");

		startSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//button to invoke the start  function on the model
				try {
					// parsing all the necessary fields that will be supplied as parametars to the simulation
					int numNodes = Integer.parseInt(numberOfNodes.getText());
					int numReplicas = Integer.parseInt(numberOfReplicas
							.getText());
					int nodeFailure = Integer.parseInt(nodeFailProb.getText());
					int networkFailure = Integer.parseInt(netFailProb.getText());

					String dataFile = inputFileField.getText();

					// setting all variables of the model
					model.setDataFile(dataFile);
					model.setNetworkFailProb(networkFailure);
					model.setNodeFailProb(nodeFailure);
					model.setNumDuplicates(numReplicas);
					model.setNumNodes(numNodes);

					model.startSimulation();

				} catch (DataException e1) {
					// if an exception is thrown create a window and display message .
					createInfoWindow("Invlaid file");
				}
			}
		});

		browseForData.addActionListener(new ActionListener() {
			/*
			 * action listener to show a file browse dialog and select a file to
			 * serve as the input file
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser(); //ensure that only files can be selected
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int dialogStatus = chooser.showOpenDialog(new JFrame()); //show an open dialog

				if (dialogStatus == JFileChooser.APPROVE_OPTION) {
					String fileName = chooser.getSelectedFile().getPath();
					// update the field holding the path to the file
					inputFileField.setText(fileName);
				}

			}
		});

		// add all the components to the toolbar
		tools.add(numNodesLabel);
		tools.add(numberOfNodes);

		tools.add(numReplLabel);
		tools.add(numberOfReplicas);

		tools.add(nodeFailProbLabel);
		tools.add(nodeFailProb);

		tools.add(netFailProbLabel);
		tools.add(netFailProb);

		//tools.add(rootLabel);
		tools.add(inputFileField);
		tools.add(browseForData);

		tools.add(startSimulation);

		mainFrame.add(tools, BorderLayout.NORTH); //add the tools to the main frame
	}

	/**
	 * Method to update the state of the GUI. This method is used mainly by the
	 * {@link SystemLogger}. When the log function is called, the message that
	 * it logs is appended to the logging buffer. In the end notifyObservers()
	 * is called and the UI is updated with the newly available logging
	 * information
	 * 
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				console.setText(""); // clear the console
				console.setText(logger.getLog()); // print the content of the logger to the console

			}
		});
	}

}
