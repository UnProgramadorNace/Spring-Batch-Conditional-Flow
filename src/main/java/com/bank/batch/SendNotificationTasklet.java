package com.bank.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class SendNotificationTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        String transactionId = chunkContext.getStepContext()
                .getStepExecution()
                .getJobParameters()
                .getString("transactionId");

        log.info("++++> Se ha enviado una notificacion al cliente para la transaccion {}", transactionId);

        return RepeatStatus.FINISHED;
    }
}
