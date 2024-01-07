package tech.api.autodealership.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.services.dynamodb.model.*;
import tech.api.autodealership.config.DynamoDbEntity;
import tech.api.autodealership.util.VehicleConverter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
@Component
@Data
public class Garage implements DynamoDbEntity {

    private UUID id;
    private List<Vehicle> vehicles;

    public Garage forCreation() {
        return new Garage(UUID.randomUUID(), this.vehicles);
    }

    public Garage update(Garage updatedGarage) {
        return new Garage(this.id, updatedGarage.getVehicles());
    }

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }

    @DynamoDbConvertedBy(VehicleConverter.class)
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public static DynamoDbEntity config() {
        return new Garage();
    }

    @Override
    @JsonIgnore
    public String getTableName() {
        return "garage";
    }

    @Override
    @JsonIgnore
    public CreateTableRequest getTableRequest() {
        return CreateTableRequest.builder()
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("id")
                                .attributeType(ScalarAttributeType.S)
                                .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName("id")
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
                .tableName(this.getTableName())
                .build();
    }
}
