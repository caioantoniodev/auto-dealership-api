package tech.api.autodealership.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import tech.api.autodealership.entity.Garage;
import tech.api.autodealership.mock.GarageMock;
import tech.api.autodealership.service.GarageService;

public class GarageServiceTest extends AbstractDynamoDbContainer {

    @Autowired
    private GarageService garageService;

    @Autowired
    private DynamoDbTable<Garage> garageDynamoDbTable;

    @Test
    void findAll() {
        this.garageDynamoDbTable.putItem(GarageMock.any());
        var result = this.garageService.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void findById() {
        var garage = GarageMock.any();
        this.garageDynamoDbTable.putItem(garage);
        var result = this.garageService.findById(String.valueOf(garage.getId()));
        Assertions.assertTrue(result.isPresent());
        Assertions.assertNotNull(result.get());
    }

    @Test
    void shouldBeCreate() {
        var garage = GarageMock.any();
        this.garageService.save(garage);

        var result = this.garageService.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }
}
