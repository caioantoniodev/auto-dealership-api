package tech.api.autodealership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import tech.api.autodealership.entity.Garage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GarageService {

    private final DynamoDbTable<Garage> garageTable;

    public void save(Garage garage) {
        var creationGarage = garage.forCreation();
        garageTable.putItem(creationGarage);
    }

    public List<Garage> findAll() {
        return garageTable.scan()
                .items()
                .stream()
                .toList();

    }
}
