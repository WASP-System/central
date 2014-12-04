package edu.yu.einstein.wasp.grid.work;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;

// ContextConfiguration(locations = {"classpath:/META-INF/application-context-ssh-test.xml" })
public class PbsTorqueWorkServiceTest { //extends AbstractTestNGSpringContextTests {
	
	@Mock private GridTransportConnection mockTransportConnection;
	
	@Mock private GridResult mockGridResult;
	
	private PbsTorqueWorkService pbsTorqueWorkService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String knownJob = "<?xml version='1.0'?>\n" +
			"<Job><Job_Id>1.trestles-fe1.sdsc.edu</Job_Id><Job_Name>test</Job_Name><Job_Owner>test@trestles-login1.local</Job_Owner>" + 
			"<resources_used><cput>1:11:11</cput><mem>1kb</mem><vmem>1kb</vmem><walltime>1:11:11</walltime></resources_used>" +
			"<job_state>R</job_state><queue>normal</queue><server>trestles-fe1.sdsc.edu</server><Account_Name>A</Account_Name>" +
			"<Checkpoint>u</Checkpoint><ctime>10</ctime><Error_Path>trestles-login1.sdsc.edu:/home/a</Error_Path>" +
			"<exec_host>trestles-11-17/31</exec_host><Hold_Types>n</Hold_Types><Join_Path>n</Join_Path><Keep_Files>n</Keep_Files>" + 
			"<Mail_Points>a</Mail_Points><mtime>11</mtime><Output_Path>trestles-login1.sdsc.edu:/home/a</Output_Path><Priority>0</Priority>" + 
			"<qtime>10</qtime><Rerunable>True</Rerunable><Resource_List><mem>1gb</mem><nodect>1</nodect><nodes>1:ppn=32</nodes>" +
			"<walltime>300:00:00</walltime></Resource_List><session_id>1111</session_id><Shell_Path_List>/bin/sh</Shell_Path_List>" +
			"<comment>1</comment><etime>10</etime><submit_args>pat</submit_args><start_time>10</start_time><Walltime>" +
			"<Remaining>1</Remaining></Walltime><start_count>1</start_count><fault_tolerant>False</fault_tolerant></Job>";
	
	private String unknown = "<?xml version='1.0'?>\n<none />";
	
	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockTransportConnection);
		Assert.assertNotNull(mockGridResult);
		pbsTorqueWorkService = new PbsTorqueWorkService(mockTransportConnection);
	}
	
	@Test
	public void testUnknown() throws MisconfiguredWorkUnitException, GridException, SAXException, IOException, TransformerException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(unknown));
		
		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");
		
		Document retval = pbsTorqueWorkService.getQstat(fakeResult, "none");
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(retval), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		
		logger.debug("got: " + output);
		
		boolean unknown = pbsTorqueWorkService.isUnknown(retval);
		logger.debug("unk:" + unknown);
		
		Assert.assertTrue(unknown);
		
	}
	
	@Test
	public void testNotUnknown() throws GridException, SAXException, IOException, MisconfiguredWorkUnitException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(knownJob));
		
		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");
		
		Document retval = pbsTorqueWorkService.getQstat(fakeResult, "none");
		boolean unknown = pbsTorqueWorkService.isUnknown(retval);
		Assert.assertFalse(unknown);
		
	}

}
