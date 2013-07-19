package edu.yu.einstein.wasp.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.charts.highchartsjs.HighChartsJsBase;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

@Controller
@Transactional
@RequestMapping("/jobresults")
public class ResultViewController extends WaspController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JobService	jobService;

	@Autowired
	private FilterService	filterService;

	@Autowired
	private AuthenticationService	authenticationService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileUrlResolver fileUrlResolver;

	@Autowired
	private FileService fileService;


	//get locale-specific message
	protected String getMessage(String key, String defaultMessage) {
		String r=getMessage(key);
		
		if (defaultMessage!=null && r!=null && r.equals(key)) return defaultMessage; 
		
		return r;
	}
	
	protected String getMessage(String key) {
		HttpSession session = this.request.getSession();
		
		Locale locale = (Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		return DBResourceBundle.MESSAGE_SOURCE.getMessage(key, null, locale);
	}


	@RequestMapping(value = "/treeview/{type}/{id}", method = RequestMethod.GET)
	public String treeView(@PathVariable("type") String type, @PathVariable("id") Integer id, ModelMap m) {
		
		if(type.equalsIgnoreCase("job")) {
			Job job = this.jobService.getJobDao().getById(id.intValue());
			
			m.addAttribute("myid", id.intValue());
			m.addAttribute("type", type);
			m.addAttribute("workflow", job.getWorkflow().getIName());
			m.addAttribute("wf_name", job.getWorkflow().getName());
		}
		
		return "jobresults/treeview"; 	
	}
	

	// get the JSON data to construct the tree 
	@RequestMapping(value="/getTreeJson", method = RequestMethod.GET)
	public @ResponseBody String getTreeJson(@RequestParam("node") String nodeJSON, HttpServletResponse response) {
		
		try {
/*			Map <String, Object> jsTree = null;
			if(type.equalsIgnoreCase("job")) {
				jsTree = this.jobService.getJobSampleD3Tree(id);
			} else if(type.equalsIgnoreCase("sample")) {
				;
			}
*/			
			JSONObject node = new JSONObject(nodeJSON);
			Integer id = node.getInt("myid");
			String type = node.getString("type");
			Integer pid = node.getInt("pid");
			Integer jid = node.getInt("jid");
			
			return outputJSON(jobService.getTreeViewBranch(id, pid, type, jid), response); 	
		} 
		catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON for " + nodeJSON, e);
		}	
	}

	@RequestMapping(value="/getDetailsJson", method = RequestMethod.GET)
	public @ResponseBody String getDetailsJson(@RequestParam("node") String nodeJSON, HttpServletResponse response) {
		
		HashMap<String, Object> jsDetailsTabs = new HashMap<String, Object>();

		LinkedHashMap<String, Object> jsDetails = new LinkedHashMap<String, Object>();
		
		try {
			JSONObject node = new JSONObject(nodeJSON);
			Integer id = node.getInt("myid");
			String type = node.getString("type");
			Integer pid = node.getInt("pid");
			Integer jid = node.getInt("jid");
			
			if(type.startsWith("job")) {
				Integer jobId = id;
				Job job = this.jobService.getJobDao().getById(jobId);
				if(job==null || job.getId()==null){
					  waspErrorMessage("listJobSamples.jobNotFound.label");
					  return null;
				}
				
				jsDetails.put(getMessage("job.name.label"), job.getName());
				
				// add job extra detail info
				HashMap<String, String> extraJobDetails = jobService.getExtraJobDetails(job);
				for (String lblEJD : extraJobDetails.keySet()) {
					try {
						String msg = getMessage(lblEJD);
						if (!msg.equals(lblEJD))
							jsDetails.put(msg, extraJobDetails.get(lblEJD));
					}
					catch (NoSuchMessageException e) {
						;
					}
				}
			
				// add job meta info
				List<JobMeta> metaList = job.getJobMeta();
				for (JobMeta mt : metaList) {
					String mKey = mt.getK();
					try {
						String msg = getMessage(mKey+".label");
						if (!msg.equals(mKey+".label"))
							jsDetails.put(msg, mt.getV());
					}
					catch (NoSuchMessageException e) {
						;
					}
				}
				
				// add job status message info
				List<MetaMessage> msgList = jobService.getUserSubmittedJobComment(jobId);
				for (MetaMessage msg : msgList) {
					jsDetails.put(msg.getName(), msg.getValue());
				}
				msgList = jobService.getAllFacilityJobComments(jobId);
				for (MetaMessage msg : msgList) {
					jsDetails.put(msg.getName(), msg.getValue());
				}
				
				jsDetailsTabs.put("job details", jsDetails);

			} else if(type.startsWith("sample") || type.startsWith("library") || type.startsWith("cell") || type.startsWith("pu")) {
				Integer sampleId = id;
				Sample sample = this.sampleService.getSampleById(sampleId);
				if(sample==null || sample.getId()==null){
					  waspErrorMessage("sampleDetail.sampleNotFound.error");
					  return null;
				}
				
				jsDetails.put(getMessage("sample.name.label"), sample.getName());
				
				// add sample meta info
				List<SampleMeta> metaList = sample.getSampleMeta();
				for (SampleMeta mt : metaList) {
					String mKey = mt.getK();
					try {
						String msg = getMessage(mKey+".label");
						if (!msg.equals(mKey+".label"))
							jsDetails.put(msg, mt.getV());
					}
					catch (NoSuchMessageException e) {
						;
					}
				}
				
				// add sample status message info
				List<MetaMessage> msgList = sampleService.getSampleQCComments(sampleId);
				for (MetaMessage msg : msgList) {
					jsDetails.put(msg.getName(), msg.getValue());
				}

			} else if(type.startsWith("filetype-")) {
				FileType ft = fileService.getFileType(id);
				Set<FileGroup> fgSet = new HashSet<FileGroup>();
				if (node.has("libid")) {
					Sample library = sampleService.getSampleById(node.getInt("libid"));
					if (node.has("cellid")) {
						Sample cell = sampleService.getSampleById(node.getInt("cellid"));
						fgSet.addAll(fileService.getFilesForCellLibraryByType(cell, library, ft));
					} else {
						fgSet.addAll(fileService.getFilesForLibraryByType(library, ft));
					}
				}

				for (FileGroup fg : fgSet) {
					jsDetails.putAll(fileService.getFileDetailsByFileType(fg));
					
//					Set<FileHandle> fhSet = fg.getFileHandles();
//					
//					for (FileHandle fh : fhSet) {
//						String mKey = mt.getK();
//						try {
//							String msg = getMessage(mKey+".label");
//							if (!msg.equals(mKey+".label"))
//								jsDetails.put(msg, mt.getV());
//						}
//						catch (NoSuchMessageException e) {
//							;
//						}
//					}
				}
			}
				
			// TODO: for testing only
			jsDetails.clear();
			jsDetailsTabs.clear();
			
			StringBuilder sb;
			WebContent webContent;
			WebPanel panel;
			int panelId = 1;
			
			sb = new StringBuilder();
			webContent = new WebContent();
			//webContent.setScriptDependencies(HighChartsJsBase.getScriptDependencies());
			sb.append("<table class='standardTable' >\n");
			sb.append("<tr>\n");
			sb.append("<th>FastQC Module</th>\n");
			sb.append("<th>Result</th>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/pass.png' height='20px'  border='0' >&nbsp;</td>\n");
			sb.append("<td>Per base sequence quality</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/pass.png' height='20px'  border='0' >&nbsp;</td>\n");
			sb.append("<td>Sequence Duplication Levels</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/pass.png' height='20px'  border='0' >&nbsp;</td>\n");
			sb.append("<td>Per sequence quality scores</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/warningAndComment.png' height='20px'  border='0' title='The difference between A and T, or G and C is greater than 10% in at least one position.' class='tooltip'>&nbsp;</td>\n");
			sb.append("<td>Per base sequence content</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/warningAndComment.png' height='20px'  border='0' title='The GC content of any base strays more than 5% from the mean GC content.' class='tooltip'>&nbsp;</td>\n");
			sb.append("<td>Per base GC content</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/warningAndComment.png' height='20px'  border='0' title='The sum of the deviations from the normal distribution represents more than 15% of the reads.' class='tooltip'>&nbsp;</td>\n");
			sb.append("<td>Per sequence GC content</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/pass.png' height='20px'  border='0' >&nbsp;</td>\n");
			sb.append("<td>Per base N content</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/failAndComment.png' height='20px'  border='0' title='At least one sequence is found to represent more than 1% of the total.' class='tooltip'>&nbsp;</td>\n");
			sb.append("<td>Overrepresented sequences</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/fail.png' height='20px'  border='0' >&nbsp;</td>\n");
			sb.append("<td>Kmer Content</td>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			sb.append("<td class='center'>&nbsp;<img src='http://localhost:8080/wasp/images/pass.png' height='20px'  border='0' >&nbsp;</td>\n");
			sb.append("<td>Sequence Length Distribution</td>\n");
			sb.append("</tr>\n");
			sb.append("</table>\n");
			webContent.setHtmlCode(sb.toString());
			jsDetails.put("panel"+panelId, new Panel("FastQC Results Summary", webContent));
			panelId++;

			sb = new StringBuilder();
			webContent = new WebContent();
			sb.append("<table class='keyValueTable' >\n");
			sb.append("<tr><th>Filename: </th><td>18_GCCAAT_L003_R1_003.fastq.gz</td></tr>\n");
			sb.append("<tr><th>File type: </th><td>Conventional base calls</td></tr>\n");
			sb.append("<tr><th>Encoding: </th><td>Sanger / Illumina 1.9</td></tr>\n");
			sb.append("<tr><th>Total Sequences: </th><td>4,000,000</td></tr>\n");
			sb.append("<tr><th>Filtered Sequences: </th><td>0</td></tr>\n");
			sb.append("<tr><th>Sequence length: </th><td>101</td></tr>\n");
			sb.append("<tr><th>%GC: </th><td>42</td></tr>\n");
			sb.append("</table>\n");
			webContent.setHtmlCode(sb.toString());
			jsDetails.put("panel"+panelId, new Panel("Basic Statistics", webContent));
			panelId++;

			sb = new StringBuilder();
			webContent = new WebContent();
			webContent.setScriptDependencies(HighChartsJsBase.getScriptDependencies());
			webContent.setHtmlCode("<div id='highchart_contents_1e58dcd5-deb1-4865-a473-f49d1fc9c7e2' style='margin: auto'></div>");
			panel = new WebPanel("Quality scores across all bases", webContent);
			sb.append("$('#highchart_contents_1e58dcd5-deb1-4865-a473-f49d1fc9c7e2').highcharts({\n");
			sb.append("chart: { type: 'boxplot' },\n");
			sb.append("title: { text: 'Quality scores across all bases' },\n");
			sb.append("legend: { enabled: false },\n");
			sb.append("xAxis: { categories: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100', '101'],\n");
			sb.append("tickInterval: 5,\n");
			sb.append("title: { text: 'position in read (bp)' }\n");
			sb.append("},\n");
			sb.append("plotOptions: { series: { groupPadding: 0} },\n");
			sb.append("yAxis: { title: { text: 'Quality Score' },\n");
			sb.append(" plotBands: [{ color: '#F6CECE', from: 0, to: 20 },{ color: '#F5ECCE', from: 20, to: 28 },{ color: '#CEF6CE', from: 28, to: 100 }]},\n");
			sb.append("series: [{ name: 'box and whisker', animation:false, marker: { enabled: false },\n");
			sb.append(" data: [[30,31,31,34,34],[30,31,34,34,34],[30,31,34,34,34],[35,35,37,37,37],[35,35,37,37,37],[35,35,37,37,37],[33,35,37,37,37],[33,35,37,37,37],[34,37,39,39,39],[34,37,39,39,39],[34,37,39,39,39],[34,37,39,39,39],[34,37,39,39,39],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[34,38,40,41,41],[33,38,40,41,41],[33,38,40,41,41],[33,38,40,41,41],[33,38,40,41,41],[33,38,40,41,41],[33,38,40,41,41],[33,38,40,41,41],[33,37,40,41,41],[33,37,40,41,41],[32,37,40,41,41],[32,37,40,41,41],[32,37,40,41,41],[32,37,40,41,41],[32,37,40,41,41],[31,37,39,41,41],[31,37,39,41,41],[31,37,40,41,41],[32,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,39,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,37,40,41,41],[31,36,40,41,41],[31,36,39,41,41],[31,36,39,41,41],[31,36,39,41,41],[31,36,39,41,41],[31,36,39,41,41],[31,35,39,41,41],[30,35,39,41,41],[30,35,39,40,41],[30,35,38,40,41],[30,35,38,40,41],[29,35,38,40,41],[29,35,38,40,41],[29,34,37,40,41],[29,34,37,40,41],[29,34,37,39,41],[29,34,37,39,41],[26,31,34,37,39],[27,32,35,38,39],[28,33,36,38,39],[28,33,36,38,39],[28,33,35,37,39],[28,33,35,37,39],[28,33,35,37,39],[27,33,35,37,39],[27,33,35,37,39],[27,33,35,36,37],[26,33,35,36,37],[26,33,35,36,37],[26,33,35,36,37],[26,33,35,35,37],[26,33,35,35,37],[26,32,35,35,36],[26,32,35,35,36],[26,32,35,35,36],[26,32,35,35,36],[25,32,35,35,36],[25,32,35,35,35],[25,32,35,35,35],[25,32,35,35,35],[24,32,35,35,35],[23,32,34,35,35],[18,30,34,35,35]]}\n");
			sb.append(",{ name: 'running-mean', type: 'spline', color: '#ff0000', animation:false, marker: { enabled: false },\n");
			sb.append(" data: [[31.8378925],[32.2102505],[32.33732375],[35.91921925],[35.8069285],[35.83178],[35.8074585],[35.78890875],[37.379208],[37.42432075],[37.380319],[37.38953225],[37.31582175],[38.63662825],[38.67141675],[38.63422875],[38.64090475],[38.569137],[38.57848375],[38.5991495],[38.61106125],[38.389544],[38.42186725],[38.38125825],[38.371736],[38.273545],[38.211948],[38.18542075],[38.16447275],[38.0693955],[38.036314],[38.0371515],[37.98100575],[37.910095],[37.705039],[37.70712],[37.6744145],[37.62871525],[37.56308],[37.2969565],[37.35704125],[37.46556375],[37.509774],[37.49612225],[37.44374075],[37.32815375],[37.332724],[37.3333655],[37.33213125],[37.29914925],[37.29283075],[37.34403775],[37.44475375],[37.45531225],[37.41344125],[37.404608],[37.3323595],[37.29965675],[37.2375485],[37.15850075],[37.084267],[36.98939325],[36.839005],[36.77382975],[36.664778],[36.5381015],[36.381161],[36.23773025],[36.13594075],[35.9719915],[35.83013875],[35.63864025],[35.45210025],[35.2279095],[34.97164],[32.5550445],[33.5355905],[33.94552075],[34.0711365],[33.917415],[33.784353],[33.597055],[33.30199625],[33.13478325],[32.98222475],[32.78550825],[32.61304775],[32.498439],[32.350318],[32.212541],[32.07310575],[32.01368],[31.916857],[31.73667275],[31.604522],[31.49697625],[31.36485825],[31.15081575],[31.00425075],[30.82553925],[29.88280975]]}\n");
			sb.append("]\n");
			sb.append("});\n");
			panel.setExecOnRenderCode(sb.toString());
			panel.setExecOnResizeCode(sb.toString());
			panel.setExecOnExpandCode(sb.toString());
			jsDetails.put("panel"+panelId, panel);
			panelId++;
			
			sb = new StringBuilder();
			webContent = new WebContent();
			webContent.setScriptDependencies(HighChartsJsBase.getScriptDependencies());
			webContent.setHtmlCode("<div id='highchart_contents_954d1749-770c-4d52-bd11-3e3097b9d91e' style='margin: auto'></div>");
			panel = new WebPanel("Quality Score Distribution Over all Sequences", webContent);
			sb.append("$('#highchart_contents_954d1749-770c-4d52-bd11-3e3097b9d91e').highcharts({\n");
			sb.append("chart: { type: 'spline' },\n");
			sb.append("title: { text: 'Quality Score Distribution Over all Sequences' },\n");
			sb.append("legend: { enabled: true },\n");
			sb.append("xAxis: { title: { text: 'position in read (bp)' }\n");
			sb.append("},\n");
			sb.append("yAxis: { min: 0,\n");
			sb.append("max: 100,\n");
			sb.append("title: { text: 'proportion of base (%)' }\n");
			sb.append("},\n");
			sb.append("series: [{ name: '% T', color: '#00ff00', animation:false, marker: { enabled: false },\n");
			sb.append(" data: [[28.86834619002161],[30.091125],[30.70175],[29.871350000000003],[30.649625000000004],[29.046224999999996],[31.027475],[30.544624999999996],[30.702724999999997],[29.614974999999998],[29.940375000000003],[30.265925],[30.699025000000002],[30.51125],[30.754025000000002],[30.739775],[30.678499999999996],[30.568075],[30.525025],[30.500125],[30.506725000000003],[30.513056795698784],[30.786350000000002],[30.619176145170613],[30.77089427235681],[30.707475000000002],[30.697115625031252],[30.599175000000002],[30.543799999999997],[30.516650000000002],[30.72265],[30.65675],[30.621150000000004],[30.585974999999998],[30.799175],[30.655424999999997],[30.59095],[30.487877138536167],[30.505488480793318],[30.538433520586132],[30.50144279386136],[30.552249658042967],[30.567328377529275],[30.54079758830363],[30.603801754605918],[30.476326711488078],[30.5667],[30.472552362761814],[30.393825000000003],[30.39855],[30.507475],[30.469849999999997],[30.475324999999998],[30.405074999999997],[30.449625],[30.425025],[30.371925],[30.244725],[30.0292],[29.9128],[29.9329],[29.881400000000003],[29.923875],[29.81315],[29.804199999999998],[29.494925],[29.3407],[29.01265],[28.85535],[28.676800000000004],[28.55225],[28.46285],[28.204600000000003],[28.078375],[27.728825],[27.464149999999997],[27.08105],[26.796325],[26.625],[26.320175000000003],[26.147198051992692],[25.856375],[25.651689753074937],[25.447231954590027],[25.228825],[25.07591253795627],[24.7976],[24.623675],[24.452125],[24.207075],[24.1088],[23.900199999999998],[23.818649999999998],[23.708025],[23.578025],[23.37235],[23.23085],[23.0961],[23.002924999999998],[22.96615],[22.8779]]}\n");
			sb.append(",{ name: '% A', color: '#0000ff', animation:false, marker: { enabled: false },\n");
			sb.append(" data: [[27.79838490442139],[29.148425],[29.1765],[28.65375],[29.601499999999998],[29.46665],[28.93295],[28.5389],[28.410025],[29.121225],[28.673975000000002],[28.435575],[28.585575000000002],[28.630325],[28.57225],[28.48845],[28.666399999999996],[28.4564],[28.46575],[28.336325000000002],[28.353775],[28.463049620673676],[28.4698],[28.470012460224286],[28.48291207280182],[28.469050000000003],[28.446348025243285],[28.329025],[28.4017],[28.310750000000002],[28.337899999999998],[28.449400000000004],[28.421975],[28.460075],[28.519425],[28.433025],[28.5352],[28.463104133576117],[28.458638304513638],[28.399143653651027],[28.522936344445206],[28.333940283602203],[28.463309541903836],[28.441122445685913],[28.49877014422183],[28.616914965023494],[28.478199999999998],[28.52639263196316],[28.403624999999998],[28.414624999999997],[28.44505],[28.510125],[28.585175000000003],[28.599375],[28.695300000000003],[28.606125],[28.516724999999997],[28.480850000000004],[28.540100000000002],[28.662399999999998],[28.827925],[28.8662],[28.881],[28.93755],[29.019325000000002],[28.979575],[29.080650000000002],[29.20355],[29.310049999999997],[29.365099999999998],[29.427500000000002],[29.417675],[29.608224999999997],[29.583025000000003],[29.673525],[29.726950000000002],[29.824650000000002],[29.889575],[29.900675],[30.026625000000003],[30.00333751251567],[30.007450000000002],[30.12748777675438],[30.08918387949876],[30.0879],[30.08629004314502],[30.060150000000004],[30.097025],[30.084699999999998],[30.09525],[30.0668],[30.023325],[29.92],[29.895100000000003],[29.8408],[29.8491],[29.8611],[29.84175],[29.8194],[29.6556],[29.6279]]}\n");
			sb.append(",{ name: '% C', color: '#000000', animation:false, marker: { enabled: false },\n");
			sb.append(" data: [[22.949257603753612],[20.791825],[20.296625],[20.123375],[19.37845],[20.472475000000003],[20.018625],[20.594725],[20.768449999999998],[21.024475000000002],[20.802200000000003],[20.807425],[20.58825],[20.844725],[20.738025],[20.82145],[20.779575],[20.8199],[20.767875],[20.87115],[20.8781],[20.65804730316556],[20.61195],[20.69602252840551],[20.60129003225081],[20.612],[20.6102543330633],[20.630850000000002],[20.602024999999998],[20.74115],[20.61435],[20.538500000000003],[20.562],[20.497225],[20.4313],[20.518675],[20.588324999999998],[20.60955203028584],[20.590281807376932],[20.557090423690312],[20.453047780068882],[20.487036918845117],[20.52483111071185],[20.562874278466555],[20.592032476500858],[20.51367399874559],[20.61535],[20.52925264626323],[20.593725],[20.4877],[20.49045],[20.47165],[20.41515],[20.445050000000002],[20.38925],[20.31415],[20.4322],[20.3852],[20.55605],[20.46675],[20.340925000000002],[20.186999999999998],[20.171425000000003],[20.14425],[20.0842],[20.235300000000002],[20.132375],[20.226825],[20.058325],[20.09515],[20.012],[20.084775],[20.098925],[20.13555],[20.3109],[20.30775],[20.434925],[20.41275],[20.52665],[20.573150000000002],[20.734952756072836],[20.876475],[21.065735009835603],[21.23494872026933],[21.401375],[21.536685768342885],[21.786675],[21.933975],[22.172075],[22.42725],[22.59775],[22.85425],[23.21],[23.402525],[23.6895],[23.910925],[24.181325],[24.435575],[24.58955],[24.8604],[25.121149999999997]]}\n");
			sb.append(",{ name: '% G', color: '#ff0000', animation:false, marker: { enabled: false },\n");
			sb.append(" data: [[20.384011301803383],[19.968625],[19.825125],[21.351525],[20.370425],[21.014650000000003],[20.020950000000003],[20.321749999999998],[20.1188],[20.239325],[20.58345],[20.491075],[20.12715],[20.0137],[19.9357],[19.950325],[19.875525],[20.155625],[20.24135],[20.2924],[20.2614],[20.365846280461984],[20.131899999999998],[20.214788866199594],[20.144903622590565],[20.211475],[20.246282016662164],[20.44095],[20.452475],[20.43145],[20.3251],[20.35535],[20.394875000000003],[20.456725000000002],[20.2501],[20.392875],[20.285525],[20.43946669760188],[20.44559140731611],[20.505332402072522],[20.522573081624557],[20.62677313950971],[20.444530969855045],[20.4552056875439],[20.30539562467139],[20.39308432474284],[20.339750000000002],[20.471802359011797],[20.608825],[20.699125],[20.557025],[20.548375],[20.52435],[20.5505],[20.465825000000002],[20.654700000000002],[20.67915],[20.889225],[20.87465],[20.95805],[20.898249999999997],[21.0654],[21.0237],[21.10505],[21.092275],[21.290200000000002],[21.446275],[21.556975],[21.776275000000002],[21.86295],[22.00825],[22.034699999999997],[22.088250000000002],[22.203049999999998],[22.286749999999998],[22.50115],[22.659375],[22.90135],[22.947675],[23.08005],[23.114511679418797],[23.2597],[23.155087460335082],[23.228635445641885],[23.2819],[23.301111650555825],[23.355575],[23.345325],[23.2911],[23.270425],[23.22665],[23.222224999999998],[23.05135],[22.99435],[22.891675],[22.867625],[22.726725000000002],[22.626575],[22.588125],[22.51785],[22.37305]]}\n");
			sb.append("]\n");
			sb.append("});\n");
			panel.setExecOnRenderCode(sb.toString());
			panel.setExecOnResizeCode(sb.toString());
			panel.setExecOnExpandCode(sb.toString());
			jsDetails.put("panel"+panelId, panel);
			panelId++;
			
			sb = new StringBuilder();
			webContent = new WebContent();
			webContent.setScriptDependencies(HighChartsJsBase.getScriptDependencies());
			webContent.setHtmlCode("<div id='highchart_contents_9fde1169-1c53-4ddb-adec-1de0834b5e7f' style='margin: auto'></div>");
			panel = new WebPanel("Quality Score Distribution Over all Sequences", webContent);
			sb.append("$('#highchart_contents_9fde1169-1c53-4ddb-adec-1de0834b5e7f').highcharts({\n");
			sb.append("chart: { type: 'spline' },\n");
			sb.append("title: { text: 'Quality Score Distribution Over all Sequences' },\n");
			sb.append("legend: { enabled: false },\n");
			sb.append("xAxis: { categories: ['2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40'],\n");
			sb.append("tickInterval: 2,\n");
			sb.append("title: { text: 'Quality Score' }\n");
			sb.append("},\n");
			sb.append("yAxis: { min: 0,\n");
			sb.append("title: { text: 'Sequence Count' }\n");
			sb.append("},\n");
			sb.append("series: [{ name: 'average quality per read', color: '#ff0000', animation:false, marker: { enabled: false },\n");
			sb.append(" data: [[19],[14],[27],[70],[155],[390],[909],[1498],[2069],[2944],[3768],[4504],[5050],[5574],[6153],[7096],[8025],[9357],[10212],[11768],[13219],[15996],[19402],[24438],[30856],[39983],[51479],[65019],[83365],[103276],[128029],[161397],[208784],[278670],[404476],[704103],[1178270],[409133],[503]]}\n");
			sb.append("]\n");
			sb.append("});\n");
			panel.setExecOnRenderCode(sb.toString());
			panel.setExecOnResizeCode(sb.toString());
			panel.setExecOnExpandCode(sb.toString());
			jsDetails.put("panel"+panelId, panel);
			panelId++;
			
			sb = new StringBuilder();
			webContent = new WebContent();
			webContent.setScriptDependencies(HighChartsJsBase.getScriptDependencies());
			webContent.setHtmlCode("<div id='highchart_contents_ebe17501-3d7f-417a-8eb5-8ac9cefa985d' style='margin: auto'></div>");
			panel = new WebPanel("Per Base GC Content", webContent);
			sb.append("$('#highchart_contents_ebe17501-3d7f-417a-8eb5-8ac9cefa985d').highcharts({\n");
			sb.append("chart: { type: 'spline' },\n");
			sb.append("title: { text: 'Per Base GC Content' },\n");
			sb.append("legend: { enabled: false },\n");
			sb.append("xAxis: { categories: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90', '91', '92', '93', '94', '95', '96', '97', '98', '99', '100', '101'],\n");
			sb.append("tickInterval: 5,\n");
			sb.append("title: { text: 'position in read (bp)' }\n");
			sb.append("},\n");
			sb.append("yAxis: { min: 0,\n");
			sb.append("max: 100,\n");
			sb.append("title: { text: '% GC' }\n");
			sb.append("},\n");
			sb.append("series: [{ name: '% GC', color: '#ff0000', animation:false, marker: { enabled: false },\n");
			sb.append(" data: [[43.333268905557],[40.76045],[40.12175],[41.4749],[39.748875],[41.487125000000006],[40.039575],[40.916475000000005],[40.88725],[41.2638],[41.38565],[41.2985],[40.7154],[40.858425],[40.673725],[40.771775],[40.6551],[40.975525000000005],[41.009225],[41.16355],[41.1395],[41.02389358362754],[40.743849999999995],[40.9108113946051],[40.74619365484137],[40.823474999999995],[40.85653634972547],[41.0718],[41.0545],[41.172599999999996],[40.93945],[40.89385],[40.956875],[40.95395],[40.681400000000004],[40.911550000000005],[40.87385],[41.04901872788772],[41.03587321469304],[41.06242282576284],[40.975620861693436],[41.11381005835483],[40.96936208056689],[41.018079966010454],[40.89742810117225],[40.90675832348843],[40.9551],[41.00105500527503],[41.20255],[41.186825],[41.047475],[41.020025],[40.9395],[40.995549999999994],[40.855075],[40.96885],[41.11135],[41.274425],[41.4307],[41.4248],[41.239175],[41.2524],[41.195125],[41.2493],[41.176475],[41.5255],[41.57865],[41.7838],[41.8346],[41.9581],[42.02025],[42.119475],[42.187174999999996],[42.3386],[42.59765],[42.8089],[43.094300000000004],[43.314099999999996],[43.474325],[43.6532],[43.84946443549163],[44.136175],[44.22082247017068],[44.46358416591122],[44.683275],[44.83779741889871],[45.14225],[45.2793],[45.463175],[45.697675],[45.8244],[46.076475],[46.26135],[46.396875],[46.581175],[46.77855],[46.90805],[47.06215],[47.177675],[47.37825],[47.4942]]}\n");
			sb.append("]\n");
			sb.append("});\n");
			panel.setExecOnRenderCode(sb.toString());
			panel.setExecOnResizeCode(sb.toString());
			panel.setExecOnExpandCode(sb.toString());
			jsDetails.put("panel"+panelId, panel);
			panelId++;
			
			jsDetailsTabs.put("FastQC Results", jsDetails);
			return outputJSON(jsDetailsTabs, response);
		} 
		catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON for " + nodeJSON, e);
		}	
	}
}

