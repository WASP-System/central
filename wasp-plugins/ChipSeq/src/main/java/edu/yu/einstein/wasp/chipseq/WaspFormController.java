package edu.yu.einstein.wasp.chipseq;

import edu.yu.einstein.wasp.dao.*;
import edu.yu.einstein.wasp.model.*;

import edu.yu.einstein.wasp.controller.JobSubmissionController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;


@Controller
@Transactional
@RequestMapping("/waspForm")
public class WaspFormController extends JobSubmissionController { 

}
