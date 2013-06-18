package edu.yu.einstein.wasp.plugin.babraham.service.imp;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridResultImpl;
import edu.yu.einstein.wasp.plugin.babraham.exception.FastQCDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.impl.BabrahamServiceImpl;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;

public class processFastQCOutputTest {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Mock BabrahamServiceImpl mockBabrahamServiceImpl;
	
	@BeforeClass
	public void beforeClass(){
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockBabrahamServiceImpl);
	}
	
	@Test (groups = "unit-tests")
	public void processFastQCOutputTest() {
		BabrahamServiceImpl babrahamServiceImpl = new BabrahamServiceImpl();
		InputStream in = this.getClass().getResourceAsStream("/fastqc_data.txt");
		Map<String, FastQCDataModule> moduleList = null;
		try {
			moduleList = babrahamServiceImpl.processFastQCOutput(in);
		} catch (FastQCDataParseException e) {
			logger.warn(e.getLocalizedMessage());
		}
		Assert.assertNotNull(moduleList);
		
		for (String key : moduleList.keySet()){
			logger.debug(">>data for key=" + key);
			logger.debug("    Name=" + moduleList.get(key).getName());
			logger.debug("    iName=" + moduleList.get(key).getIName());
			logger.debug("    result=" + moduleList.get(key).getResult());
			logger.debug("    attributes=" + moduleList.get(key).getAttributes());
			logger.debug("    keyValueData=" + moduleList.get(key).getKeyValueData());
			logger.debug("    dataPoints=" + moduleList.get(key).getDataPoints());
		}
		
		FastQCDataModule module = moduleList.get(FastQC.PlotType.BASIC_STATISTICS);
		Assert.assertEquals(module.getDataPoints().size(), 7);
		Assert.assertEquals(module.getKeyValueData().size(), 0);
		module = moduleList.get(FastQC.PlotType.DUPLICATION_LEVELS);
		Assert.assertEquals(module.getDataPoints().size(), 10);
		Assert.assertEquals(module.getKeyValueData().size(), 1);
		module = moduleList.get(FastQC.PlotType.OVERREPRESENTED_SEQUENCES);
		Assert.assertEquals(module.getDataPoints().size(), 0);
		Assert.assertEquals(module.getKeyValueData().size(), 0);
		Assert.assertEquals(module.getResult(), "pass");
		module = moduleList.get(FastQC.PlotType.PER_BASE_SEQUENCE_CONTENT);
		Assert.assertEquals(module.getName(), "Per base sequence content");
		Assert.assertEquals(module.getAttributes().size(), 5);
		
	}
	
	@Test (groups = "unit-tests")
	public void parseOutputTest() throws FastQCDataParseException, GridException, JSONException {
		BabrahamServiceImpl babrahamServiceImpl = new BabrahamServiceImpl();
		InputStream in = this.getClass().getResourceAsStream("/fastqc_data.txt");
		Map<String, FastQCDataModule> moduleList = null;
		try {
			moduleList = babrahamServiceImpl.processFastQCOutput(in);
		} catch (FastQCDataParseException e) {
			logger.warn(e.getLocalizedMessage());
		}
		Assert.assertNotNull(moduleList);
		
		GridResult result = new GridResultImpl();
		
		PowerMockito.when(mockBabrahamServiceImpl.parseFastQCOutput(result)).thenReturn(moduleList);
		
		FastQC fastQC = new FastQC();
		ReflectionTestUtils.setField(fastQC, "babrahamService", mockBabrahamServiceImpl);
		Map<String,JSONObject> output = fastQC.parseOutput(result);
		JSONObject jsonObject =  output.get(FastQC.PlotType.PER_BASE_QUALITY);
		logger.debug(jsonObject.toString());
		Assert.assertEquals(jsonObject.getJSONArray("dataSeries").length(), 2);
		Assert.assertEquals(jsonObject.getJSONObject("properties").getString("result"), "pass");
		
		
	}
}
