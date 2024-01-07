package tech.api.autodealership.util;

import com.fasterxml.jackson.core.type.TypeReference;
import tech.api.autodealership.entity.Vehicle;

import java.util.List;

public class VehicleConverter extends JacksonAttributeConverter<List<Vehicle>> {

    public VehicleConverter() {
        super(new TypeReference<>() {});
    }

}