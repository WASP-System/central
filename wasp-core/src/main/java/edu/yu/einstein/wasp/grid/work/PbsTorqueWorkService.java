/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;

/**
 * {@link GridWorkService} implementation for PBS/torque.  Based on version 2.4.6.
 * 
 * @author calder
 * 
 */
public class PbsTorqueWorkService extends SgeWorkService {
	
	@Value("${wasp.temporary.dir}")
	private String localTempDir;

	/**
	 * @param transportConnection
	 */
	public PbsTorqueWorkService(GridTransportConnection transportConnection) {
		super(transportConnection);
		// TODO Auto-generated constructor stub
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected Document getQstat(GridResult g, String jobname) throws GridException, SAXException, IOException {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setWorkingDirectory(g.getWorkingDirectory());
		WorkUnit w = new WorkUnit(c);
		w.setCommand("qstat -x | perl -nle 'print \"<?xml version=\\\"1.0\\\"?>\"; print m|(<Job><Job_Id>[^<]*?</Job_Id><Job_Name>" + 
				jobname + "</Job_Name>.*?</Job>)| ? $1 : \"<none />\"'");
		GridResult result;
		try {
			result = transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridException(e.getLocalizedMessage(), e);
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			dbFactory.setValidating(false);
			docBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to generate xml parser");
		}
		InputStream is = result.getStdOutStream();
		Document doc = docBuilder.parse(new InputSource(is));
		is.close();
		return doc;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isUnknown(Document stdout) {
		stdout.getDocumentElement().normalize();
		NodeList nl = stdout.getElementsByTagName("Job");
		return nl.getLength() == 0 ? true : false; 
		
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isInError(Document stdout) {
		// PBS/Torque should not report jobs in error state (i.e. no Eqw).
		return false;
	}

	protected class PbsTorqueSubmissionScript extends SgeSubmissionScript {
		
		protected String schedulerFlag= "#PBS";
		
		@Deprecated
		private PbsTorqueSubmissionScript() {
			super();
		}

		@SuppressWarnings("deprecation")
		private PbsTorqueSubmissionScript(WorkUnit w) throws GridException, MisconfiguredWorkUnitException {
			super(w);
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getFlag() {
			return schedulerFlag;
		}
		
		
		
	}
	
}
