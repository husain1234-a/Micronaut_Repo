package com.yash.usermanagementsystem;

import io.micronaut.http.annotation.*;

@Controller("/userManagementSystem")
public class UserManagementSystemController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}