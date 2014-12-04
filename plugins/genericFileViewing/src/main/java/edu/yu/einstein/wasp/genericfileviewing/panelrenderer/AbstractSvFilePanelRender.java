package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;

/**
 * 
 * @author aj
 * @author asmclellan
 *
 */
public class AbstractSvFilePanelRender {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractSvFilePanelRender.class);
	
	
	protected AbstractSvFilePanelRender(){} // makes abstract
	
	/**
	 * Returns grid content parsed from input stream.
	 * @param is
	 * @param header
	 * @param seperator
	 * @param lineLimit (set to -1 for unlimited)
	 * @return
	 */
	protected static GridContent getGridContent(InputStream is, boolean header, String seperator, int lineLimit){
		GridContent content = new GridContent();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String currentLine;
			String lastLine = "";
			String workingLine = "";
			int lineNumber = 1;
			boolean headerProceessed = false;
			while ((currentLine = br.readLine()) != null && (lineLimit == -1 || lineNumber <= lineLimit)) {
				logger.trace(currentLine);
				currentLine = currentLine.trim();
				lastLine = workingLine;
				workingLine = currentLine;
				if (workingLine.startsWith("#"))
					continue;
				if (workingLine.replaceAll(seperator, "").isEmpty())
					continue;
				boolean dontProcessLineAsDataRow = false;
				String[] dataFields = workingLine.split(seperator);
				if (!headerProceessed){
					String[] lastLineFields = lastLine.replaceFirst("#+", "").split(seperator);
					String[] headerFields = dataFields; // default
					if (lastLineFields.length == dataFields.length)
						headerFields = lastLineFields;
					else
						dontProcessLineAsDataRow = true;
					if (header){
						int dataIndex = 1;
						// process header line first
						logger.trace("processing header: " + currentLine);
						
						for (String fstr : headerFields) {
							GridColumn c = new GridColumn(fstr.trim(), "di"+dataIndex, 1);
							c.setWidth(150);
							content.addColumn(c);
							content.addDataFields(new GridDataField("di"+dataIndex, "string"));
							dataIndex++;
						}
					} else {
						for (int dataIndex = 1; dataIndex <= headerFields.length; dataIndex++) {
							content.addColumn(new GridColumn("", "di"+dataIndex, 1));
							content.addDataFields(new GridDataField("di"+dataIndex, "string"));
						}
					}
					headerProceessed = true;
				} 
				if (headerProceessed && !dontProcessLineAsDataRow) { 
					List<String> row = new ArrayList<String>();
					for (String fstr : dataFields){
						row.add(fstr.trim());
					}
					content.addDataRow(row);
					lineNumber++;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
