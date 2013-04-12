package cs4103.componenets.computeNode.factories;

import java.util.LinkedList;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.masterNode.IMasterNode;

public interface IComputeNodeFactory {

	public IComputeNode getSumComputeNode(LinkedList<Integer> data, int ID,
			IMasterNode parent);

}
