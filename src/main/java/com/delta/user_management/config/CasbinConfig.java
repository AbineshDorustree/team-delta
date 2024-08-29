package com.delta.user_management.config;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class CasbinConfig {

//    @Bean
//    public Enforcer casbinEnforcer() {
//        // Load the Casbin model and policy files from the classpath
//        String modelPath = getClass().getClassLoader().getResource("casbin/model.conf").getPath();
//        String policyPath = getClass().getClassLoader().getResource("casbin/policy.csv").getPath();
//
//        // Create the enforcer with the model and policy files
//        return new Enforcer(modelPath, policyPath);
//    }

//    @Bean
//    public Enforcer casbinEnforcer() {
//        // Load the Casbin model and policy files from the classpath
//        InputStream modelStream = getClass().getClassLoader().getResourceAsStream("casbin/model.conf");
//        InputStream policyStream = getClass().getClassLoader().getResourceAsStream("casbin/policy.csv");
//
//        if (modelStream == null || policyStream == null) {
//            throw new IllegalStateException("Casbin configuration files are missing from the classpath");
//        }
//
//        // Create the enforcer with the model and policy streams
//        return new Enforcer(modelStream, policyStream);
//    }

    @Bean
    public Enforcer casbinEnforcer() throws IOException {
        // Load the Casbin model and policy files from the classpath
        InputStream modelStream = getClass().getClassLoader().getResourceAsStream("casbin/model.conf");
        InputStream policyStream = getClass().getClassLoader().getResourceAsStream("casbin/policy.csv");

        if (modelStream == null || policyStream == null) {
            throw new IllegalStateException("Casbin configuration files are missing from the classpath");
        }

        // Create temporary files to hold the configuration
        File modelFile = File.createTempFile("model", ".conf");
        File policyFile = File.createTempFile("policy", ".csv");

        try (FileOutputStream modelOutputStream = new FileOutputStream(modelFile);
             FileOutputStream policyOutputStream = new FileOutputStream(policyFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = modelStream.read(buffer)) != -1) {
                modelOutputStream.write(buffer, 0, bytesRead);
            }

            while ((bytesRead = policyStream.read(buffer)) != -1) {
                policyOutputStream.write(buffer, 0, bytesRead);
            }
        }

        // Clean up the temporary files on exit
        modelFile.deleteOnExit();
        policyFile.deleteOnExit();

        // Create the enforcer with the model and policy file paths
        return new Enforcer(modelFile.getAbsolutePath(), policyFile.getAbsolutePath());
    }

}
