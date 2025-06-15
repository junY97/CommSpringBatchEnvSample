package demo.biz.controller;

import demo.biz.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @PostMapping("/sample")
    public String sampleBatch() throws Exception {

        return sampleService.SampleBatch();
    }

}
