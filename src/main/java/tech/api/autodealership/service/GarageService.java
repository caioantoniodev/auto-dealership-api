package tech.api.autodealership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import tech.api.autodealership.entity.Garage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Garage> findById(String garageId) {
        var key = Key.builder()
                .partitionValue(garageId)
                .build();

        return Optional.ofNullable(this.garageTable.getItem(key));
    }

    public void delete(String garageId) {
        this.garageTable.deleteItem(
                Key.builder()
                        .partitionValue(garageId)
                        .build()
        );
    }
}
