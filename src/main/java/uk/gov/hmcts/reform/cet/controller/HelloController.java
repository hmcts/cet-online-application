package uk.gov.hmcts.reform.cet.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/civil-enforcement")
public class HelloController {

    @RequestMapping("")
    public String greet() {
        return "Greetings from Civil Enforcement!";
    }

}