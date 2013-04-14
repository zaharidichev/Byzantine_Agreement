package cs4103.componenets.computeNode;

public interface IComputeNode {

	public boolean sendResultToMasterNode(IPartialResult result);

	public void start();
	
	public IPartialResult getResult();

}
