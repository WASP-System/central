package edu.yu.einstein.wasp.service.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.AcctGrantjobDao;
import edu.yu.einstein.wasp.model.AcctGrantjob;



public class TestAcctGrantjobServiceImpl {
	/*
	private AcctGrantjobServiceImpl acctGrantjobServiceImpl = new AcctGrantjobServiceImpl();
	private AcctGrantjobDao mockAcctGrantjobDao;
	
	
	@BeforeClass
	public void setUp() throws Exception {
		
		mockAcctGrantjobDao = createMock(AcctGrantjobDao.class);
		Assert.assertNotNull(mockAcctGrantjobDao);
		acctGrantjobServiceImpl.setAcctGrantjobDao(mockAcctGrantjobDao);
	}

	@AfterClass
	public void tearDown() throws Exception {
		acctGrantjobServiceImpl = null;
		mockAcctGrantjobDao = null;
	}

	@Test (groups = "unit-tests")
	public void testGetAcctGrantByGrantId() {
		AcctGrantjob results = new AcctGrantjob();
			
		expect(mockAcctGrantjobDao.getAcctGrantjobByJobId(1)).andReturn(results);
		replay(mockAcctGrantjobDao);
		acctGrantjobServiceImpl.getAcctGrantjobByJobId(1);
		//Verifies that the specified behavior has been used: acctGrantServiceImpl.getAcctGrantByGrantId(1)
		verify(mockAcctGrantjobDao);
		
		//fail("Not yet implemented");
	}

*/
}
