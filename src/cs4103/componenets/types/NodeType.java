package cs4103.componenets.types;

/**
 * Enum that can express the different types of nodes <li>MASTER - this type of
 * node is effectively responsible for assigning work to the slave nodes and
 * retrieving the results</li> <li>SLAVE - it is assigned work by the masters
 * and is responsible for returning the result upon request</li>
 * 
 * @author 120010516
 * 
 */
public enum NodeType {

	MASTER, SLAVE

}
