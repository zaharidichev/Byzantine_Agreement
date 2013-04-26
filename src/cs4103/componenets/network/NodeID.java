package cs4103.componenets.network;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.types.NodeType;

/**
 * This class represents a unique ID of a {@link IComputeNode} object. Each
 * master is {@link IMasterNode} object is responsible for generating this
 * unique id upon creation of an {@link IComputeNode} objects
 * 
 * @author 120010516
 * 
 */
public class NodeID {
	private String idString; // the textual representation
	private NodeType type; // the type of the node
	private int numPart;

	/**
	 * Constructor takes as arguments the numeric part of the id as well as the
	 * {@link NodeType} of the node
	 * 
	 * @param type
	 *            the {@link NodeType} enum
	 * @param numPart
	 *            the numeric part
	 */
	public NodeID(NodeType type, int numPart) {

		//builds the id string
		this.type = type;
		this.idString = new String(this.type + "-" + numPart);
		this.numPart = numPart;

	}

	public int getNumPart() {
		return this.numPart;
	}

	/**
	 * Used if the type of the node is needed
	 * 
	 * @return a {@link NodeType} value
	 */
	public NodeType getType() {
		return this.type;
	}

	// All the rest is pretty clear 
	@Override
	public int hashCode() {
		return this.idString.hashCode(); // we only care about the has of the String 
	}

	@Override
	public String toString() {
		return this.idString;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeID other = (NodeID) obj;
		if (this.hashCode() != other.hashCode()) {
			return false;
		}

		return true;
	}

}
