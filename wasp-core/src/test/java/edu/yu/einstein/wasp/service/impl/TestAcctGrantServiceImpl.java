package edu.yu.einstein.wasp.service.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.AcctGrantDao;
import edu.yu.einstein.wasp.model.AcctGrant;

public class TestAcctGrantServiceImpl {
	
	private AcctGrantServiceImpl acctGrantServiceImpl = new AcctGrantServiceImpl();
	private AcctGrantDao mockAcctGrantDao;
	
	
	@BeforeClass
	public void setUp() throws Exception {
		
		mockAcctGrantDao = createMock(AcctGrantDao.class);
		Assert.assertNotNull(mockAcctGrantDao);
		acctGrantServiceImpl.setAcctGrantDao(mockAcctGrantDao);
	}

	@AfterClass
	public void tearDown() throws Exception {
		acctGrantServiceImpl = null;
		mockAcctGrantDao = null;
	}

	@Test (groups = "unit-tests")
	public void testGetAcctGrantByGrantId() {
		AcctGrant results = new AcctGrant();
			
		expect(mockAcctGrantDao.getAcctGrantByGrantId(0)).andReturn(results);
		replay(mockAcctGrantDao);
		acctGrantServiceImpl.getAcctGrantByGrantId(0);
		//Verifies that the specified behavior has been used: acctGrantServiceImpl.getAcctGrantByGrantId(1)
		verify(mockAcctGrantDao);
		
		//fail("Not yet implemented");
	}

}
