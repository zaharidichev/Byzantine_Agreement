public class ComputeNodeID {

	private String idString;

	public ComputeNodeID(int numPart) {
		this.idString = new String("ComputeNode-" + numPart);
	}

	@Override
	public int hashCode() {
		return this.idString.hashCode();
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
