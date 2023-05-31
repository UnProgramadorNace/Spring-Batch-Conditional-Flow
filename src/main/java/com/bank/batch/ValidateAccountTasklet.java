package com.bank.batch;

import com.bank.entities.TransferPaymentEntity;
import com.bank.repository.TransferPaymentRespository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidateAccountTasklet implements Tasklet {

    @Autowired
    private TransferPaymentRespository transferPaymentRespository;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        Boolean filterIsAproved = true;

        String transactionId = chunkContext.getStepContext()
                .getStepExecution()
                .getJobParameters()
                .getString("transactionId");

        TransferPaymentEntity transferPaymentEntity  = transferPaymentRespository.findById(transactionId).orElseThrow();

        if (!transferPaymentEntity.getIsEnabled()) {
            // Error porque la cuenta esta inactiva
            chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("message", "Error, la cuenta se eencuentra inactiva.");

            filterIsAproved = false;
        }

        if (transferPaymentEntity.getAmountPaid() > transferPaymentEntity.getAvaiableBalance()) {
            // Error porque el saldo es insuficiente
            chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("message", "Error, el saldo del cliente es insuficiente.");

            filterIsAproved = false;
        }

        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("transactionObject", transferPaymentEntity);

        ExitStatus exitStatus = null;
        if (filterIsAproved) {

            exitStatus = new ExitStatus("VALID");
            stepContribution.setExitStatus(exitStatus);
        } else {

            exitStatus = new ExitStatus("INVALID");
            stepContribution.setExitStatus(exitStatus);
        }

        return RepeatStatus.FINISHED;
    }

}
