/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptagham.batch.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.yu.einstein.wasp.helptagham.service.impl.AbstractHelptaghamServiceImpl;
import edu.yu.einstein.wasp.helptagham.batch.service.HelptaghamBatchService;

@Service
@Transactional("entityManager")
public class HelptaghamBatchServiceImpl extends AbstractHelptaghamServiceImpl implements HelptaghamBatchService {
	

}
