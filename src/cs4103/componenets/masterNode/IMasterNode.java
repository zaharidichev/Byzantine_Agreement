package cs4103.componenets.masterNode;

import cs4103.componenets.computeNode.IPartialResult;
import cs4103.exceptions.MasterNodeException;

public interface IMasterNode {

	public void StartComputeNodes();

	public void submitResult(IPartialResult result);

	public boolean isReady();

	public int getResult() throws MasterNodeException;

}
