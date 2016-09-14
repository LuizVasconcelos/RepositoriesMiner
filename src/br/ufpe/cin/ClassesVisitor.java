package br.ufpe.cin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import br.com.metricminer2.parser.jdt.JDTRunner;
import br.com.metricminer2.scm.RepositoryFile;

public class ClassesVisitor extends ASTVisitor {
	
	private RepositoryFile file;
	private int qty = 0;
	private ArrayList<String> names = new ArrayList<String>();
	private HashMap<String, Boolean> mapsCode = new HashMap<String,Boolean>();
	
	private MethodsVisitor test;
	
	public ClassesVisitor(RepositoryFile file) {
		this.file = file;
		
		this.mapsCode.put("MANIFEST_ACCESS_COARSE_LOCATION", false);
		this.mapsCode.put("MANIFEST_ACCESS_FINE_LOCATION", false);
		this.mapsCode.put("MANIFEST_INTERNET", false);
		this.mapsCode.put("MANIFEST_GMS_VERSION", false);
		this.mapsCode.put("MANIFEST_GEO_API_KEY", false);
		this.mapsCode.put("MANIFEST_GLES_VERSION", false);
		this.mapsCode.put("LAYOUT_GMS_MAPS", false);
		this.mapsCode.put("CLASS_IMPLEMENTS", false);
		this.mapsCode.put("CLASS_MAP_INITIALIZATION", false);
	}
	
	public boolean visit() {
		
		File temp = this.file.getFile();
		System.out.println(this.file.fileNameContains("AndroidManifest"));
		System.out.println(this.file.getFullName());
		System.out.println(this.file.getFile().getName());
		String code = readFile(temp);
		if(this.file.getFile().getName().contains("AndroidManifest")) {
			//System.out.println("Manifesto: " + code);
			if(code.contains("android:name=\"android.permission.ACCESS_COARSE_LOCATION\"")) this.mapsCode.put("MANIFEST_ACCESS_COARSE_LOCATION", true);
			if(code.contains("android:name=\"android.permission.ACCESS_FINE_LOCATION\"")) this.mapsCode.put("MANIFEST_ACCESS_FINE_LOCATION", true);
			if(code.contains("android:name=\"android.permission.INTERNET\"")) this.mapsCode.put("MANIFEST_INTERNET", true);
			if(code.contains("android:name=\"com.google.android.gms.version\"android:value=\"@integer/google_play_services_version\"")) this.mapsCode.put("MANIFEST_GMS_VERSION", true);
			if(code.contains("android:name=\"com.google.android.geo.API_KEY\"")) this.mapsCode.put("MANIFEST_GMS_VERSION", true);
			if(code.contains("android:glEsVersion=\"0x00020000\" android:required=\"true\"")) this.mapsCode.put("MANIFEST_GLES_VERSION", true);
			
			System.out.println("CODES PRESENT ON MANIFEST");
			System.out.println("COARSE LOCATION: " + this.mapsCode.get("MANIFEST_ACCESS_COARSE_LOCATION"));
			System.out.println("FINE LOCATION: " + this.mapsCode.get("MANIFEST_ACCESS_FINE_LOCATION"));
			System.out.println("INTERNET: " + this.mapsCode.get("MANIFEST_INTERNET"));
			System.out.println("GMS VERSION: " + this.mapsCode.get("MANIFEST_GMS_VERSION"));
			System.out.println("API KEY: " + this.mapsCode.get("MANIFEST_GEO_API_KEY"));
			System.out.println("GLES VERSION: " + this.mapsCode.get("MANIFEST_GLES_VERSION"));
			
		}else if(this.file.getFullName().contains("res\\layout")) {
			if(code.contains("android:name=\"com.google.android.gms.maps.MapFragment\"") || code.contains("android:name=\"com.google.android.gms.maps.SupportMapFragment\"")) {
				this.mapsCode.put("LAYOUT_GMS_MAPS", true);
				
				System.out.println("CODE PRESENT ON LAYOUT");
				System.out.println("GMS MAPS: " + this.mapsCode.get("LAYOUT_GMS_MAPS"));
			}
			
			//System.out.println("CODE PRESENT ON LAYOUT");
			//System.out.println("GMS MAPS: " + this.mapsCode.get("LAYOUT_GMS_MAPS"));
		}
		
		else {
			if(code.contains("implements") && code.contains("OnMapReadyCallback")) {
				this.mapsCode.put("CLASS_IMPLEMENTS", true);
				
				System.out.println("CODE PRESENT ON CLASS");
				System.out.println("CLASS IMPLEMENTS: " + this.mapsCode.get("CLASS_IMPLEMENTS"));
			}
			
			//System.out.println("CODE PRESENT ON CLASS");
			//System.out.println("CLASS IMPLEMENTS: " + this.mapsCode.get("CLASS_IMPLEMENTS"));
			
			test = new MethodsVisitor();
			new JDTRunner().visit(test, new ByteArrayInputStream(readFile(temp).getBytes()));
			
			//test.searchOnCode();
			//this.mapsCode.put("CLASS_MAP_INITIALIZATION", test.getMapInit());
			
			//this.searchOnCode();
			
			qty = test.getQty();
			names = test.getNames();
		}
		
		return true;
	}
	
	private String readFile(File f) {
		try {
			FileInputStream input = new FileInputStream(f);
			String text = IOUtils.toString(input);
			input.close();
			return text;
		} catch (Exception e) {
			throw new RuntimeException("error reading file " + f.getAbsolutePath(), e);
		}
	}
	
	public void searchOnCode() {
		for (Block methodCode : test.getMethods()) {
			//Block methodCode = met.getBody();
			if(methodCode.toString().contains("MapFragment") && methodCode.toString().contains("(MapFragment) getFragmentManager().findFragmentById")) {
				if(methodCode.toString().contains("getMapSync")) {
					//this.mapInitialization = true;
					this.mapsCode.put("CLASS_MAP_INITIALIZATION", test.getMapInit());
				
					//System.out.println("Method name: " + met.getName());
					System.out.println("Code Block: " + methodCode.toString());
				}
			}
		
			if(methodCode.toString().contains("SupportMapFragment") && methodCode.toString().contains("(SupportMapFragment) getFragmentManager().findFragmentById")) {
				if(methodCode.toString().contains("getMapSync")) {
					//this.mapInitialization = true;
					this.mapsCode.put("CLASS_MAP_INITIALIZATION", test.getMapInit());
				
					//System.out.println("Method name: " + met.getName());
					System.out.println("Code Block: " + methodCode.toString());
				}
			}
		}
	}
	
	
	public int getQty() {
		return qty;
	}
	
	public ArrayList<String> getNames() {
		return this.names;
	}
}
