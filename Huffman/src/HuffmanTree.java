//Andrew Yu
//Raymond Webster Berry
//HW8 - Huffman
//Huffman is a program that compresses file size by sorting words based on how often
//they're used, that way the program runs more efficiently by making access to more used 
//words much easier at the costs of making the less used words slightly harder to access. 
import java.util.*;
import java.io.*;

public class HuffmanTree {
	// A queue that can hold the nodes that represent
	// each character
	private Queue<HuffmanNode> values;
	// HuffmanNode root of the binary tree
	private HuffmanNode overallRoot;

	// Constructor for HuffmanTree that gets the count and the frequency that a 
	// character shows up, and then orders them into a binary tree based on their frequency
	// higher frequency's Node at top of tree, lower frequency Nodes at bottom
	//	Parameters:
	//int[] count - where constructor "gets" the frequencies and char values to organize the tree
	public HuffmanTree(int[] count) {
		values = new PriorityQueue<HuffmanNode>();
		int length = count.length;
		for (int loopIndex = 0; loopIndex < count.length; loopIndex++) {
			if (count[loopIndex] > 0) {
				HuffmanNode newNode = new HuffmanNode(count[loopIndex], loopIndex);
				values.add(newNode);
			}
		}
		HuffmanNode endChar = new HuffmanNode(1, length);
		values.add(endChar);
		overallRoot = buildTree();
	}
	
	//Builds a binary tree using the many nodes from constructor
	private HuffmanNode buildTree() {
		while (values.size() != 1) {
			HuffmanNode node1 = values.remove();
			HuffmanNode node2 = values.remove();
			int nodeFrequency = node1.frequency + node2.frequency;
			HuffmanNode updatedNode = new HuffmanNode(nodeFrequency, -1, node1, node2);
			values.add(updatedNode);
		}
		return values.remove();
	}
	
	//Writes to a file in the format of the char's integer representation on one line 
	//the char's frequency on the next line. Repeats this for all the char's. Ordered by the
	//smallest char's integer reprsentations first. 
	//PrintStream output - the printStream object that the user uses to write Strings to the file
	public void write(PrintStream output) {
		String treeIndex = "";
		writeHelper(overallRoot, output, treeIndex);
	}
	
	//Helps to write to file (see write method for description)
	//Parameters: 
	//	HuffmanNode overallRoot - the root to start at when parsing thru tree
	//	PrintStream output - to write to the output file
	// 	String treeIndex - the string that tracks values while traversing the tree
	public void writeHelper(HuffmanNode overallRoot, PrintStream output, String treeIndex) {
		// if leaf node
		if (overallRoot.left == null && overallRoot.right == null) {
			output.println(overallRoot.charValue);
			output.println(treeIndex);
		} else {
			writeHelper(overallRoot.left, output, treeIndex + "0");
			writeHelper(overallRoot.right, output, treeIndex + "1");
		}
	}
	
	//Constructs a tree of HuffmanNodes that takes information from
	//a Scanner input file and constructs a tree based on that information
	//will read in the same order I mentioned before, the char's int represntattion
	//followed by the "code"
	//Scanner input - the file read in to create the tree
	public HuffmanTree(Scanner input) {
		overallRoot = new HuffmanNode(0, 0);
		while (input.hasNextLine()) {
			overallRoot = inputTree(overallRoot, input);
		}
	}
	
	//helps to write a tree based on information given from a scanner object
	//HuffmanNode root - the starting root during tree traversal 
	//Scanner input - the Scanner object to read in 2 lines at a time to construct tree
	private HuffmanNode inputTree(HuffmanNode root, Scanner input) {
		int n = Integer.parseInt(input.nextLine());
		String code = input.nextLine();
		HuffmanNode leaf = new HuffmanNode(0, n);
		HuffmanNode parent = root;
		for (int i = 0; i < code.length() - 1; i++) {
			if (code.charAt(i) == '0') {
				if (parent.left == null) {
					parent.left = new HuffmanNode(0, 0);
				}
				parent = parent.left;
			} else {
				if (parent.right == null) {
					parent.right = new HuffmanNode(0, 0);
				}
				parent = parent.right;
			}
		}
		if (code.charAt(code.length() - 1) == '0') {
			parent.left = leaf;
		} else {
			parent.right = leaf;
		}
		return root;
	}
	
	//reads a series of "bits" from a give input stream and writes those bits into 
	//characters people can read (chars) to the output stream.
	//BitInputStream input - where the bits are being read in from 
	//PrintStream output - where the translated chars are being sent to
	//int eof - the stopping character, aka when the program stops reading
	public void decode(BitInputStream input, PrintStream output, int eof) {

		boolean keepGoing = true;
		while (keepGoing) {
			keepGoing = decodeHelper(input, output, eof, overallRoot);
		}
	}
	
	//Helps to read through the series of bits 
	//in order to translate them into chars that people can read
	//Parameters: 
	// (see above for the parameters, they're the same)
	//HuffmanNode root - the overallRoot of the tree 
	private boolean decodeHelper(BitInputStream input, PrintStream output, int eof, HuffmanNode root) {
		int bit = input.readBit();
		if (bit < 0) {
			return false;
		}
		HuffmanNode checkNode = null;
		if (bit == 0) {
			checkNode = root.left;
		} else {
			checkNode = root.right;
		}
		if (checkNode.left == null && checkNode.right == null) {
			int charValue = checkNode.charValue;
			if (charValue == eof) {
				return false;
			}
			output.write(charValue);
		} else {
			decodeHelper(input, output, eof, checkNode);
		}
		return true;
	}
}