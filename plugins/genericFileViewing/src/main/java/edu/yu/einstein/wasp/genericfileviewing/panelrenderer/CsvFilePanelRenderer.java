/**
 * 
 */
package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * @author aj
 *
 */
public class CsvFilePanelRenderer {

	public static PanelTab getPanelForFileGroup(String fileName, InputStream is, boolean header) {
		String csvSplitBy = ",";
		PanelTab panelTab = new PanelTab();
		panelTab.setNumberOfColumns(1);
		panelTab.setMaxOnLoad(true);
		panelTab.setName("CSV File Viewer");
		panelTab.setDescription("Generic CSV file viewing");
		GridPanel panel = new GridPanel();
		panel.setTitle(fileName);
		panelTab.addPanel(panel);
		GridContent content = new GridContent();
		if (is == null) {
			panel.setContent(content);
			return panelTab;
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line = br.readLine().trim();
			if (line!=null) {
				// If the first line starts with #, it is a header line
				if (line.startsWith("#")) {
					header = true;
					line = line.substring(1);
				}
				
				String[] fields = line.split(csvSplitBy);
				Integer dataIndex = 1;
				if (header) {	// the first line is the header
					for (String fstr : fields) {
						content.addColumn(new GridColumn(fstr.trim(), "di"+dataIndex, 1));
						content.addDataFields(new GridDataField("di"+dataIndex, "string"));
						dataIndex++;
					}
				} else {	// the first line is the data
					List<String> row = new ArrayList<String>();
					for (String fstr : fields) {
						content.addColumn(new GridColumn("", "di"+dataIndex, 1));
						content.addDataFields(new GridDataField("di"+dataIndex, "string"));
						row.add(fstr.trim());
						dataIndex++;
					}
					content.addDataRow(row);
				}
			}
			while ((line = br.readLine().trim()) != null && !line.isEmpty()) {
				List<String> row = new ArrayList<String>();
				String[] fields = line.split(csvSplitBy);
				for (String fstr : fields) {
					row.add(fstr.trim());
				}
				
				content.addDataRow(row);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		panel.setContent(content);
		return panelTab;
	}

}
