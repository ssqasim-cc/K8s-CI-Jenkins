package io.digital.supercharger.controller;

import io.digital.supercharger.dto.DemoData;
import io.digital.supercharger.interceptor.security.LoginRequired;
import io.digital.supercharger.model.CustomerProfileDto;
import io.digital.supercharger.service.DemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/demo")
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class DemoController {
    private final DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping()
    @ApiOperation(value = "View a list of product items")
    public List<DemoData> getAll() {
        return demoService.findAll();
    }

    /**
     * An example authenticated endpoint. Which is annotated by the LoginRequired marker
     * @param demoId - the object id
     * @param customerProfile - the customer profile injected by the auth interceptor
     * @return the object loaded from the db
     */
    @LoginRequired
    @GetMapping("/{demoId}")
    @ApiOperation(value = "View item by id (Requires Authentication for test purposes)")
    public DemoData getDemoById(
        @ApiParam(value = "demoId", example = "12345", required = true)
        @PathVariable Long demoId,
        @ApiIgnore
        @ApiParam(hidden = true)
        @RequestAttribute(required = false) CustomerProfileDto customerProfile) {
        return demoService.findById(demoId);
    }

    @PostMapping()
    @ApiOperation(value = "Add new Demo",
        notes = "Add new Demo to database.",
        response = DemoData.class)
    public ResponseEntity<DemoData> saveDemo(@RequestBody @Valid DemoData demoData) {
        return new ResponseEntity<>(demoService.save(demoData), HttpStatus.CREATED);
    }
}
