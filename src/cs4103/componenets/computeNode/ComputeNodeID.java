package cs4103.componenets.computeNode;

import cs4103.componenets.masterNode.IMasterNode;

/**
 * This class represents a unique ID of a {@link IComputeNode} object. Each
 * master is {@link IMasterNode} object is responsible for generating this
 * unique id upon creation of an {@link IComputeNode} objects
 * 
 * @author 120010516
 * 
 */
public class ComputeNodeID {
	private String idString;

	/**
	 * Constructor takes as an argument the numeric part of the ID
	 * 
	 * @param numPart
	 */
	public ComputeNodeID(int numPart) {

		//builds the id string
		this.idString = new String("ComputeNode-" + numPart);
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
		ComputeNodeID other = (ComputeNodeID) obj;
		if (this.hashCode() != other.hashCode()) {
			return false;
		}

		return true;
	}

}
