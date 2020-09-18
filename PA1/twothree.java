/*
Algorithm programming assignment 1
09/2020
*/

import java.io.*;
import java.util.*;


class Node {
   String guide;
   // guide poeints to max key in subtree rooted at node
}

class InternalNode extends Node {
   Node child0, child1, child2;
   // child0 and child1 are always non-null
   // child2 is null iff node has only 2 children
}

class LeafNode extends Node {
   // guide points to the key

   int value;
}

class TwoThreeTree {
   Node root;
   int height;

   TwoThreeTree() {
      root = null;
      height = -1;
   }
}

class WorkSpace {	
// this class is used to hold return values for the recursive doInsert
// routine (see below)

   Node newNode;
   int offset;
   boolean guideChanged;
   Node[] scratch;
}

public class twothree {

	public static void main(String[] args)throws Exception {
	TwoThreeTree tree = new TwoThreeTree();
	//scan the input	
	Scanner scanner = new Scanner(System.in);
	int n = scanner.nextInt();
	for(int i = 0; i < n; i++){
	 	//creat new node and insert node
		String planet = scanner.next();
		int fee = scanner.nextInt();
		insert(planet, fee, tree);
	}
	//printAll(tree.root, tree.height);
	
	//System.out.println('\n' +"height: "+ tree.height);
	
	//queries 
	int m =scanner.nextInt();
	String[] X = new String[m];
	String[] Y = new String[m];
	for(int j = 0; j < m; j++){
	//call printRange function
		String x = scanner.next();
		String y = scanner.next();
		X[j] = x;
		Y[j] = y;
	}
	for(int j = 0; j < m; j++){
		printRange(tree,X[j],Y[j]);
	}
  	 }

//printRange(x,y) function, call printAll() internally
//x is not necessarily smaller than y


   static void printRange(TwoThreeTree tree, String x, String y) throws Exception {

	int h = tree.height;
	//System.out.print("height in printRange: "+h+'\n');
	//check empty tree
	if(h == -1) return;
	
	//compare x and y
	if(x.compareTo(y) == 0){
		InternalNode[] path = search(tree, x);
		LeafNode kid0 = (LeafNode) path[h-1].child0;
		LeafNode kid1 = (LeafNode) path[h-1].child1;
		if(kid0.guide.compareTo(x) == 0) {System.out.println(kid0.guide + ' '+Integer.toString(kid0.value));
							return;}
		else if(kid1.guide.compareTo(x) == 0){System.out.println(kid1.guide + ' '+Integer.toString(kid1.value)); return;}
		
	        else if(path[h-1].child2 != null){
			LeafNode kid2 = (LeafNode) path[h-1].child2;
			if(kid2.guide.compareTo(x) == 0) { 
				System.out.println(kid2.guide + ' '+Integer.toString(kid2.value)); return;}
			}
		else{ return;}
		}
	
	String small; 
	String large;

	if(x.compareTo(y) > 0){ //x > y 
			small = y; large= x; } 
	else{	
		small = x;large = y;}

	//only one node: root
	if( h == 0) {
		LeafNode node = (LeafNode) tree.root;
		if((node.guide.compareTo(small) >= 0)&& (node.guide.compareTo(large) <= 0)){
			System.out.println(node.guide + ' ' + Integer.toString(node.value) + '\n' );}
		}
	//search two limits, return two Node[] with the same sizes
	InternalNode[] left = search(tree, small);
	InternalNode[] right = search(tree, large);
	int divergepoint = divergePoint(left, right);
	
	if( divergepoint >= 0){ 
	// left route
	InternalNode leftmost = (InternalNode) left[h-1];
	if(leftmost.child0.guide.compareTo(small) >= 0) printAll(leftmost.child0, 0);
	if(leftmost.child1.guide.compareTo(small) >= 0) printAll(leftmost.child1, 0); 
	if(leftmost.child2 != null){
		if(leftmost.child2.guide.compareTo(small) >= 0 ){
			printAll(leftmost.child2, 0);}	
		}
	for(int i = h-2 ; i > divergepoint-1; i--){
		InternalNode c0 = (InternalNode) left[i].child0;	
		InternalNode c1 = (InternalNode) left[i].child1;
		if(left[i].child2 != null ){
			InternalNode c2 = (InternalNode) left[i].child2;
			if(left[i+1].equals(c0)){
					printAll(c1, h-i-1);
					printAll(c2, h-i-1);}
			else if(left[i+1].equals(c1)){
					printAll(c2, h-i-1);}
			}
		else{//child2 NOT exist
			if(left[i+1].equals(c0)){
					printAll(c1, h-i-1);}
				}					
	}
	//process the diverge point
  	InternalNode diverge_point = (InternalNode) left[divergepoint-1];
	InternalNode divergechild0 = (InternalNode) diverge_point.child0;
	InternalNode divergenode = (InternalNode) diverge_point.child1;
	//middle child exists                               
	if(diverge_point.child2 != null){	
		InternalNode divergechild2 = (InternalNode) diverge_point.child2;
	 if((left[divergepoint].equals(divergechild0))&&(right[divergepoint].equals(divergechild2))){        int hh = h - divergepoint; //2
				printAll(divergenode, hh);
		}
			}
		

	//right route
	for(int j = divergepoint; j < h-1; j++ ){
		InternalNode c0 = (InternalNode) right[j].child0;
		InternalNode c1 = (InternalNode) right[j].child1;
		if(right[j].child2 != null){// child2 exits and go down the child2, print child0 and child1
			InternalNode c2 = (InternalNode) right[j].child2;
			if(right[j+1].equals(c2)){
					printAll(c0, h-j-1);
					printAll(c1, h-j-1);}
			if(right[j+1].equals(c1)){
					printAll(c0,h-j-1);}
			}
		else if(right[j+1].equals(c1)){
					printAll(c0, h-j-1);}
		}
		//rightmost boundary case
	InternalNode rightmost = (InternalNode) right[h-1];
	if(rightmost.child0.guide.compareTo(large) <= 0) printAll(rightmost.child0, 0);
	if(rightmost.child1.guide.compareTo(large) <= 0) printAll(rightmost.child1, 0); 

	if(rightmost.child2 != null){
		if(rightmost.child2.guide.compareTo(large) <= 0 ){
			printAll(rightmost.child2, 0);}	
		}
	
		}

	else{ // -1, NOT Diverge, includes x = y
		InternalNode finalNode = (InternalNode)left[h-1];
		if((finalNode.child0.guide.compareTo(small) >=0 )&&(finalNode.child0.guide.compareTo(large) <= 0 )){
					printAll(finalNode.child0,0);}
		if((finalNode.child1.guide.compareTo(small) >=0 )&&(finalNode.child1.guide.compareTo(large) <= 0 )){
					printAll(finalNode.child1,0); }
		if(finalNode.child2 != null){
		
			if((finalNode.child2.guide.compareTo(small) >=0 )&&(finalNode.child2.guide.compareTo(large) <= 0 ))
					{printAll(finalNode.child2,0);}
			}
		}
	output.flush();
	}



//printAll() function
   static void printAll(Node n, int h) throws Exception {
	//BufferedWriter
	BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);	

	//assume the input h is the reversed height, ie. h of the 2-3tree  - h of that node
	// when h = 0, then we reach the leaves, print node's guide
	if(h == 0){
			LeafNode leaf = (LeafNode) n;
			output.write(leaf.guide + ' ' + Integer.toString(leaf.value) + '\n' );
			}
	else{ //not leaves, do recursion
		h--;	
		InternalNode nnn = (InternalNode) n;
		printAll(nnn.child0, h);
		printAll(nnn.child1, h);				
		if(nnn.child2 != null){
			printAll(nnn.child2, h);
			}
		}
		output.flush();
	}



//empty tree: root = null, height = -1
//only root:  root = xxx, height = 0 
   static InternalNode[] search(TwoThreeTree tree, String key){
	int h = tree.height;
	//Node array: track the path, i stop at the h-1 level
	InternalNode[] path = new InternalNode[h];
        // ignore cases: h = -1 & h= 0, handled by printRange() 

        //root has childern
	InternalNode p = (InternalNode) tree.root;
	path[0] = p;
	for(int i = 1; i < h; i++){
	//go left
		if(key.compareTo(p.child0.guide) <= 0 ){
			p = (InternalNode) p.child0;
			path[i] = p;
			}
		else if((p.child2 == null)||(key.compareTo(p.child1.guide) <= 0)){
			p = (InternalNode) p.child1;
			path[i] = p;
			}
		else{  //key > p.guide = p.child2.guide
			p = (InternalNode) p.child2;
			path[i] = p;
			}
	}                                                              //if the key is in the tree,handled by printAll()
	return path;
}

   static int divergePoint(InternalNode[] X, InternalNode[] Y){

	int size = X.length;

	for(int i= 0; i < size; i ++){
	//return the index of divergence point
		if(X[i].guide.compareTo(Y[i].guide)!= 0){
		return i;
		}
	}
	
	//not diverge at all, return -1
	return -1;
	}




   static void insert(String key, int value, TwoThreeTree tree) {
   // insert a key value pair into tree (overwrite existsing value
   // if key is already present)

      int h = tree.height;

       if (h == -1) {
          LeafNode newLeaf = new LeafNode();
          newLeaf.guide = key;
          newLeaf.value = value;
          tree.root = newLeaf; 
          tree.height = 0;
      }
      else {
         WorkSpace ws = doInsert(key, value, tree.root, h);

         if (ws != null && ws.newNode != null) {
         // create a new root

            InternalNode newRoot = new InternalNode();
            if (ws.offset == 0) {
               newRoot.child0 = ws.newNode; 
               newRoot.child1 = tree.root;
            }
            else {
               newRoot.child0 = tree.root; 
               newRoot.child1 = ws.newNode;
            }
            resetGuide(newRoot);
            tree.root = newRoot;
            tree.height = h+1;
         }
      }
   }

   static WorkSpace doInsert(String key, int value, Node p, int h) {
   // auxiliary recursive routine for insert

      if (h == 0) {
         // we're at the leaf level, so compare and 
         // either update value or insert new leaf

         LeafNode leaf = (LeafNode) p; //downcast
         int cmp = key.compareTo(leaf.guide);

         if (cmp == 0) {
            leaf.value = value; 
            return null;
         }

         // create new leaf node and insert into tree
         LeafNode newLeaf = new LeafNode();
         newLeaf.guide = key; 
         newLeaf.value = value;

         int offset = (cmp < 0) ? 0 : 1;
         // offset == 0 => newLeaf inserted as left sibling
         // offset == 1 => newLeaf inserted as right sibling

         WorkSpace ws = new WorkSpace();
         ws.newNode = newLeaf;
         ws.offset = offset;
         ws.scratch = new Node[4];

         return ws;
      }
      else {
         InternalNode q = (InternalNode) p; // downcast
         int pos;
         WorkSpace ws;

         if (key.compareTo(q.child0.guide) <= 0) {
            pos = 0; 
            ws = doInsert(key, value, q.child0, h-1);
         }
         else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
            pos = 1;
            ws = doInsert(key, value, q.child1, h-1);
         }
         else {
            pos = 2; 
            ws = doInsert(key, value, q.child2, h-1);
         }

         if (ws != null) {
            if (ws.newNode != null) {
               // make ws.newNode child # pos + ws.offset of q

               int sz = copyOutChildren(q, ws.scratch);
               insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
               if (sz == 2) {
                  ws.newNode = null;
                  ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
               }
               else {
                  ws.newNode = new InternalNode();
                  ws.offset = 1;
                  resetChildren(q, ws.scratch, 0, 2);
                  resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
               }
            }
            else if (ws.guideChanged) {
               ws.guideChanged = resetGuide(q);
            }
         }

         return ws;
      }
   }


   static int copyOutChildren(InternalNode q, Node[] x) {
   // copy children of q into x, and return # of children

      int sz = 2;
      x[0] = q.child0; x[1] = q.child1;
      if (q.child2 != null) {
         x[2] = q.child2; 
         sz = 3;
      }
      return sz;
   }

   static void insertNode(Node[] x, Node p, int sz, int pos) {
   // insert p in x[0..sz) at position pos,
   // moving existing extries to the right

      for (int i = sz; i > pos; i--)
         x[i] = x[i-1];

      x[pos] = p;
   }

   static boolean resetGuide(InternalNode q) {
   // reset q.guide, and return true if it changes.

      String oldGuide = q.guide;
      if (q.child2 != null)
         q.guide = q.child2.guide;
      else
         q.guide = q.child1.guide;

      return q.guide != oldGuide;
   }


   static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
   // reset q's children to x[pos..pos+sz), where sz is 2 or 3.
   // also resets guide, and returns the result of that

      q.child0 = x[pos]; 
      q.child1 = x[pos+1];

      if (sz == 3) 
         q.child2 = x[pos+2];
      else
         q.child2 = null;

      return resetGuide(q);
   }
}

