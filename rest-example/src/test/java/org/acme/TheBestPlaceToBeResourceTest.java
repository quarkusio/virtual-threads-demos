package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit5.virtual.ShouldNotPin;
import io.quarkus.test.junit5.virtual.internal.VirtualThreadExtension;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@QuarkusTest
@ExtendWith(VirtualThreadExtension.class)
class TheBestPlaceToBeResourceTest {


    @Test
    @ShouldNotPin
    void verify() {
        RestAssured.get("/")
                .then()
                .statusCode(200);
    }


}