package org.n52.sos.importer.controller;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.Resource;

public class ResourceController {
	
	public Resource getNextUnassignedResource(Resource r) {
		for (Resource resource: ModelStore.getInstance().getFeatureOfInterests())
			if (!isAssignedToMeasuredValue(resource))
				return resource;	
		return null;
	}
	
	public boolean isAssignedToMeasuredValue(Resource resource) {
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) 
			if (resource.isAssigned(mv))
				return true;
		return false;
	}
}
