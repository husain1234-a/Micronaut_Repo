package com.yash.usermanagementsystem;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
            title = "UserManagementSystem",
            version = "10.0"
    )
)
public class Application {


    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}