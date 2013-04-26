package cs4103.componenets.computeNode.factories;

import cs4103.componenets.computeNode.ComputeNode;
import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.network.NodeID;

/**
 * An implementation of the {@link ComputeNodeFactory} interface. The purpose of
 * this class is to abstract away the creation of {@link IComputeNode} objects.
 * It is useful when a dependency injection needs to be made into the master
 * node. This way different implementations of {@link IComputeNodeFactory}
 * interface can be used by the {@link IMasterNode}
 * 
 * @author 120010516
 * 
 */
public class ComputeNodeFactory implements IComputeNodeFactory {

	/**
	 * Default constructor used to create the object
	 */
	public ComputeNodeFactory() {

	}

	@Override
	public IComputeNode getSumComputeNode(int id, NodeID master) {

		return new ComputeNode(id, master);
	}
}
