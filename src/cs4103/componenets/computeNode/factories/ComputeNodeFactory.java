package cs4103.componenets.computeNode.factories;

import java.util.LinkedList;

import cs4103.componenets.computeNode.ComputeNode;
import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.masterNode.IMasterNode;

public class ComputeNodeFactory implements IComputeNodeFactory {

	public ComputeNodeFactory() {

	}

	public IComputeNode getSumComputeNode(LinkedList<Integer> data, int ID,
			IMasterNode parent) {
		return new ComputeNode(data, ID, parent);
	}
}
