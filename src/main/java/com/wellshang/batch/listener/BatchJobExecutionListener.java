package com.wellshang.batch.listener;

import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

public class BatchJobExecutionListener implements JobExecutionListener {
    private Logger LOG = LoggerFactory.getLogger("JobListener");
    
    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            StringBuilder protocol = new StringBuilder();
            protocol.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
            protocol.append("Result for " + jobExecution.getJobInstance().getJobName() + " \n");
            protocol.append("  Started     : "+ jobExecution.getStartTime()+"\n");
            protocol.append("  Finished    : "+ jobExecution.getEndTime()+"\n");
            protocol.append("  Exit-Code   : "+ jobExecution.getExitStatus().getExitCode()+"\n");
            protocol.append("  Exit-Descr. : "+ jobExecution.getExitStatus().getExitDescription()+"\n");
            protocol.append("  Status      : "+ jobExecution.getStatus()+"\n");
            protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");

            protocol.append("Job-Parameter: \n");       
            JobParameters jp = jobExecution.getJobParameters();
            for (Iterator<Entry<String,JobParameter>> iter = jp.getParameters().entrySet().iterator(); iter.hasNext();) {
                Entry<String,JobParameter> entry = iter.next();
                protocol.append("  "+entry.getKey()+"="+entry.getValue()+"\n");
            }
            protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");      
            
            for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
                protocol.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
                protocol.append("Step " + stepExecution.getStepName() + " \n");
                protocol.append("WriteCount: " + stepExecution.getWriteCount() + "\n");
                protocol.append("Commits: " + stepExecution.getCommitCount() + "\n");
                protocol.append("SkipCount: " + stepExecution.getSkipCount() + "\n");
                protocol.append("Rollbacks: " + stepExecution.getRollbackCount() + "\n");
                protocol.append("Filter: " + stepExecution.getFilterCount() + "\n");                    
                protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
            }
            LOG.info(protocol.toString());
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
        LOG.info(jobExecution.getJobInstance().getJobName() + " STARTING...");
    }
}
