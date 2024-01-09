package tech.api.autodealership.mock;

import tech.api.autodealership.entity.Car;
import tech.api.autodealership.entity.Garage;
import tech.api.autodealership.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class GarageMock {

    public static Garage any() {
        var car = new Car("black");

        List<Vehicle> vehicle = new ArrayList<>(List.of(car));

        return new Garage(null, vehicle).forCreation();
    }
}
