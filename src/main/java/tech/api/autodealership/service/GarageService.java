package tech.api.autodealership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import tech.api.autodealership.entity.Garage;
import tech.api.autodealership.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

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
        var key = buildPartitionKey(garageId);

        return Optional.ofNullable(this.garageTable.getItem(key));
    }

    public void delete(String garageId) {
        var key = buildPartitionKey(garageId);

        this.garageTable.deleteItem(key);
    }

    private static Key buildPartitionKey(String garageId) {
        return Key.builder()
                .partitionValue(garageId)
                .build();
    }

    public void update(String id, Garage garage) throws NotFoundException {
        var garageEntity = this.findById(id)
                .orElseThrow(() -> new NotFoundException("Garage " + id + " not found"));

        this.garageTable.updateItem(garageEntity.update(garage));
    }
}
