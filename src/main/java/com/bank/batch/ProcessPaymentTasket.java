package com.bank.batch;

import com.bank.entities.TransferPaymentEntity;
import com.bank.repository.TransferPaymentRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ProcessPaymentTasket implements Tasklet {

    @Autowired
    private TransferPaymentRespository transferPaymentRespository;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        String transactionId = chunkContext.getStepContext()
                .getStepExecution()
                .getJobParameters()
                .getString("transactionId");


        log.info("------> Se procesa el pago de la transaccion {} exitosamente.", transactionId);
        transferPaymentRespository.updateTransactionStatus(true, transactionId);

        return RepeatStatus.FINISHED;
    }
}
