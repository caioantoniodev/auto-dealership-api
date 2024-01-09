package tech.api.autodealership.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import tech.api.autodealership.entity.Garage;
import tech.api.autodealership.mock.GarageMock;
import tech.api.autodealership.service.GarageService;

public class GarageServiceTest extends AbstractDynamoDbContainerTest {

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
}
