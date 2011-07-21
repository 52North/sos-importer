package org.n52.sos.importer.test;

import org.n52.sos.importer.controller.MainController;
import org.n52.sos.importer.controller.Step6bController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.Column;

public class Step6bTest {

	public static void main(String[] args) {
		MainController f = MainController.getInstance();
		Object[][] o = {{"1", "4"},{"2", "5"},{"3", "6"}};
		TableController.getInstance().setContent(o);
		MeasuredValue mv = new NumericValue();
		mv.setTableElement(new Column(0));
		MeasuredValue mv2 = new NumericValue();
		mv2.setTableElement(new Column(1));
		ModelStore.getInstance().add(mv);
		ModelStore.getInstance().add(mv2);
		Step6bModel step6aModel = new Step6bModel(mv, new FeatureOfInterest());
		f.setStepController(new Step6bController(step6aModel));
	}
}
