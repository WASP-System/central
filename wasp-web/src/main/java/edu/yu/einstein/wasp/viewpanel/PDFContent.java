package edu.yu.einstein.wasp.viewpanel;

public class PDFContent extends Content {

	private String pdfURL;

	public PDFContent() {
		pdfURL = "";
	}

	/**
	 * @return the pdfURL
	 */
	public String getPdfURL() {
		return pdfURL;
	}

	/**
	 * @param pdfURL the pdfURL to set
	 */
	public void setPdfURL(String pdfURL) {
		this.pdfURL = pdfURL;
	}
}
