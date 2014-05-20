/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.Properties;

import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.WaspJobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * 
 */
public class TrimGalorePlugin extends WaspPlugin implements ClientMessageI, FileDataTabViewing {

    /**
     * 
     */
    private static final long serialVersionUID = 8094367515193248876L;

    @Autowired
    BabrahamService babrahamService;

    protected WaspJobExplorer batchJobExplorer;

    @Autowired
    void setJobExplorer(JobExplorer jobExplorer) {
	this.batchJobExplorer = (WaspJobExplorer) jobExplorer;
    }

    @Autowired
    @Qualifier("trim_galore")
    private Software trim_galore;

    public TrimGalorePlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
	super(iName, waspSiteProperties, channel);
    }

    /**
     * Wasp plugins implement InitializingBean to give authors an opportunity to
     * initialize at runtime.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
	// TODO Auto-generated method stub

    }

    /**
     * Wasp plugins implement DisposableBean to give authors the ability to tear
     * down on shutdown.
     */
    @Override
    public void destroy() throws Exception {
	// TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hyperlink getDescriptionPageHyperlink() {
	return new Hyperlink("trim_galore.hyperlink.label", "/babraham/trim_galore/description.do");
    }

    /**
     * Trimming is happening prior to returning the file to the user,
     * so the status of analysis of a fastq file is always complete
     * when the user wants to visualize any information.
     * {@inheritDoc}
     */
    @Override
    public Status getStatus(FileGroup fileGroup) {
	return Status.COMPLETED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException {
	return null;
    }

}
