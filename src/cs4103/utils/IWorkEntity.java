package cs4103.utils;

import java.util.LinkedList;

/**
 * An interface that defines the behavior of work entities. The purpose of work
 * entities is to serve as an abstraction that takes care of the separation of
 * an amount of work into smaller pieces to be distributed among computing
 * entities (e.g. nodes,threads cores). Note that this interface does not need
 * to be used by classes that distribute the work in any predefined manner. THe
 * details of the distribution and the factors that are taken when deciding its
 * nature are completely up to the implementor
 * 
 * @author 120010516
 * 
 */

public interface IWorkEntity {

	/**
	 * A method that provides a piece of work from the entity. Usually this
	 * method is called by a component that needs the work in order to assign it
	 * to a computing node (e.g. a Job Broker system)
	 * 
	 * @return {@link LinkedList} of integers
	 */
	public LinkedList<Integer> getPieceOfWork();

	/**
	 * This method returns the number of integers that are left in the work
	 * entity
	 * 
	 * @return the number of integers left
	 */
	public int workLeft();

	/**
	 * The method should be used in order to check whether there is any work
	 * left in the entity (any integers)
	 * 
	 * @return {@link Boolean} yes or no
	 */
	public boolean thereIsWork();

}
