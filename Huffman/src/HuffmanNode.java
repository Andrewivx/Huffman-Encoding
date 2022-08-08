//Andrew Yu
//TA: Raymond Webster Berry
//Homework 8 
//HuffmanNode - constructs a single node 
//to be used later in the tree of the program 
//each node contains the character's ascii value 
//the frequency it occurs, and has two possible links
public class HuffmanNode implements Comparable<HuffmanNode>{
	//the frequency of that the char shows up
	public int frequency;
	//the char value of the char (integer representation)
	public int charValue;
	//the right pointing node
	public HuffmanNode right;
	//the left pointing node
	public HuffmanNode left;
	
	//Constructs a HuffmanNode with a frequency and charValue
	//but doesn't point to any other nodes 
	//Parameters:
	//	int Frequency - the frequency the char appears
	//	int charValue - the integer representation of the char
	public HuffmanNode(int frequency, int charValue) {
		this(frequency, charValue, null, null);
	}
	
	//Constructs a HuffmanNode with a frequency and charValue
	//that points to both a leftNode and A right Node
	//Parameters:
	//	int Frequency - the frequency the char appears
	//	int charValue - the integer representation of the char
	//	HuffmanNode left - the huffmanNode the left branch goes to
	//	HuffmanNode right - the huffmanNode the right branch goes to
	public HuffmanNode(int frequency, int charValue, HuffmanNode left, HuffmanNode right) {
		this.frequency = frequency;
		this.charValue = charValue;
		this.right = right;
		this.left = left;
	}
	
	@Override
	//Compares 2 differnet HuffmanNodes and returns positive
	//or negative values based on whichever is bigger
	//(this bigger = positive, aNode bigger = negative) 
	public int compareTo(HuffmanNode aNode) {
		return this.frequency - aNode.frequency;
	}
}
