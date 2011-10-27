package edu.yu.einstein.wasp.service.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.yu.einstein.wasp.dao.AcctGrantDao;
import edu.yu.einstein.wasp.model.AcctGrant;
import static org.easymock.EasyMock.*;

public class TestAcctGrantServiceImpl {
	
	private AcctGrantServiceImpl acctGrantServiceImpl = new AcctGrantServiceImpl();
	private AcctGrantDao mockAcctGrantDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		mockAcctGrantDao = createMock(AcctGrantDao.class);
		assertNotNull(mockAcctGrantDao);
		acctGrantServiceImpl.setAcctGrantDao(mockAcctGrantDao);
	}

	@After
	public void tearDown() throws Exception {
		acctGrantServiceImpl = null;
		mockAcctGrantDao = null;
	}

	@Test
	public void test() {
		AcctGrant results = new AcctGrant();
			
		expect(mockAcctGrantDao.getAcctGrantByGrantId(0)).andReturn(results);
		replay(mockAcctGrantDao);
		acctGrantServiceImpl.getAcctGrantByGrantId(0);
		//Verifies that the specified behavior has been used: acctGrantServiceImpl.getAcctGrantByGrantId(1)
		verify(mockAcctGrantDao);
		
		//fail("Not yet implemented");
	}

}
