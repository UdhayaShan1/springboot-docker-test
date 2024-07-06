package com.docker3.controller;

import org.springframework.batch.core.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.docker3.service.BatchService;

@Controller
public class BatchController {
    @Autowired
    private BatchService batchService;

    @GetMapping("/run-batch-job")
    public ResponseEntity<Void> runBatchJob() {
        try {
            batchService.runBatchJob();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (JobExecutionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
