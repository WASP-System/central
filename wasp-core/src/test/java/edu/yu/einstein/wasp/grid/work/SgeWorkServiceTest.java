package edu.yu.einstein.wasp.grid.work;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.StringUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.SingleHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.file.SshFileService;

@ContextConfiguration(locations = { "classpath:/META-INF/application-context-ssh-test.xml" })
public class SgeWorkServiceTest extends AbstractTestNGSpringContextTests {

	@Mock
	private GridTransportConnection mockTransportConnection;

	@Mock
	private GridResult mockGridResult;

	GridFileService gfs;

	@Autowired
	GridTransportConnection testGridTransportConnection;
	GridWorkService gws;
	GridHostResolver ghr;

	private SgeWorkService sgeWorkService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String successfulJob = "<?xml version='1.0'?>\n"
			+ "<detailed_job_info>\n <djob_info>\n  <qmaster_response>\n   <JB_job_name>test</JB_job_name>\n   <JB_ja_tasks>\n    "
			+ "<ulong_sublist>\n     <JAT_status>128</JAT_status>\n     <JAT_task_number>1</JAT_task_number>\n    "
			+ "</ulong_sublist>\n   </JB_ja_tasks>\n   </qmaster_response>\n </djob_info>\n</detailed_job_info>";

	private String unsuccessfulJob = "<?xml version='1.0'?>\n"
			+ "<detailed_job_info>\n <djob_info>\n  <qmaster_response>\n   <JB_job_name>test</JB_job_name>\n   <JB_ja_tasks>\n    "
			+ "<ulong_sublist>\n     <JAT_status>32784</JAT_status>\n     <JAT_task_number>1</JAT_task_number>\n    "
			+ "</ulong_sublist>\n   </JB_ja_tasks>\n   </qmaster_response>\n </djob_info>\n</detailed_job_info>";

	private String successfulTask = "<?xml version='1.0'?><detailed_job_info><djob_info><qmaster_response><JB_job_name>test</JB_job_name>"
			+ "<JB_ja_tasks><ulong_sublist><JAT_status>128</JAT_status><JAT_task_number>1</JAT_task_number></ulong_sublist>"
			+ "<ulong_sublist><JAT_status>128</JAT_status><JAT_task_number>2</JAT_task_number></ulong_sublist></JB_ja_tasks>"
			+ "</qmaster_response></djob_info></detailed_job_info>";

	private String unknown = "<?xml version='1.0'?>\n<unknown_jobs> <a>\n<ST_name>test</ST_name>\n </a>\n</unknown_jobs>";

	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
		Assert.assertParameterNotNull(mockTransportConnection);
		Assert.assertParameterNotNull(mockGridResult);
		sgeWorkService = new SgeWorkService(mockTransportConnection);

		gfs = new SshFileService(testGridTransportConnection);
		gws = new SgeWorkService(testGridTransportConnection);
		gws.setGridFileService(gfs);
		ghr = new SingleHostResolver(gws);

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
		Assert.assertTrue(!unknown);

	}

	@Test
	public void testNotInError() throws GridException, SAXException, IOException, MisconfiguredWorkUnitException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(successfulJob));

		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");

		Document retval = sgeWorkService.getQstat(fakeResult, "none");
		boolean unknown = sgeWorkService.isInError(retval);
		Assert.assertTrue(!unknown);

	}

	@Test
	public void testTaskNotInError() throws GridException, SAXException, IOException, MisconfiguredWorkUnitException {
		PowerMockito.when(mockTransportConnection.sendExecToRemote(Mockito.any(WorkUnit.class))).thenReturn(mockGridResult);
		PowerMockito.when(mockGridResult.getStdOutStream()).thenReturn(IOUtils.toInputStream(successfulTask));

		GridResultImpl fakeResult = new GridResultImpl();
		fakeResult.setWorkingDirectory("none");

		Document retval = sgeWorkService.getQstat(fakeResult, "none");
		boolean unknown = sgeWorkService.isInError(retval);
		Assert.assertTrue(!unknown);

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

	@Test(groups = { "ssh" })
	public void testGetStdOut() throws IOException, GridException, InterruptedException {
		WorkUnit w = new WorkUnit(new WorkUnitGridConfiguration());
        w.getConfiguration().setWorkingDirectory("/wasp/testing/");
		w.getConfiguration().setResultsDirectory("/wasp/testing/");
		w.setCommand("echo test");
		GridResult r = gws.execute(w);
		while (!ghr.isFinished(r)) {
			Thread.sleep(5000);
		}
		String out = gws.getResultStdOut(r, SgeWorkService.NO_FILE_SIZE_LIMIT);
		logger.debug("String was: " + out);
		Assert.assertTrue("test".equals(StringUtils.chomp(out)));
		
	}
	
	@Test(groups = { "ssh" })
	public void testGetStdErr() throws IOException, GridException, InterruptedException {
		WorkUnit w = new WorkUnit(new WorkUnitGridConfiguration());
        w.getConfiguration().setWorkingDirectory("/wasp/testing/");
		w.getConfiguration().setResultsDirectory("/wasp/testing/");
		w.setCommand("echo testErr >&2");
		GridResult r = gws.execute(w);
		while (!ghr.isFinished(r)) {
			Thread.sleep(5000);
		}
		String err = gws.getResultStdErr(r, SgeWorkService.NO_FILE_SIZE_LIMIT);
		logger.debug("String was: " + err);
		Assert.assertTrue(err.contains("testErr") && err.startsWith("submitted"));
	}
	
	@Test(groups = { "ssh" })
	public void testGetArbitraryFile() throws GridException, InterruptedException, IOException {
		WorkUnit w = new WorkUnit(new WorkUnitGridConfiguration());
        w.getConfiguration().setWorkingDirectory("/wasp/testing/");
		w.getConfiguration().setResultsDirectory("/wasp/testing/");
		w.setCommand("echo testArb > arbitraryFile.txt");
		GridResult r = gws.execute(w);
		while (!ghr.isFinished(r)) {
			Thread.sleep(5000);
		}
		String f = gws.getUnregisteredFileContents(r, "arbitraryFile.txt", SgeWorkService.NO_FILE_SIZE_LIMIT);
		logger.debug("String was: " + f);
		Assert.assertTrue(StringUtils.chomp(f).equals("testArb"));
	}
}
