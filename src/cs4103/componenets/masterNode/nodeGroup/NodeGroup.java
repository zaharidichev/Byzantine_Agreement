package cs4103.componenets.masterNode.nodeGroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import cs4103.componenets.network.NodeID;

/**
 * An instance of this class is used to store the results of the computations of
 * a number of nodes that are working on the same problem. The purpose of this
 * class is to provide functionality that enables the client to determine
 * whether the nodes have agreed on a result.
 * 
 * @author 120010516
 * 
 */
public class NodeGroup {

	private HashSet<NodeID> members; // the set of separate nodes
	/*
	 * The result histogram is used to store key : pair values where key is the
	 * result that has been received and the value is the amount of times this
	 * result has been produced by a slave node member of this group
	 */
	private HashMap<Integer, Integer> resHistogram;

	private int agreedResult = 0; // the agreed result
	private int totalReceived = 0; // total results received
	private int sizeOfGroup; // the size of the group

	/**
	 * Constructs an empty node group
	 */
	public NodeGroup() {
		this.members = new HashSet<NodeID>();
		this.resHistogram = new HashMap<Integer, Integer>();
		this.sizeOfGroup = 0;
	}

	/**
	 * Joins a node to the group
	 * 
	 * @param node
	 *            {@link NodeID}
	 */
	public void joinNode(NodeID node) {
		members.add(node);
		this.sizeOfGroup++; // increment the size variable
	}

	/**
	 * Removes a node from the group. Used when a node needs to be excluded from
	 * the computation due to network or node failure
	 * 
	 * @param node
	 */
	public void removeNode(NodeID node) {
		this.members.remove(node);

	}

	/**
	 * Checks whether a particular node belongs to the group
	 * 
	 * @param node
	 *            {@link NodeID}
	 * @return {@link Boolean}
	 */
	public boolean belongs(NodeID node) {
		return this.members.contains(node);
	}

	/**
	 * Adds a newly received result to the result histogram
	 * 
	 * @param r
	 *            {@link Integer}
	 */

	public void addResult(int r) {

		int count = resHistogram.containsKey(r) ? resHistogram.get(r) : 0;
		resHistogram.put(r, count + 1); // if already in the map, increment its value
		this.totalReceived++;

	}

	/**
	 * The node returns the {@link GroupState} value of the group. The group can
	 * either be undecided or decided. If the group is FAILED though, there is
	 * no way of recovering. Failure may be caused due to too many nodes that
	 * belong of the group failing or producing separate results
	 * 
	 * @return Boolean
	 */
	public GroupState getGroupState() {

		//int numMembers = this.members.size();

		/*
		 * The rule for agreement is that, we need at least 50% + 1 of the
		 * results to be the same in order to reach an agreement
		 */
		int agreementNeeded = (this.sizeOfGroup / 2) + 1;

		///////////////////////////////////////////////////////////
		if (this.resHistogram.size() > agreementNeeded
				|| this.members.size() < agreementNeeded) {

			/*
			 * Therefore, if the size of the histogram is larger than the
			 * results that need to be the same in order to reach an agreement,
			 * an agreement cannot be reached at all since the remaining nodes
			 * cannot compensate for the already produced deferring results.
			 * Moreover , if the size of the group has decreased to a size which
			 * value is smaller than the value of the matching results needed
			 * for agreement, the group again FAILS to reach it, since there is
			 * no way for enough results to be accumulate in order to achieve a
			 * state of agreement.
			 */
			return GroupState.FAILED;
		}

		/*
		 * If the group has not failed, we iterate through all of its received
		 * results and check whether the frequency of one result matches the
		 * amount needed for agreement. If this is the case, the group is in a
		 * DECIDED state
		 */
		for (Entry<Integer, Integer> ent : this.resHistogram.entrySet()) {
			int frecuency = ent.getValue();

			if (frecuency >= agreementNeeded) {
				this.agreedResult = ent.getKey();
				return GroupState.DECIDED;
			}
		}

		/*
		 * if (this.totalReceived == this.members.size()) {
		 * 
		 * Again checking the last condition.If the result histogram size is the
		 * same number as the total received result, it means that each result
		 * differs from
		 * 
		 * return GroupState.FAILED; }
		 */

		return GroupState.UNDECIDED;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Members [");

		for (NodeID id : this.members) {
			sb.append(id + " ");
		}

		sb.append("] Results [");
		for (Entry<Integer, Integer> ent : this.resHistogram.entrySet()) {
			sb.append(" {R: " + ent.getKey() + " O: " + ent.getValue() + "} ");
		}
		sb.append("] State [" + this.getGroupState() + "]");
		return sb.toString();

	}

	public int getResult() {
		return this.agreedResult;
	}

	public HashSet<NodeID> getNodeAddresses() {
		return this.members;
	}
}
