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
public class Car extends Vehicle {

    private final String color;

    @JsonCreator
    public Car(@JsonProperty("color") String color) {
        super(UUID.randomUUID(), VehicleType.CAR);
        this.color = color;
    }
}