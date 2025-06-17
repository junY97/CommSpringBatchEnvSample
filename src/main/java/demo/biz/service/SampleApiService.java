package demo.biz.service;

import core.common.CommonBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SampleApiService {

    private final CommonBatchService commonBatchService;

    public String sampleService() throws Exception {
        commonBatchService.runJob("sampleJob");
        return "success";
    }
}
