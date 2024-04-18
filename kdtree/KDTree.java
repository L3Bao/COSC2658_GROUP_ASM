package kdtree;

import java.util.Scanner;

public class KDTree {
    Node root = null;
    int dimension;

    public KDTree(int dimension) {
        this.dimension = dimension;
        this.root = null;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int dim = 2;
        KDTree tree = new KDTree(dim);

        int option = -1;
        while (option != 5) {
            System.out.println("Select operation:");
            System.out.println("1. Add a Record.");
            System.out.println("2. Delete a Record");
            System.out.println("3. Search for a Record");
            System.out.println("4. Display the tree");
            System.out.println("5. Exit");

            option = sc.nextInt();

            switch (option) {
                case 1 -> {
                    int[] arr = new int[dim];
                    for (int i = 0; i < dim; i++) {
                        System.out.print("Enter value for dimension " + (i + 1) + ": ");
                        arr[i] = sc.nextInt();
                    }
                    tree.insertRecursive(arr);
                    tree.display();
                }
                case 2 -> {
                    int[] arr = new int[dim];
                    for (int i = 0; i < dim; i++) {
                        System.out.print("Enter value for dimension " + (i + 1) + ": ");
                        arr[i] = sc.nextInt();
                    }
                    tree.deleteRecursive(arr);
                    tree.display();
                }
                case 3 -> {
                    int[] arr = new int[dim];
                    System.out.println("Enter point values to search:");
                    for (int i = 0; i < dim; i++) {
                        System.out.print("Value for dimension " + (i + 1) + ": ");
                        arr[i] = sc.nextInt();
                    }
                    Node foundNode = tree.searchRecursive(arr);
                    if (foundNode != null) {
                        System.out.println("Point found in tree: " + foundNode);
                    } else {
                        System.out.println("Point not found in tree.");
                    }
                }
                case 4 -> tree.display();
                case 5 -> {
                    // Explicitly handling the exit case to break the loop
                    System.out.println("Exiting...");
                    break;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
        sc.close();
    }

    private void insertRecursive(int[] arr) {
		root = insert(arr, root, 0);
	}

    private Node insert(int[] arr, Node node, int dim) {
        if (node == null) {
            return new Node(0, arr);
        }

        if (node.values[dim] > arr[dim]) {
            node.left = insert(arr, node.left, (dim+1) % dimension);
        } else {
            node.right = insert(arr, node.right, (dim+1) % dimension);
        }

        return node;
    }

    private void deleteRecursive(int[] point) {
		root = delete(root, point, 0);
	}

    private Node delete(Node currentNode, int[] targetPoint, int depth) {
        if (currentNode == null) {
            return null;
        }

        // Calculate current dimension to compare based on the depth in the tree
        int currentDimension = depth % dimension;


        // Check if the current node holds the target point
        if (arePointsSame(currentNode.values, targetPoint)) {
            // Node with two children
            if (currentNode.right != null) {
                // Find the minimum node on the right subtree in the current dimension
                Node min = findMin(currentNode.right, currentDimension);
                // Replace current node with the minimum node from the right subtree
                copyPoint(currentNode, min);
                // Recursively delete the minimum node from the right subtree
                currentNode.right = delete(currentNode.right, min.values, depth + 1);
            } else if (currentNode.left != null) {
                // If there's no right child, promote the left subtree
                Node min = findMin(currentNode.left, currentDimension);
                copyPoint(currentNode, min);
                // Recursively delete the minimum node from the left subtree
                currentNode.right = delete(currentNode.left, min.values, depth + 1);
                currentNode.left = null;
            } else {
                // Node is a leaf
                return null;
            }
            return currentNode;
        }

        // Recursive case: Traverse down the tree
        if (targetPoint[currentDimension] < currentNode.values[currentDimension]) {
            // Target point is less than current node in the current dimension
            currentNode.left = delete(currentNode.left, targetPoint, depth + 1);
        } else {
            // Target point is greater than or equal to current node in the current dimension
            currentNode.right = delete(currentNode.right, targetPoint, depth + 1);
        }

        return currentNode;
    }

    private Node searchRecursive(int[] target) {
        return search(root, target, 0);
    }
    
    private Node search(Node node, int[] target, int depth) {
        if (node == null) {
            return null; // Base case: node is null, target not found
        }

        int currentDimension = depth % dimension;

        // Check if current node's point matches the target point
        if (arePointsSame(node.values, target)) {
            return node; // Target point found
        }

        if (target[currentDimension] < node.values[currentDimension]) {
            return search(node.left, target, depth + 1);
        } else {
            return search(node.right, target, depth + 1);
        }
    }

    private boolean arePointsSame(int[] point1, int[] point2) {
        if (point1.length != point2.length) {
            return false;
        }

		for (int i = 0; i < dimension; ++i)
			if (point1[i] != point2[i])
				return false;
		return true;
	}

    private void copyPoint(Node a, Node b) {
		for (int i = 0; i < dimension; i++)
			a.values[i] = b.values[i];
	}

    private Node findMin(Node node, int d) {
		return findMinRec(node, d, 0);
	}

    private Node findMinRec(Node node, int d, int depth) {
		if (node == null)
			return null;
		
		int currentDimension = depth % dimension;

		if (currentDimension == d) {
			if (node.left == null)
				return node;
			return findMinRec(node.left, d, depth+1);
		}

		return minNode(node, findMinRec(node.left, d, depth+1), findMinRec(node.right, d, depth+1), d);
	}

    private Node minNode(Node x, Node y, Node z, int d) {
		Node res = x;
		if (y != null && y.values[d] < res.values[d])
		res = y;
		if (z != null && z.values[d] < res.values[d])
		res = z;
		return res;
	}

	private void display() {
		System.out.println(root);
	}
}
