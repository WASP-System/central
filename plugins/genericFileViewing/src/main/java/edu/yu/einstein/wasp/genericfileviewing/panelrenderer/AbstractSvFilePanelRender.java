package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;

public class AbstractSvFilePanelRender {
	
	private static Logger logger = Logger.getLogger(AbstractSvFilePanelRender.class);
	
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
			String line;
			String headerLine = "";
			boolean isFirstNonHeaderLineNotProcessed = true;
			int lineNumber = 1;
			while ((line = br.readLine()) != null && !line.trim().isEmpty() && (lineLimit == -1 || lineNumber++ <= lineLimit)) {
				line = line.trim();
				//logger.debug("line=" + line);
				if (isFirstNonHeaderLineNotProcessed){
					//logger.debug("isFirstNonHeaderLineNotProcessed=true");
					// If the first line starts with #, it might be a header line There may be more than one so only consider the 
					// last one we find a possible header line and then only if more than one tab delimited item
					if (line.startsWith("#")) {
						header = true;
						headerLine = line.substring(1);
						continue;
					}
					if (header && headerLine.isEmpty()){
						headerLine = line;
						continue;
					}
					if (header){
						// process header line first
						String[] fields = headerLine.split(seperator);
						if (fields.length > 1){ // unlikely to be a true header if only 1 field
							Integer dataIndex = 1;
							for (String fstr : fields) {
								content.addColumn(new GridColumn(fstr.trim(), "di"+dataIndex, 1));
								content.addDataFields(new GridDataField("di"+dataIndex, "string"));
								dataIndex++;
							}
						}
					}
					Integer dataIndex = 1;
					List<String> row = new ArrayList<String>();
					for (String fstr : line.split(seperator)) {
						content.addColumn(new GridColumn("", "di"+dataIndex, 1));
						content.addDataFields(new GridDataField("di"+dataIndex, "string"));
						//logger.debug("row.add(" + fstr.trim() + ") for first line");
						row.add(fstr.trim());
						dataIndex++;
					}
					content.addDataRow(row);
					isFirstNonHeaderLineNotProcessed = false;
				} else { 
					//logger.debug("isFirstNonHeaderLineNotProcessed=false");
					List<String> row = new ArrayList<String>();
					for (String fstr : line.split(seperator)){
						row.add(fstr.trim());
						//logger.debug("row.add(" + fstr.trim() + ")");
					}
					content.addDataRow(row);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
