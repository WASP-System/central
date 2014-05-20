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
public class SgeWorkServiceTest { //extends AbstractTestNGSpringContextTests {
	
	@Mock private GridTransportConnection mockTransportConnection;
	
	@Mock private GridResult mockGridResult;
	
	private SgeWorkService sgeWorkService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String successfulJob = "<?xml version='1.0'?>\n" +
			"<detailed_job_info>\n <djob_info>\n  <qmaster_response>\n   <JB_job_name>test</JB_job_name>\n   <JB_ja_tasks>\n    " + 
			"<ulong_sublist>\n     <JAT_status>128</JAT_status>\n     <JAT_task_number>1</JAT_task_number>\n    " + 
			"</ulong_sublist>\n   </JB_ja_tasks>\n   </qmaster_response>\n </djob_info>\n</detailed_job_info>";
	
	private String unsuccessfulJob = "<?xml version='1.0'?>\n" +
			"<detailed_job_info>\n <djob_info>\n  <qmaster_response>\n   <JB_job_name>test</JB_job_name>\n   <JB_ja_tasks>\n    " + 
			"<ulong_sublist>\n     <JAT_status>32784</JAT_status>\n     <JAT_task_number>1</JAT_task_number>\n    " + 
			"</ulong_sublist>\n   </JB_ja_tasks>\n   </qmaster_response>\n </djob_info>\n</detailed_job_info>";
	
	private String successfulTask = "<?xml version='1.0'?><detailed_job_info><djob_info><qmaster_response><JB_job_name>test</JB_job_name>" + 
	      "<JB_ja_tasks><ulong_sublist><JAT_status>128</JAT_status><JAT_task_number>1</JAT_task_number></ulong_sublist>" +
		  "<ulong_sublist><JAT_status>128</JAT_status><JAT_task_number>2</JAT_task_number></ulong_sublist></JB_ja_tasks>" +
	      "</qmaster_response></djob_info></detailed_job_info>";
	
	private String unknown = "<?xml version='1.0'?>\n<unknown_jobs> <a>\n<ST_name>test</ST_name>\n </a>\n</unknown_jobs>";
	
	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockTransportConnection);
		Assert.assertNotNull(mockGridResult);
		sgeWorkService = new SgeWorkService(mockTransportConnection);
	}
	
	@Test
	public void testUnknown() throws MisconfiguredWorkUnitException, GridException, SAXException, IOException, TransformerException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(unknown));
		
		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");
		
		Document retval = sgeWorkService.getQstat(fakeResult, "none");
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(retval), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		
		logger.debug("got: " + output);
		
		boolean unknown = sgeWorkService.isUnknown(retval);
		
		Assert.assertTrue(unknown);
		
	}
	
	@Test
	public void testNotUnknown() throws GridException, SAXException, IOException, MisconfiguredWorkUnitException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(successfulJob));
		
		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");
		
		Document retval = sgeWorkService.getQstat(fakeResult, "none");
		boolean unknown = sgeWorkService.isUnknown(retval);
		Assert.assertFalse(unknown);
		
	}
	
	@Test
	public void testNotInError() throws GridException, SAXException, IOException, MisconfiguredWorkUnitException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(successfulJob));
		
		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");
		
		Document retval = sgeWorkService.getQstat(fakeResult, "none");
		boolean unknown = sgeWorkService.isInError(retval);
		Assert.assertFalse(unknown);
		
	}
	
	@Test
	public void testTaskNotInError() throws GridException, SAXException, IOException, MisconfiguredWorkUnitException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(successfulTask));
		
		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");
		
		Document retval = sgeWorkService.getQstat(fakeResult, "none");
		boolean unknown = sgeWorkService.isInError(retval);
		Assert.assertFalse(unknown);
		
	}
	
	@Test
	public void testInError() throws GridException, SAXException, IOException, MisconfiguredWorkUnitException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(unsuccessfulJob));
		
		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");
		
		Document retval = sgeWorkService.getQstat(fakeResult, "none");
		boolean unknown = sgeWorkService.isInError(retval);
		Assert.assertTrue(unknown);
		
	}
	

}
