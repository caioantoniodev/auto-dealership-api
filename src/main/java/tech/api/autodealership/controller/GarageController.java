package tech.api.autodealership.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.api.autodealership.entity.Garage;
import tech.api.autodealership.service.DatabaseService;

@RequiredArgsConstructor
@RequestMapping("/v1/garage")
@RestController
@Slf4j
public class GarageController {

    private final DatabaseService databaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Garage garage) {
        log.info("save - executed");
        this.databaseService.save(garage);
    }
}
