package com.umlrtParser; 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrusrt.umlrt.uml.util.UMLRTResourcesUtil;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.common.util.UML2Util.EObjectMatcher;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Vertex;

public class Instrumentation {

	/**
	 * The resource set containing the instrumented model
	 */
	private ResourceSet resourceSet;
	/**
	 * Input path of the model to instrument
	 */
	private String inputPath;
	/**
	 * Output path of the instrumented model
	 */
	private String outputPath;
	/**
	 * Output resource
	 */
	private Resource outputResource;
	/**
	 * Output instrumented model
	 */
	private Model outputModel;
	/**
	 * Capsule to instrument (CUT)
	 */
	private Class capsule;
	/**
	 * The state machine of the CUT
	 */
	private StateMachine statemachine;
	/**
	 * The region of the state machine
	 */
	private Region region;

	/**
	 * The list of capsule attribute 
	 */
	private Property[] attributes;
	/**
	 * @constructor
	 * Initialize and perform the instrumentation
	 */
	public Instrumentation() throws Exception { 
	}
	/**
	 * @param path - the path of the input/output file
	 * @return {@link Resource} - a Resource located at the corresponding path
	 */
	private Resource getResource(String path) {
		URI uri = URI.createFileURI(path);
		Resource resource = resourceSet.getResource(uri, true);
		EcoreUtil.resolveAll(resourceSet);
		return resource; 
	}
	/**
	 * Helper that returns the unique region of a UML-RT state machine
	 * @return the unique region of the UML-RT state machine
	 */
	public Region getRegion() {
		if (region == null)
			region = statemachine.getRegions().get(0);
		return region;
	}

	/**
	 * Initialize the model to instrument.
	 * This method copies the input model into the output model.
	 * Then, it retrieves the output model.
	 * @return {@link Boolean} indicates whether the initialization succeeded or failed.
	 */
	private boolean initOutputModel() {

		copyFile(inputPath, outputPath);
		outputResource = getResource(outputPath);
		if (outputResource == null)
			return false;
		Object element = outputResource.getContents().get(0);
		if (!(element instanceof Model))
			return false;
		outputModel = (Model)element;
		return true;
	}
	/**
	 * Retireve the capsule to instrument.
	 * @param capsuleName - the name of the capsule to instrument
	 * @return {@link Boolean} indicates whether the capsule was correctly initialized.
	 */
	private boolean initCapsule(String capsuleName) {
		if (outputModel == null)
			return false;
		EObject eobj = UML2Util.findEObject(outputModel.getPackagedElements(), new EObjectMatcher() {
			@Override
			public boolean matches(EObject el) {
				// TODO Auto-generated method stub
				return (el instanceof Class
						&& ((Class)el).getName().equals(capsuleName));
			}
		});
		if (eobj != null) {
			capsule = (Class) eobj;
			statemachine = (StateMachine) capsule.getOwnedBehaviors().get(0);
		}
		return capsule != null && statemachine != null;
	}
	/**
	 * Initialize the resource set.
	 * Uses {@link UMLRTResourcesUtil} to correctly initialize it for UML-RT models.
	 */
	private void initResourceSet() {
		resourceSet = new ResourceSetImpl();
		UMLRTResourcesUtil.init(resourceSet);

	}
	/**
	 * Initialise the instrumentation
	 * @param args the list of args passed to the Java program
	 * @return whether the initialization succeeded or failed
	 * @throws Exception
	 */
	private boolean init(String[] args) throws Exception {
		if (args.length < 6) {
			throw new Exception("Not enough argument. Usage: java Instrumentation -i "
					+ "<input file> -o <output file> -c <capsule name>");
		}
		List<String> arguments = Arrays.asList(args);
		int i = arguments.indexOf("-i");
		int o = arguments.indexOf("-o");
		int c = arguments.indexOf("-c");
		if (i == -1 || o == -1 || c == -1) {
			throw new Exception("Missing argument. Usage: java Instrumentation -i "
					+ "<input file> -o <output file> -c <capsule name>");
		}
		inputPath = arguments.get(++i);
		outputPath = arguments.get(++o);
		initResourceSet();
		boolean result =
				initOutputModel() &
				initCapsule(arguments.get(++c));
		return result;
	}
	/**
	 * Copy a file from one path into another.
	 * @param inputPath the path of the input file
	 * @param outputPath the path of the output file
	 */
	private void copyFile(String inputPath, String outputPath) {

		InputStream in = null;
		OutputStream out = null;
		try {

			//create output directory if it doesn't exist
			File dir = new File (outputPath); 

			in = new FileInputStream(inputPath);        
			out = new FileOutputStream(outputPath);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file (You have now copied the file)
			out.flush();
			out.close();
			out = null;        

		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
