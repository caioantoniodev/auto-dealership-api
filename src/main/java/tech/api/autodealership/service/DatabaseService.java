package tech.api.autodealership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import tech.api.autodealership.entity.Garage;

@RequiredArgsConstructor
@Service
public class DatabaseService {

    private final DynamoDbTable<Garage> garageTable;

    public void save(Garage garage) {
        var creationGarage = garage.forCreation();
        garageTable.putItem(creationGarage);
    }
}
