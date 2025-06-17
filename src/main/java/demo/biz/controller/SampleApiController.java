package demo.biz.controller;

import core.common.BatchManager;
import core.common.CommonBatchService;
import demo.biz.mapper.SampleMapper;
import demo.biz.service.SampleApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SampleApiController {

    private final SampleApiService sampleService;
    private final BatchManager batchManager;
    private final CommonBatchService commonBatchService;
    private final SampleMapper sampleMapper;


    @PostMapping("/sample")
    public void getSampleDummy() throws Exception {

        commonBatchService.runJob("sampleJob");

    }

}
