package edu.yu.einstein.wasp.service.impl.illumina;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.service.illumina.WaspIlluminaService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class WaspIlluminaServiceImpl extends WaspServiceImpl implements WaspIlluminaService{




}
