package cs4103.GUI;

import java.util.Observable;

import cs4103.componenets.computeNode.factories.ComputeNodeFactory;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.componenets.network.NodeID;
import cs4103.componenets.types.NodeType;
import cs4103.exceptions.DataException;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.io.IReader;
import cs4103.utils.io.Reader;
import cs4103.utils.logger.Log;
import cs4103.utils.logger.SystemLogger;
import cs4103.utils.misc.ProbabilityGenerator;

public class SimulationModel extends Observable {

	private int networkFailProb;
	private int nodeFailProb;
	private String dataFile;
	private int numNodes;
	private int numDuplicates;
	private MasterNode m;

	public SimulationModel() {

		this.networkFailProb = 0;
		this.nodeFailProb = 0;
		this.dataFile = "";
		this.numNodes = 0;
		this.numDuplicates = 0;
	}

	public void setDataFile(String s) {
		this.dataFile = s;
	}

	public void setNodeFailProb(int p) {
		this.nodeFailProb = p;
	}

	public void setNetworkFailProb(int p) {
		this.networkFailProb = p;
	}

	public void setNumNodes(int n) {
		this.numNodes = n;
	}

	public void setNumDuplicates(int d) {
		this.numDuplicates = d;
	}

	public boolean done() {

		if (m != null) {
			try {
				return this.m.checkIfDone();
			} catch (MasterNodeException e) {
				SystemLogger.getInstance().log(e.getMessage(), Log.PROBLEM);
			}

		}

		return false;
	}

	public int getResult() throws MasterNodeException {
		return this.m.getResult();
	}

	public void startSimulation() throws DataException {
		final SystemLogger logger = SystemLogger.getInstance();
		final IReader reader = new Reader(this.dataFile);
		ProbabilityGenerator.setProbabilityOfComissionError(this.nodeFailProb);
		ProbabilityGenerator.setProbabilityOfNetworkError(this.networkFailProb);

		Thread runner = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					m = new MasterNode(new ComputeNodeFactory(), reader,
							new NodeID(NodeType.MASTER, 1), numNodes,
							numDuplicates);
					m.start();

					while (!m.checkIfDone()) {
						// blocks untill the master is done
						Thread.sleep(500);
					}

					int result = m.getResult();
					logger.logToAll("Result is : " + result);
				} catch (DataException e) {
					logger.log(e.getMessage(), Log.PROBLEM);
				} catch (MasterNodeException e) {
					logger.log(e.getMessage(), Log.PROBLEM);

				} catch (InterruptedException e) {
				}

			}
		});
		runner.start();

	}
}
