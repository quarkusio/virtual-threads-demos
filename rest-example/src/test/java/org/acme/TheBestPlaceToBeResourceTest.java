package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import me.escoffier.loom.loomunit.LoomUnitExtension;
import me.escoffier.loom.loomunit.ShouldNotPin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@QuarkusTest
@ExtendWith(LoomUnitExtension.class)
class TheBestPlaceToBeResourceTest {


    @Test
    @ShouldNotPin
    void verify() {
        RestAssured.get("/")
                .then()
                .statusCode(200);
    }


}