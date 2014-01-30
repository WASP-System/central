package edu.yu.einstein.wasp.util;

import java.sql.Date;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.controller.util.BatchJobTreeModel;
import edu.yu.einstein.wasp.controller.util.ExtTreeModel.ExtIcon;

public class testExtTreeModel {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private BatchJobTreeModel testTreeModel;
	
	@BeforeClass
	public void setup(){
		testTreeModel = new BatchJobTreeModel();
		testTreeModel.addChild(new BatchJobTreeModel("1", ExtIcon.TASK_FOLDER, false, false, "job 1", 1L, 
				new Date(Calendar.getInstance().getTimeInMillis() - 100000), 
				new Date(Calendar.getInstance().getTimeInMillis()), "COMPLETED", "COMPLETED", "GREAT"));
		testTreeModel.addChild(new BatchJobTreeModel("2", ExtIcon.TASK_FOLDER, false, false, "job 2", 2L, 
				new Date(Calendar.getInstance().getTimeInMillis() - 700000), 
				new Date(Calendar.getInstance().getTimeInMillis()), "STOPPED", "HIBERNATING", "OK"));
		BatchJobTreeModel node = new BatchJobTreeModel("3", ExtIcon.TASK_FOLDER, false, false, "job 3", 3L, 
				new Date(Calendar.getInstance().getTimeInMillis() - 9000000),
				new Date(Calendar.getInstance().getTimeInMillis()), "STOPPED", "HIBERNATING", "OK");
		node.addChild(new BatchJobTreeModel("1000", ExtIcon.TASK, false, false, "step 1", 1L, 
				new Date(Calendar.getInstance().getTimeInMillis() - 5000000), 
				new Date(Calendar.getInstance().getTimeInMillis()), "STOPPED", "HIBERNATING", "GRAND"));
		testTreeModel.addChild(node);
	}
	
	@Test (groups = "unit-tests")
	public void testJsonEncode() {
		//String output = testTreeModel.getAsJSON();
		//logger.debug(output);
		//Assert.assertNotNull(output);
	}

	public testExtTreeModel() {
	}
}
