/**
 * 
 */
package edu.yu.einstein.wasp.viewpanel;

/**
 * @author aj
 *
 */
public class PDFPanel extends Panel {

	private String type = "PDFPanel";

	// Initial scaling of the PDF. 1 = 100%
	private double pageScale = 0.75;
	
	/**
	 * 
	 */
	public PDFPanel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param content
	 */
	public PDFPanel(String title, Content content) {
		super(title, content);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param description
	 * @param width
	 * @param height
	 * @param order
	 * @param content
	 * @param resizable
	 * @param maximizable
	 * @param closeable
	 */
	public PDFPanel(String title, String description, Integer width,
			Integer height, Integer order, Content content, boolean resizable,
			boolean maximizable, boolean closeable) {
		super(title, description, width, height, order, content, resizable,
				maximizable, closeable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param description
	 * @param width
	 * @param height
	 * @param content
	 */
	public PDFPanel(String title, String description, Integer width,
			Integer height, Content content) {
		super(title, description, width, height, content);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the pageScale
	 */
	public double getPageScale() {
		return pageScale;
	}

	/**
	 * @param pageScale the pageScale to set
	 */
	public void setPageScale(double pageScale) {
		// To enforce the page scale between 10% to 100%
		if (pageScale>1) {
			pageScale = 1;
		} else if (pageScale<0.1) {
			pageScale = 0.1;
		}
		this.pageScale = pageScale;
	}


}
