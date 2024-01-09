package tech.api.autodealership.integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import tech.api.autodealership.Application;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractDynamoDbContainer implements Extension {

    static GenericContainer<?> dynamoDb = new GenericContainer<>(DockerImageName.parse("amazon/dynamodb-local:latest"))
            .withExposedPorts(8000)
            .withCommand("-jar DynamoDBLocal.jar -inMemory -port 8000");

    @DynamicPropertySource
    static void dynamoDbProperties(DynamicPropertyRegistry registry) {

        registry.add("aws.dynamodb.uri",
                () -> String.format("http://%s:%d", dynamoDb.getHost(), dynamoDb.getFirstMappedPort()));

        dynamoDb.start();
    }
}