package demo.biz.service;

import core.util.BatchExecutionService;
import demo.biz.batch.SampleJobStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class SampleService {

    private final BatchExecutionService batchExecutionService;

    public String SampleBatch() throws Exception {
        batchExecutionService.runJob("sampleJob");
        return "success";
    }
}
