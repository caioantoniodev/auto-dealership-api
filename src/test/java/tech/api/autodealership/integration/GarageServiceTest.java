package tech.api.autodealership.integration;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import tech.api.autodealership.entity.Car;
import tech.api.autodealership.entity.Garage;
import tech.api.autodealership.entity.Vehicle;
import tech.api.autodealership.mock.GarageMock;
import tech.api.autodealership.service.GarageService;

import java.util.ArrayList;
import java.util.List;

public class GarageServiceTest extends AbstractDynamoDbContainer {

    @Autowired
    private GarageService garageService;

    @Autowired
    private DynamoDbTable<Garage> garageDynamoDbTable;

    @Test
    void shouldBeReturnListOfGarages() {
        this.garageDynamoDbTable.putItem(GarageMock.any());
        var result = this.garageService.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void shouldBeReturnGarageWhenFoundById() {
        var garage = GarageMock.any();
        this.garageDynamoDbTable.putItem(garage);
        var result = this.garageService.findById(String.valueOf(garage.getId()));
        Assertions.assertTrue(result.isPresent());
        Assertions.assertNotNull(result.get());
    }

    @Test
    void shouldBeCreateGarage() {
        var garage = GarageMock.any();
        this.garageService.save(garage);

        var result = this.garageService.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteGarage() {
        var garage = GarageMock.any();
        this.garageDynamoDbTable.putItem(garage);

        garageService.delete(String.valueOf(garage.getId()));

        var result = this.garageService.findById(String.valueOf(garage.getId()));
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldBeUpdateGarage() {
        var garage = GarageMock.any();
        this.garageDynamoDbTable.putItem(garage);

        var car = new Car("purple");
        List<Vehicle> vehicle = new ArrayList<>(List.of(car));
        var garageToUpdate = new Garage(null, vehicle);

        garageService.update(String.valueOf(garage.getId()), garageToUpdate);

        var result = this.garageService.findById(String.valueOf(garage.getId()));

        Assertions.assertTrue(result.isPresent());

        var vehicleResult = result.get().getVehicles().stream().findFirst().get();

        var carResult = (Car) vehicleResult;

        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(car.getColor(), carResult.getColor());
    }
}
