package cs4103.componenets.computeNode;

import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.network.IAddressable;

/**
 * This is an interface to the implementation of compute nodes that are supposed
 * to complete an {@link IPartialResult} object and deliver it to their
 * {@link IMasterNode} parent
 * 
 * @author 120010516
 * 
 */
public interface IComputeNode extends IAddressable {

	public void start();

	public void demonise();

}
