import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

// This is a starter file that includes the read9/write9 methods described in
// the bonus assignment writeup.

public class HuffmanTree2 {
	// A queue that can hold the nodes that represent
	// each character
	private Queue<HuffmanNode> values;
	// HuffmanNode root of the binary tree
	private HuffmanNode overallRoot;

	// Constructor for HuffmanTree that gets the count and the frequency that a
	// character shows up, and then orders them into a binary tree based on their
	// frequency
	// higher frequency's Node at top of tree, lower frequency Nodes at bottom
	// Parameters:
	// int[] count - where constructor "gets" the frequencies and char values to
	// organize the tree
	public HuffmanTree2(int[] count) {
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
	
	//alternative constructor for making a huffman tree using a input stream
	public HuffmanTree2(BitInputStream input) {
		overallRoot = new HuffmanNode(0, 0);
		overallRoot = readHeaderHelper(overallRoot, input);
	}
	
	//helps read the huffmanTree using the given root and the give input stream 
	private HuffmanNode readHeaderHelper(HuffmanNode root, BitInputStream input) {
		
		int bit = input.readBit();
		if(bit == 0) {
			root.left = new HuffmanNode(0,  0);
			root.left = readHeaderHelper(root.left, input);
			root.right = new HuffmanNode(0,  0);
			root.right = readHeaderHelper(root.right, input);
		}
		else {
			return new HuffmanNode(0,  read9(input));
		}
		return root;
	}
	
	//assigns a tree's code to string array, codes 
	public void assign(String[] codes) {
		String index = "";
		assignHelper(overallRoot, index, codes);
	}
	
	//heklper to do the recursive portion of assigning a trees code to string array 
	private void assignHelper(HuffmanNode root, String index, String[] codes) {
		if(root.left == null && root.right == null) {
			codes[root.charValue] = index ;
		}
		else { 
			assignHelper(root.left, index + "0", codes);
			assignHelper(root.right, index + "1", codes);
		}
	}
	
	//writes out the tree as a file header to the output stream
	public void writeHeader(BitOutputStream output) {
		String index = "";
		headerWriteHelper(output,overallRoot, index);
	}
	
	//Does the recursive portion traversing through the tree to write out the code 
	private void headerWriteHelper(BitOutputStream output, HuffmanNode root, String index) { 
		if(root.left == null && root.right == null) {
			output.writeBit(1);
			write9(output, root.charValue);
		}
		else {
			output.writeBit(0);
			headerWriteHelper(output, root.left, index + "0");
			headerWriteHelper(output, root.right, index + "1");
		}
	}
	// Builds a binary tree using the many nodes from constructor
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

	// Writes to a file in the format of the char's integer representation on one
	// line
	// the char's frequency on the next line. Repeats this for all the char's.
	// Ordered by the
	// smallest char's integer reprsentations first.
	// PrintStream output - the printStream object that the user uses to write
	// Strings to the file
	public void write(PrintStream output) {
		String treeIndex = "";
		writeHelper(overallRoot, output, treeIndex);
	}

	// Helps to write to file (see write method for description)
	// Parameters:
	// HuffmanNode overallRoot - the root to start at when parsing thru tree
	// PrintStream output - to write to the output file
	// String treeIndex - the string that tracks values while traversing the tree
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

	// Constructs a tree of HuffmanNodes that takes information from
	// a Scanner input file and constructs a tree based on that information
	// will read in the same order I mentioned before, the char's int represntattion
	// followed by the "code"
	// Scanner input - the file read in to create the tree
	public HuffmanTree2(Scanner input) {
			overallRoot = new HuffmanNode(0, 0);
			while (input.hasNextLine()) {
				overallRoot = inputTree(overallRoot, input);
			}
		}

	// helps to write a tree based on information given from a scanner object
	// HuffmanNode root - the starting root during tree traversal
	// Scanner input - the Scanner object to read in 2 lines at a time to construct
	// tree
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

	// reads a series of "bits" from a give input stream and writes those bits into
	// characters people can read (chars) to the output stream.
	// BitInputStream input - where the bits are being read in from
	// PrintStream output - where the translated chars are being sent to
	// int eof - the stopping character, aka when the program stops reading
	public void decode(BitInputStream input, PrintStream output, int eof) {

		boolean keepGoing = true;
		while (keepGoing) {
			keepGoing = decodeHelper(input, output, eof, overallRoot);
		}
	}

	// Helps to read through the series of bits
	// in order to translate them into chars that people can read
	// Parameters:
	// (see above for the parameters, they're the same)
	// HuffmanNode root - the overallRoot of the tree
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
	
	
	// pre : an integer n has been encoded using write9 or its equivalent
	// post: reads 9 bits to reconstruct the original integer
	private int read9(BitInputStream input) {
		int multiplier = 1;
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += multiplier * input.readBit();
			multiplier = multiplier * 2;
		}
		return sum;
	}

	// pre : 0 <= n < 512
	// post: writes a 9-bit representation of n to the given output stream
	private void write9(BitOutputStream output, int n) {
		for (int i = 0; i < 9; i++) {
			output.writeBit(n % 2);
			n = n / 2;
		}
	}
}
