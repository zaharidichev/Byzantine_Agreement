package cs4103.utils;

import java.util.LinkedList;

public interface IWorkEntity {

	public LinkedList<Integer> getPieceOfWork();

	public int workLeft();

	public boolean thereIsWork();

}
