INSERT INTO BATCH_JOB_INSTANCE (JOB_INSTANCE_ID, VERSION, JOB_NAME, JOB_KEY)
VALUES
	(1,0,'wasp.sample.jobflow.v1','00e62e0355de69ca00a4e923ddafb0fe');
INSERT INTO BATCH_JOB_INSTANCE (JOB_INSTANCE_ID, VERSION, JOB_NAME, JOB_KEY)
VALUES
	(2,0,'wasp.sample.jobflow.v1','b99ba26336f0a866c1aefa4e896f2537');

	
INSERT INTO BATCH_JOB_EXECUTION (JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(1,2,1,'2012-09-27 10:26:35','2012-09-27 10:26:35','2012-09-27 10:26:51','COMPLETED','COMPLETED','','2012-09-27 10:26:51');
INSERT INTO BATCH_JOB_EXECUTION (JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(2,1,2,'2012-10-11 16:01:23','2012-10-11 16:01:23',NULL,'STARTED','UNKNOWN','','2012-10-11 16:01:23');


INSERT INTO BATCH_JOB_EXECUTION_CONTEXT (JOB_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(1,'{\"map\":\"\"}',NULL);
INSERT INTO BATCH_JOB_EXECUTION_CONTEXT (JOB_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(2,'{\"map\":\"\"}',NULL);

	
INSERT INTO BATCH_JOB_EXECUTION_PARAMS (JOB_EXECUTION_ID, TYPE_CD, KEY_NAME, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING)
VALUES
	(1,'STRING','jobId','1','1969-12-31 19:00:00',0,0,1);
INSERT INTO BATCH_JOB_EXECUTION_PARAMS (JOB_EXECUTION_ID, TYPE_CD, KEY_NAME, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING)
VALUES
	(1,'STRING','sampleId','2','1969-12-31 19:00:00',0,0,1);
INSERT INTO BATCH_JOB_EXECUTION_PARAMS (JOB_EXECUTION_ID, TYPE_CD, KEY_NAME, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING)
VALUES
	(2,'STRING','jobId','1','1969-12-31 19:00:00',0,0,1);
INSERT INTO BATCH_JOB_EXECUTION_PARAMS (JOB_EXECUTION_ID, TYPE_CD, KEY_NAME, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING)
VALUES
	(2,'STRING','sampleId','1','1969-12-31 19:00:00',0,0,1);
	
INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID)
VALUES
	(2);


INSERT INTO BATCH_JOB_SEQ (ID)
VALUES
	(2);

	
INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(1,4,'wasp.sample.step.listenForSampleReceived',1,'2012-10-11 16:00:58','2012-10-11 16:01:03','COMPLETED',2,0,0,0,0,0,0,0,'COMPLETED','','2012-10-11 16:01:03');
INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(2,7,'wasp.sample.step.listenForExitCondition',1,'2012-10-11 16:00:58','2012-10-11 16:01:18','COMPLETED',5,0,0,0,0,0,0,0,'COMPLETED','','2012-10-11 16:01:18');
INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(3,5,'wasp.sample.step.listenForJobApproved',1,'2012-10-11 16:00:58','2012-10-11 16:01:08','COMPLETED',3,0,0,0,0,0,0,0,'COMPLETED','','2012-10-11 16:01:08');
INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(4,4,'wasp.sample.step.sampleQC',1,'2012-10-11 16:01:08','2012-10-11 16:01:13','COMPLETED',2,0,0,0,0,0,0,0,'COMPLETED','','2012-10-11 16:01:13');
INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(5,3,'wasp.sample.step.notifySampleReady',1,'2012-10-11 16:01:13','2012-10-11 16:01:13','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-10-11 16:01:13');
INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(6,2,'wasp.sample.step.listenForExitCondition',2,'2012-10-11 16:01:23',NULL,'STARTED',1,0,0,0,0,0,0,0,'EXECUTING','','2012-10-11 16:01:28');
INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(7,4,'wasp.sample.step.listenForSampleReceived',2,'2012-10-11 16:01:23','2012-10-11 16:01:28','COMPLETED',2,0,0,0,0,0,0,0,'COMPLETED','','2012-10-11 16:01:28');
INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
VALUES
	(8,2,'wasp.sample.step.listenForJobApproved',2,'2012-10-11 16:01:23',NULL,'STARTED',1,0,0,0,0,0,0,0,'EXECUTING','','2012-10-11 16:01:28');

	
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(1,'{\"map\":\"\"}',NULL);
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(2,'{\"map\":\"\"}',NULL);
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(3,'{\"map\":\"\"}',NULL);
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(4,'{\"map\":\"\"}',NULL);
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(5,'{\"map\":\"\"}',NULL);
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(6,'{\"map\":\"\"}',NULL);
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(7,'{\"map\":\"\"}',NULL);
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT, SERIALIZED_CONTEXT)
VALUES
	(8,'{\"map\":\"\"}',NULL);

INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID)
VALUES
	(8);



