package tech.api.autodealership.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;
import tech.api.autodealership.entity.Garage;

import java.net.URI;
import java.util.List;

@Configuration
@Slf4j
public class DynamoDbConfig implements CommandLineRunner {

    private final URI dynamodbUri;
    private final List<DynamoDbEntity> entities;

    public DynamoDbConfig(@Value("${aws.dynamodb.uri}") final String dynamodbStringUri,
                          List<DynamoDbEntity> entities) {

        if (dynamodbStringUri == null || dynamodbStringUri.isBlank())
            throw new IllegalArgumentException(
                    "DynamoDb Uri is required, the java property aws.dynamodb.uri must be set");

        this.dynamodbUri = URI.create(dynamodbStringUri);
        this.entities = entities;
    }

    public void createTables(DynamoDbClient dynamoDbClient) {
        this.entities.forEach(t -> {
            var tableRequest = DescribeTableRequest.builder()
                    .tableName(t.getTableName())
                    .build();

            try {
                var response = dynamoDbClient.describeTable(tableRequest);
                log.info("Table [{}] existence verified", response.table().tableName());

            } catch (ResourceNotFoundException e) {
                log.warn("Table [{}] not exists, creating...", t.getTableName());

                dynamoDbClient.createTable(t.getTableRequest());

                DynamoDbWaiter dbWaiter = dynamoDbClient.waiter();

                var waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);

                waiterResponse.matched()
                        .exception()
                        .ifPresent(exception -> {
                            log.error("Table [{}] not created, details: {}",
                                    t.getTableName(), exception);
                            System.exit(-1);
                        });

                waiterResponse.matched()
                        .response()
                        .ifPresentOrElse((response ->
                                log.info("Table [{}] created successfully", response.table().tableName())
                        ), () -> {
                            log.error("Table [{}] not created, verify your connection with the database",
                                    t.getTableName());
                            System.exit(-1);
                        });
            }
        });
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.SA_EAST_1)
                .endpointOverride(this.dynamodbUri)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<Garage> carDynamoDbTable(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return dynamoDbEnhancedClient.table(Garage.config().getTableName(), TableSchema.fromBean(Garage.class));
    }

    @Override
    public void run(String... args) throws Exception {
        this.createTables(dynamoDbClient());
    }
}
