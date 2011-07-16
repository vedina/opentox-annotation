package net.idea.opentox.annotation.report;

import java.io.IOException;
import java.io.InputStream;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.RDFDefaultErrorHandler;
import com.hp.hpl.jena.shared.Lock;

public class OTREST {
	public enum OTRESTClass {
		RESTOperation,
		InputParameter,
		HTTPMethod,
		HTTPStatus;
		public String getNS() {
			return String.format(_NS, toString());
		}
		public OntClass getOntClass(OntModel model) {
			OntClass c = model.getOntClass(getNS());
			return (c==null)?createOntClass(model):c;
		}
		public OntClass createOntClass(OntModel model) {
			return model.createClass(getNS());
		}	
		public Individual createIndividual(String id,OntModel model) {
			return model.createIndividual( String.format(_NS, id), getOntClass(model));
		}	
	};

	private static Model m_model = ModelFactory.createDefaultModel();
	protected static final String _NS = "http://opentox.org/opentox-rest.owl#%s";
	public static final String NS = String.format(_NS,"");
	
	public static String getURI() {return NS;}
    public static final Resource NAMESPACE = m_model.createResource( NS );

    /**
     * Object properties
     */
    public enum OTRESTObjectProperty {
		   	
		    hasHTTPMethod,
		    inputParam,
		    resource,
		    result,
		    status,
		    uri,
		    paramContent;


		   	public Property createProperty(OntModel jenaModel) {
		   		Property p = jenaModel.getObjectProperty(String.format(_NS, toString()));
		   		return p!= null?p:
		   				jenaModel.createObjectProperty(String.format(_NS, toString()));
		   	}
    }
    /**
     * Data properties
     */
    public enum OTRESTDataProperty {
    	hasAttribute,
    	hasURI,
	    paramName,
	    paramOptional;
	        	
	   	public Property createProperty(OntModel jenaModel) {
	   		Property p = jenaModel.getDatatypeProperty(String.format(_NS, toString()));
	   		return p!= null?p:
	   				jenaModel.createDatatypeProperty(String.format(_NS, toString()));
	   	}
    };

    public static OntModel createModel() throws Exception {
    	return createModel(OntModelSpec.OWL_DL_MEM);
    }
	public static OntModel createModel(OntModelSpec spec) throws ResourceException {
		OntModel jenaModel = ModelFactory.createOntologyModel( spec,null);
		jenaModel.setNsPrefix( "otrs", OTREST.NS );
		return jenaModel;
	}

    public static Model createModel(Model base,InputStream in, MediaType mediaType) throws ResourceException {
    	Model model = base==null?createModel(OntModelSpec.OWL_DL_MEM):base;
    	RDFReader reader = model.getReader();
    	/**
    	 * When reading RDF/XML the check for reuse of rdf:ID has a memory overhead,
    	 * which can be significant for very large files. 
    	 * In this case, this check can be suppressed by telling ARP to ignore this error. 
    	 */
    	reader.setProperty("WARN_REDEFINITION_OF_ID","EM_IGNORE");

    	model.enterCriticalSection(Lock.WRITE) ;
    	try {
    		reader.setProperty("error-mode", "lax" );
    		reader.setProperty("WARN_REDEFINITION_OF_ID","EM_IGNORE");

    		reader.setErrorHandler(new RDFDefaultErrorHandler() {
    			@Override
    			public void warning(Exception e) {
    				super.warning(e);
    				
    			}
    		});
    		reader.read(model,in,getJenaFormat(mediaType));
    		return model;
    	} catch (Exception x) {
    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x.getMessage(),x);
    	} finally {
    		model.leaveCriticalSection() ;

    	}
    	
    }
    public static Model createModel(Model model,Representation entity,MediaType mediaType) throws ResourceException {
    	try {
    		return createModel(model,entity.getStream(),entity.getMediaType()==null?entity.getMediaType():entity.getMediaType());
    	} catch (IOException x) {
    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x.getMessage(),x);
    	}
    }	    
   
	public static String getJenaFormat(MediaType mediaType) throws ResourceException {
		if (mediaType.equals(MediaType.APPLICATION_RDF_XML))
			return "RDF/XML";
			//return "RDF/XML-ABBREV";	
		else if (mediaType.equals(MediaType.APPLICATION_RDF_TURTLE))
			return "TURTLE";
		else if (mediaType.equals(MediaType.TEXT_RDF_N3))
			return "N3";
		else if (mediaType.equals(MediaType.TEXT_RDF_NTRIPLES))
			return "N-TRIPLE";		
		else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
						String.format("Unsupported format %s",mediaType));
	}
	

}
