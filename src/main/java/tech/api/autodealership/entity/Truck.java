package tech.api.autodealership.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import tech.api.autodealership.entity.enums.VehicleType;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@DynamoDbBean
@Data
public class Truck extends Vehicle {

    private final Integer containerCapacity;

    @JsonCreator
    public Truck(@JsonProperty("container_capacity") Integer containerCapacity) {
        super(UUID.randomUUID(), VehicleType.TRUCK);
        this.containerCapacity = containerCapacity;
    }
}