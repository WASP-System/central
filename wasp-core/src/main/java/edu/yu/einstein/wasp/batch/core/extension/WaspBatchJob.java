/*
* Copyright 2006-2013 the original author or authors.
* @author Lucas Ward
* @author Dave Syer
* @author Ben Hale
* @author Robert Kasanicky
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package edu.yu.einstein.wasp.batch.core.extension;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.job.SimpleStepHandler;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionException;
import org.springframework.batch.core.job.flow.FlowHolder;
import org.springframework.batch.core.job.flow.FlowJob;
import org.springframework.batch.core.job.flow.JobFlowExecutor;
import org.springframework.batch.core.job.flow.State;
import org.springframework.batch.core.listener.CompositeJobExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.StepHolder;
import org.springframework.batch.core.step.StepLocator;

/**
 * Code taken from {@link FlowJob} and modified. 
 * @author asmclellan
 *
 */
public class WaspBatchJob extends AbstractWaspBatchJob {

        protected static final Logger logger = LoggerFactory.getLogger(WaspBatchJob.class);

        private Flow flow;

        private Map<String, Step> stepMap = new ConcurrentHashMap<String, Step>();

        private volatile boolean initialized = false;

        /**
         * Create a {@link FlowJob} with null name and no flow (invalid state).
         */
        public WaspBatchJob() {
                super();
        }

        /**
         * Create a {@link FlowJob} with provided name and no flow (invalid state).
         */
        public WaspBatchJob(String name) {
                super(name);
        }
        
        public WaspBatchJob(FlowJob job) {
        	setJobParametersIncrementer(job.getJobParametersIncrementer());
            setJobParametersValidator(job.getJobParametersValidator());
            setName(job.getName());
            setRestartable(job.isRestartable());
            try {
             	// set listener. Unfortunately no getters for this so use reflection.
             	Field listenerField = AbstractJob.class.getDeclaredField("listener");
             	listenerField.setAccessible(true); // because field is private
             	listener = (CompositeJobExecutionListener) listenerField.get((AbstractJob) job);
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to set value for 'listener' from value provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
            try {
             	// set jobRepository. Unfortunately no getters for these so use reflection.
             	Field jobRepositoryField = AbstractJob.class.getDeclaredField("jobRepository");
             	jobRepositoryField.setAccessible(true); // because field is private
 				jobRepository = (JobRepository) jobRepositoryField.get((AbstractJob) job);
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to obtain value for 'jobRepository' from provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
            try {
             	// set flow. Unfortunately no getters for this so use reflection.
             	Field flowField = job.getClass().getDeclaredField("flow");
             	flowField.setAccessible(true); // because field is private
 				setFlow((Flow) flowField.get(job));
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to obtain value for 'flow' from provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
            try {
             	// set stepMap. Unfortunately no getters for this so use reflection.
             	Field stepMapField = job.getClass().getDeclaredField("stepMap");
             	stepMapField.setAccessible(true); // because field is private
             	stepMapField.set(this, stepMapField.get(job));
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to obtain value for 'stepMap' from provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
            try {
             	// set initialized. Unfortunately no getters for this so use reflection.
             	Field initializedField = job.getClass().getDeclaredField("initialized");
             	initializedField.setAccessible(true); // because field is private
             	initializedField.set(this, initializedField.get(job));
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to obtain value for 'initialized' from provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
        }

        /**
         * Public setter for the flow.
         *
         * @param flow the flow to set
         */
        public void setFlow(Flow flow) {
                this.flow = flow;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Step getStep(String stepName) {
                if (!initialized) {
                        init();
                }
                return stepMap.get(stepName);
        }

        /**
         * Initialize the step names
         */
        private void init() {
                findSteps(flow, stepMap);
        }

        /**
         * @param flow
         * @param map
         */
        private void findSteps(Flow flow, Map<String, Step> map) {

                for (State state : flow.getStates()) {
                        if (state instanceof StepHolder) {
                                Step step = ((StepHolder) state).getStep();
                                String name = step.getName();
                                stepMap.put(name, step);
                        }
                        else if (state instanceof FlowHolder) {
                                for (Flow subflow : ((FlowHolder) state).getFlows()) {
                                        findSteps(subflow, map);
                                }
                        }
                        else if (state instanceof StepLocator) {
                                StepLocator locator = (StepLocator) state;
                                for (String name : locator.getStepNames()) {
                                        map.put(name, locator.getStep(name));
                                }
                        }
                }

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<String> getStepNames() {
                if (!initialized) {
                        init();
                }
                return stepMap.keySet();
        }

        /**
         * @see AbstractJob#doExecute(JobExecution)
         */
        @Override
        protected void doExecute(final JobExecution execution) throws JobExecutionException {
                try {
                        JobFlowExecutor executor = new JobFlowExecutor(getJobRepository(),
                                        new SimpleStepHandler(getJobRepository()), execution);
                        executor.updateJobExecutionStatus(flow.start(executor).getStatus());
                }
                catch (FlowExecutionException e) {
                        if (e.getCause() instanceof JobExecutionException) {
                                throw (JobExecutionException) e.getCause();
                        }
                        throw new JobExecutionException("Flow execution ended unexpectedly", e);
                }
        }
        
       
       

}
