package net.idea.opentox.annotation.report;

import net.idea.opentox.annotation.OpenToxRESTOperation;
import net.idea.opentox.annotation.OpenToxRESTOperation.HTTP_STATUS;
import net.idea.opentox.annotation.report.OTREST.OTRESTObjectProperty;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

public class Report {
	protected OntModel jenaModel;
	public OntModel getJenaModel() {
		return jenaModel;
	}

	public void setJenaModel(OntModel jenaModel) {
		this.jenaModel = jenaModel;
	}

	protected Individual individual;
	
	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	protected void addItem(OpenToxRESTOperation operation) throws Exception {
		Individual method = OTREST.OTRESTClass.HTTPMethod.createIndividual(operation.hasHTTPMethod().name(),jenaModel);
		jenaModel.add(individual,OTRESTObjectProperty.hasHTTPMethod.createProperty(jenaModel),method);
		
		for (HTTP_STATUS astatus : operation.hasStatus()) {
			Individual status = OTREST.OTRESTClass.HTTPMethod.createIndividual(astatus.name(),jenaModel);
			jenaModel.add(individual,OTRESTObjectProperty.status.createProperty(jenaModel),status);
		}
	}

}
