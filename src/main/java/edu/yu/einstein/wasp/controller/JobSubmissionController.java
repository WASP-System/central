package edu.yu.einstein.wasp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaHelper;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobDraftMetaService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobMetaService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobUserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.SampleDraftMetaService;
import edu.yu.einstein.wasp.service.SampleDraftService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;


@Controller
@Transactional
@RequestMapping("/jobsubmit")
public class JobSubmissionController extends WaspController {

	@Autowired
	private JobDraftService jobDraftService;

	@Autowired
	private JobDraftMetaService jobDraftMetaService;

	@Autowired
	private SampleDraftService sampleDraftService;

	@Autowired
	private SampleDraftMetaService sampleDraftMetaService;


	@Autowired
	private JobService jobService;

	@Autowired
	private JobUserService jobUserService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private JobMetaService jobMetaService;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private SampleMetaService sampleMetaService;
	
	@Autowired
	private MetaValidator metaValidator;

	@Autowired
	private TypeSampleService typeSampleService;  
	
	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private File sampleDir;
	
	@Autowired
	private FileService fileService;
	
	MetaHelper metaHelper = new MetaHelper("jobDraft", JobDraft.class);


	@RequestMapping(value="/list", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public String list(ModelMap m) {
		User me = getAuthenticatedUser();

		Map jobDraftQueryMap = new HashMap();
		jobDraftQueryMap.put("UserId", me.getUserId());
		jobDraftQueryMap.put("status", "PENDING");

		List<JobDraft> jobDraftList = jobDraftService.findByMap(jobDraftQueryMap);

		m.put("jobdrafts", jobDraftList); 

		return "jobsubmit/list";
	}

	protected void generateCreateForm(ModelMap m) {
		User me = getAuthenticatedUser();

		List <LabUser> labUserAllRoleList = me.getLabUser();

		List <Lab> labList = new ArrayList();
		for (LabUser lu: labUserAllRoleList) {
			String roleName =	lu.getRole().getRoleName();

			if (roleName.equals("lu") ||
					roleName.equals("lm") ||
					roleName.equals("pi")) {
				labList.add(lu.getLab());
			}
		}

		List <Workflow> workflowList = workflowService.findAll();

		m.put("labs", labList); 
		m.put("workflows", workflowList); 
	}

	@RequestMapping(value="/create.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public String showCreateForm(ModelMap m) {

		generateCreateForm(m);
		m.put("jobDraft", new JobDraft()); 

		return "jobsubmit/create";
	}

	@RequestMapping(value="/create.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('lu-*')")
	public String create (
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		User me = getAuthenticatedUser();

		jobDraftForm.setUserId(me.getUserId());
		jobDraftForm.setStatus("PENDING");
		jobDraftForm.setCreatets(new Date());

		String rt = doModify(jobDraftForm, result, status, m); 

		// Adds the jobdraft to authorized list
 		doReauth();

		return rt;
	}

	@RequestMapping(value="/modify/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modify(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
	
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		// check if i am the drafter
		User me = getAuthenticatedUser();
		if (me.getUserId() != jobDraft.getUserId()) {
			return "hello";
		}

		// check that the status is PENDING
		if (! jobDraft.getStatus().equals("PENDING")) {
			return "hello";
		}

		generateCreateForm(m);
		m.put("jobDraft", jobDraft);

		return "jobsubmit/create";
	}


	@RequestMapping(value="/modify/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modify (
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		JobDraft jobDraftDb = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		// check if i am the drafter
		User me = getAuthenticatedUser();
		if (me.getUserId() != jobDraftDb.getUserId()) {
			return "hello";
		}

		if (! jobDraftDb.getStatus().equals("PENDING")) {
			return "hello";
		}

		jobDraftDb.setName(jobDraftForm.getName());
		jobDraftDb.setWorkflowId(jobDraftForm.getWorkflowId());
		jobDraftDb.setLabId(jobDraftForm.getLabId());


		// TODO CHECK PERMS IT IS MY JOB
		return doModify(jobDraftDb, result, status, m); 
	}

	public String doModify (
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		// TODO CHECK ACCESS OF LABUSER

		Errors errors = new BindException(result.getTarget(), "jobDraft");
		result.addAllErrors(errors);

		if (result.hasErrors()) {
			waspMessage("hello.error");
			generateCreateForm(m);
			return "jobsubmit/create";
		}

		JobDraft jobDraftDb = jobDraftService.save(jobDraftForm); 

		return "redirect:/jobsubmit/modifymeta/" + jobDraftDb.getJobDraftId() + ".do";
	}


	@RequestMapping(value="/modifymeta/{jobDraftId}", method=RequestMethod.GET)
	//@PreAuthorize("hasRole('jd-' + #jobDraftId)") TODO: uncomment
	public String showModifyMetaForm(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		metaHelper.setBundle(getBundle());

		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		metaHelper.setArea(jobDraft.getWorkflow().getIName());

		jobDraft.setJobDraftMeta(metaHelper.getMasterList(JobDraftMeta.class));

		m.put("jobDraftDb", jobDraft);
		m.put("jobDraft", jobDraft);
		m.put("area", metaHelper.getArea());
		m.put("parentarea", metaHelper.getParentArea());

		return "jobsubmit/metaform";
	}
	
	@RequestMapping(value="/modifymeta/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('lu-*')")
	public String showModifyMetaForm(
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		jobDraftForm.setJobDraftId(jobDraftId);
		jobDraftForm.setUserId(jobDraft.getUserId());
		jobDraftForm.setLabId(jobDraft.getLabId());
		jobDraftForm.setWorkflowId(jobDraft.getWorkflowId());

		metaHelper.setBundle(getBundle());
		metaHelper.setArea(jobDraft.getWorkflow().getIName());

		List<JobDraftMeta> jobDraftMetaList = metaHelper.getFromRequest(request, JobDraftMeta.class);

		jobDraftForm.setJobDraftMeta(jobDraftMetaList);

		metaHelper.validate(jobDraftMetaList, result);

		if (result.hasErrors()) {
			waspMessage("hello.error");

			m.put("jobDraftDb", jobDraft);
			m.put("area", metaHelper.getArea());
			m.put("parentarea", metaHelper.getParentArea());

			return "jobsubmit/metaform";
		}

		jobDraftMetaService.updateByJobdraftId(jobDraftId, jobDraftMetaList);



		// TODO SHOULD ACTUALLY FORWARD
	  MetaHelper sampleMetaHelper = new MetaHelper("sampleDraft", SampleDraftMeta.class);

	  sampleMetaHelper.getMasterList(JobDraftMeta.class);

	  m.addAttribute("_metaList", sampleMetaHelper.getMasterList(SampleDraftMeta.class));
	  m.addAttribute(JQFieldTag.AREA_ATTR, "sampleDraft");		
	  prepareSelectListData(m);
	  m.addAttribute("jobdraftId",jobDraftId);
	  m.addAttribute("uploadStartedMessage",getMessage("sampleDraft.fileupload.started"));
	  return "jobsubmit-sample";
	}
	

	@RequestMapping(value="/verify/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showJobDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		metaHelper.setBundle(getBundle());
		metaHelper.setArea(jobDraft.getWorkflow().getIName());

    jobDraft.setJobDraftMeta(metaHelper.syncWithMaster(jobDraft.getJobDraftMeta()));

		m.put("jobDraft", jobDraft);

		return "jobsubmit/verify";
	}

	@RequestMapping(value="/submit/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String submitJob(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		User me = getAuthenticatedUser();

		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		metaHelper.setBundle(getBundle());
		metaHelper.setArea(jobDraft.getWorkflow().getIName());

    jobDraft.setJobDraftMeta(metaHelper.syncWithMaster(jobDraft.getJobDraftMeta()));


		// Copies JobDraft to a new Job
		Job job = new Job();
		job.setUserId(me.getUserId());
		job.setLabId(jobDraft.getLabId());
		job.setName(jobDraft.getName());
		job.setWorkflowId(jobDraft.getWorkflowId());
		job.setIsActive(1);
		job.setCreatets(new Date());

		job.setViewablebylab(0); // Todo: get from lab? // really not being used yet

		Job jobDb = jobService.save(job); 

		// Saves the metadata
		for (JobDraftMeta jdm: jobDraft.getJobDraftMeta()) {
			JobMeta jobMeta = new JobMeta();
			jobMeta.setJobId(jobDb.getJobId());
			jobMeta.setK(jdm.getK());
			jobMeta.setV(jdm.getV());

			jobMetaService.save(jobMeta); 
		}

		// Creates the JobUser Permission
		JobUser jobUser = new JobUser(); 
		jobUser.setUserId(me.getUserId());
		jobUser.setJobId(jobDb.getJobId());
		Role role = roleService.getRoleByRoleName("js");
		jobUser.setRoleId(role.getRoleId());
		jobUserService.save(jobUser);

		// update the jobdraft
		jobDraft.setStatus("SUBMITTED");
		jobDraft.setSubmittedjobId(jobDb.getJobId());
		jobDraftService.save(jobDraft); 

		// TODO!!! ADD!!! WORKFLOW!!! STEPS!!!

		// Adds new Job to Authorized List
		doReauth();

		return "jobsubmit/ok";
	}


    @RequestMapping(value="/listSampleDraftsJSON", method=RequestMethod.GET)	
	public @ResponseBody String getSampleDraftListJSON(@RequestParam("jobdraftId") Integer jobdraftId) {
	
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<SampleDraft> drafts=sampleDraftService.getSampleDraftByJobId(jobdraftId);


		ObjectMapper mapper = new ObjectMapper();
   	
		try {
			//String users = mapper.writeValueAsString(userList);
			jqgrid.put("page","1");
			jqgrid.put("records",drafts.size()+"");
			jqgrid.put("total",drafts.size()+"");
			
			
			List<Map> rows = new ArrayList<Map>();
			
			Map<Integer, String> allSampleTypes=new TreeMap<Integer, String>();
			for(TypeSample type:(List<TypeSample>)typeSampleService.findAll()) {
				allSampleTypes.put(type.getTypeSampleId(),type.getName());
			}
			
			for (SampleDraft draft : drafts) {
				Map cell = new HashMap();
				cell.put("id", draft.getSampleDraftId());
			
				MetaHelper sampleMetaHelper = new MetaHelper("sampleDraft", SampleDraftMeta.class);

				List<SampleDraftMeta> draftMeta=sampleMetaHelper.syncWithMaster(draft.getSampleDraftMeta());
				String fileCell="";
				if (draft.getFile()!=null ) {
					String fileName=draft.getFile().getFilelocation();
					if (fileName!=null && ( fileName.indexOf('/')>-1 || fileName.indexOf('\\')>-1)) {
						int idx = fileName.lastIndexOf('/');
						if (idx==-1) idx = fileName.lastIndexOf('\\');
						fileName=fileName.substring(idx+1);						
					}
					
					if (fileName!=null && fileName.indexOf('.')>-1) {
						int idx = fileName.lastIndexOf('.');
						fileName=fileName.substring(0,idx);		
					}
					
					fileCell="<a href='/wasp/jobsubmit/downloadFile.do?id="+draft.getFile().getFileId()+"'>"+fileName+"</a> "+draft.getFile().getSizek()+"kB";
				}
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						draft.getName(),
						allSampleTypes.get(draft.getTypeSampleId()),							
						draft.getStatus(),
						fileCell
							
				})); 
			
				for(SampleDraftMeta meta:draftMeta) {
					cellList.add(meta.getV());
				}
			
				cell.put("cell", cellList);
			 
				rows.add(cell);
			}

			jqgrid.put("rows",rows);
		
			String json=mapper.writeValueAsString(jqgrid);
		
			return json;
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+drafts,e);
		}
	}


    
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)		
    public String uploadFile(@RequestParam("id") Integer sampleDraftId,SampleDraft sampleDraftForm, ModelMap m, HttpServletResponse response) {
	   	
    	String jsCallback="<html>\n"+
"<head>\n"+
"<script type='text/javascript'>\n"+
"function init() {\n"+
	"if(top.uploadDone) top.uploadDone('{message}'); \n"+
"}\n"+
"window.onload=init;\n"+
"</script>\n"+
"<body>\n"+
"</body>\n";
 
    	
		try {
			
			if (sampleDraftForm.getFileData()!=null) {//uploading just file				
			    
			    CommonsMultipartFile fileData=sampleDraftForm.getFileData();
			    
			    SampleDraft samplDraft=sampleDraftService.findById(sampleDraftId);
			    
			    Lab lab=samplDraft.getLab();
			    
			    User piUser=userService.getById(lab.getPrimaryUserId());
			    
			    final String login = 
			    ((org.springframework.security.core.userdetails.User)
			    		SecurityContextHolder.getContext().getAuthentication().getPrincipal()
			    ).getUsername();
			   			   
			    String[] path=new String[] {
			    		this.sampleDir.toString(),
			    		piUser.getLogin(),
			    		login,
			    		"jobdraft_"+sampleDraftForm.getJobdraftId(),
			    		fileData.getOriginalFilename()+"."+System.currentTimeMillis()
			    		};
			    
			    String destStr=StringUtils.join(path, System.getProperty("file.separator"));
			    
			    File dest=new File(destStr);
			    
			    FileUtils.forceMkdir(dest);
			    
			    fileData.transferTo(dest);
			    			 	
			    edu.yu.einstein.wasp.model.File file = new edu.yu.einstein.wasp.model.File();
			    
			    file.setFilelocation(dest.getAbsolutePath());
			    file.setIsActive(1);
			    file.setContenttype("?");
			    file.setMd5hash("xxx");
			    file.setSizek((int)(fileData.getSize()/1024));
			    			    
			    fileService.persist(file);
			    
			    samplDraft.setFileId(file.getFileId());
			    
			    sampleDraftService.merge(samplDraft);
			    			  
			    jsCallback=jsCallback.replace("{message}",getMessage("sampleDraft.fileupload.done"));
			    response.setContentType( "text/html; charset=UTF-8" );
			    response.getWriter().print(jsCallback);
			  
			} else {
				jsCallback=jsCallback.replace("{message}",getMessage("sampleDraft.fileupload.empty"));
			    response.setContentType( "text/html; charset=UTF-8" );
			    response.getWriter().print(jsCallback);				
			}
		} catch(Throwable e) {
			throw new IllegalStateException("cant get file to upload",e);
		}		
		return null;
	}
  
    @RequestMapping(value = "/updateSampleDrafts", method = RequestMethod.POST)		
    public String updateDetailJSON(@RequestParam("id") Integer sampleDraftId,SampleDraft sampleDraftForm, ModelMap m, HttpServletResponse response) {


    MetaHelper sampleMetaHelper = new MetaHelper("sampleDraft", SampleDraftMeta.class);	
	List<SampleDraftMeta> sampleDraftMetaList = sampleMetaHelper.getFromJsonForm(request, SampleDraftMeta.class);
	
	sampleDraftForm.setSampleDraftMeta(sampleDraftMetaList);

	boolean adding=sampleDraftId==0;
	
	//get from jobdraft table
	JobDraft jd=jobDraftService.findById(sampleDraftForm.getJobdraftId());
	sampleDraftForm.setUserId(jd.getUserId());
	sampleDraftForm.setLabId(jd.getLabId());
	
	
	if (adding) {
				
		SampleDraft sampleDraftDb = this.sampleDraftService.save(sampleDraftForm);
		
		sampleDraftId=sampleDraftDb.getSampleDraftId();
	} else {
		
		SampleDraft sampleDraftDb = this.sampleDraftService.getById(sampleDraftId);
		
		sampleDraftDb.setName(sampleDraftForm.getName());			
		sampleDraftDb.setStatus(sampleDraftForm.getStatus());
		sampleDraftDb.setTypeSampleId(sampleDraftForm.getTypeSampleId());

		this.sampleDraftService.merge(sampleDraftDb);
	}


	for (SampleDraftMeta meta : sampleDraftMetaList) {
		meta.setSampledraftId(sampleDraftId);
	}

	sampleDraftMetaService.updateBySampledraftId(sampleDraftId, sampleDraftMetaList);
	
	try {			
		response.getWriter().println(sampleDraftId+"|"+(adding?getMessage("sampleDraft.created.success"):getMessage("sampleDraft.updated.success")));		
		return null;
		
	} catch (Throwable e) {
		throw new IllegalStateException("Cant output success message ",e);
	}
}
	
	
	
	@RequestMapping(value = "/deleteSampleDraftJSON", method = RequestMethod.DELETE)	
	public String deleteSampleDraftJSON(@RequestParam("id") Integer sampleDraftId,HttpServletResponse response) {

		sampleDraftMetaService.updateBySampledraftId(sampleDraftId, new ArrayList<SampleDraftMeta>());
		this.sampleDraftService.remove(sampleDraftService.findById(sampleDraftId));
		
		try {
			response.getWriter().println(getMessage("sampleDraft.removed.success"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}
	}
	
	@RequestMapping(value = "/downloadFile.do", method = RequestMethod.GET)	
	public String downloadSampleDraftFile(@RequestParam("id") Integer fileId,HttpServletResponse response) {
		
		int FILEBUFFERSIZE=1000000;//megabyte
		
		edu.yu.einstein.wasp.model.File file=fileService.findById(fileId);
		file=null;
		if (file==null) {
	    	String html="<html>\n"+
	    	"<head>\n"+	    	
	    	"</script>\n"+
	    	"<body>\n"+
	    	"<script>alert('Error: file id "+fileId+" not foud');</script>\n"+
	    	"</body>\n";
	    	response.setContentType( "text/html; charset=UTF-8" );
	    	try {
	    		response.getWriter().print(html);
	    	} catch (Throwable e) {
	    		throw new IllegalStateException("Cant output error message ",e);
	    	}
		    return null;
		}
		try {
			ServletOutputStream out = response.getOutputStream();
			
			File diskFile=new File(file.getFilelocation());
			InputStream in = new FileInputStream(diskFile);
			
			String mimeType = "application/octet-stream";
			byte[] bytes = new byte[FILEBUFFERSIZE];
			int bytesRead;

			response.setContentType(mimeType);
			
			response.setContentLength( (int)diskFile.length() );
			
			String fileName=diskFile.getName();
				
			if (fileName!=null && fileName.indexOf('.')>-1) {
				int idx = fileName.lastIndexOf('.');
				fileName=fileName.substring(0,idx);		
			}
			
	        response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\"" );

			while ((bytesRead = in.read(bytes)) != -1) {
			    out.write(bytes, 0, bytesRead);
			}

			// do the following in a finally block:
			in.close();
			out.close();
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant download file id "+fileId,e);
		}
	}
	
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);
		m.addAttribute("typeSamples",typeSampleService.findAll());
		Map<String, String> statuses=new TreeMap<String, String>();
		for(SampleDraft.Status status:SampleDraft.Status.values()) {
			statuses.put(status.name(), status.name());
		}
		m.addAttribute("statuses",statuses);
		
	}

}
