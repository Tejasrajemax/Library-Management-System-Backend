package com.library.library_management_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class AdminConfig {
// @Value.... helps to read the Admin Id or Password from Application.properties file
    @Value ("${admin.id}")
    private String adminId;

    @Value ("${admin.password}")
    private String adminPassword;

    public String getAdminId(){
        return adminId;
    }

    public String getAdminPassword(){
        return adminPassword;
    }
}
