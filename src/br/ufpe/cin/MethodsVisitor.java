package br.ufpe.cin;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MethodsVisitor extends ASTVisitor {

	private int qty = 0;
	private ArrayList<String> names = new ArrayList<String>();
	private boolean mapInitialization = false;
	private ArrayList<Block> methods = new ArrayList<Block>();

	public boolean visit(MethodDeclaration node) {
		//System.out.println("Method name: " + node.getName());
		this.methods.add(node.getBody());
		//System.out.println("Code Block: " + methodCode.toString());
		
		/*if(methodCode.toString().contains("MapFragment") && methodCode.toString().contains("(MapFragment) getFragmentManager().findFragmentById")) {
			if(methodCode.toString().contains("getMapSync")) {
				this.mapInitialization = true;
				
				System.out.println("Method name: " + node.getName());
				System.out.println("Code Block: " + methodCode.toString());
			}
		}
		
		if(methodCode.toString().contains("SupportMapFragment") && methodCode.toString().contains("(SupportMapFragment) getFragmentManager().findFragmentById")) {
			if(methodCode.toString().contains("getMapSync")) {
				this.mapInitialization = true;
				
				System.out.println("Method name: " + node.getName());
				System.out.println("Code Block: " + methodCode.toString());
			}
		}*/
		
		qty++;
		return super.visit(node);
	}

	/*public boolean visit(VariableDeclarationFragment node) {
		SimpleName name = node.getName();
		this.names.add(name.getIdentifier());
		System.out.println("Declaration of '"+name);
		return false; // do not continue to avoid usage info
	}
	
	public boolean visit(SimpleName node) {
		if (this.names.contains(node.getIdentifier())) {
		System.out.println("Usage of '" + node);
		}
		return true;
	}*/

	public int getQty() {
		return this.qty;
	}
	
	public ArrayList<String> getNames() {
		return this.names;
	}
	
	public boolean getMapInit() {
		//searchOnCode();
		
		return this.mapInitialization;
	}
	
	public ArrayList<Block> getMethods() {
		return this.methods;
	}
}
