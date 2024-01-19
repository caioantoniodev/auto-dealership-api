package tech.api.autodealership.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.api.autodealership.entity.Garage;
import tech.api.autodealership.exception.NotFoundException;
import tech.api.autodealership.service.GarageService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/garage")
@RestController
@Slf4j
public class GarageController {

    private final GarageService garageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Garage garage) {
        log.info("save - executed");
        this.garageService.save(garage);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Garage> findAll() {
        log.info("findAll - executed");
        return this.garageService.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Garage> findAll(@PathVariable String id) throws NotFoundException {
        log.info("findById - executed");
        return this.garageService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@PathVariable String id) {
        log.info("deleteById - executed");
        return this.garageService.findById(id)
                .map(Garage -> {
                    this.garageService.delete(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Garage> notFoundExceptionHandler(NotFoundException e) {
        log.error("notFoundExceptionHandler - message: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
