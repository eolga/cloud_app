package com.cc.iam;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.iam.v1.Iam;
import com.google.api.services.iam.v1.IamScopes;
import com.google.api.services.iam.v1.model.CreateRoleRequest;
import com.google.api.services.iam.v1.model.Role;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class iam {


        private static String baseSource = "projects/organic-palace-220908";

        public static void main(String[] args) throws Exception {
            // Get credentials
            GoogleCredential credential =
                    GoogleCredential.getApplicationDefault()
                            .createScoped(Collections.singleton(IamScopes.CLOUD_PLATFORM));
            // Create the Cloud IAM service object
            Iam service =
                    new Iam.Builder(
                            GoogleNetHttpTransport.newTrustedTransport(),
                            JacksonFactory.getDefaultInstance(),
                            credential)
                            .setApplicationName("My First Project")
                            .build();

            String command;
            BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
            while (!(command = reader.readLine()).equalsIgnoreCase("exit")){
                if ("print_roles".equalsIgnoreCase(command)){
                    printRoles(service);
                }
                if ("help".equalsIgnoreCase(command)){
                    System.out.print("YES!");
                }
                if ("create_role".equalsIgnoreCase(command)){
                    System.out.print("roleId: ");
                    String roleId = reader.readLine();
                    System.out.print("title: ");
                    String title = reader.readLine();
                    System.out.print("description: ");
                    String description = reader.readLine();
                    Role role = createRole(service,title,description,roleId);
                    System.out.println(role);
                }
            }
        }


        public static void printRoles(Iam service) throws Exception{
            List<Role> roles = service.projects().roles().list(baseSource).execute().getRoles();
            // Process the response
            for (Role role : roles) {
                System.out.println("Title: " + role.getTitle());
                System.out.println("Name: " + role.getName());
                System.out.println("Description: " + role.getDescription());
                System.out.println();
            }
        }

        public static Role createRole(Iam service, String title, String description, String roleId) throws Exception{
            Role newRole = new Role();
            newRole.setTitle(title);
            newRole.setDescription(description);

            CreateRoleRequest request = new CreateRoleRequest();
            request.setRoleId(roleId);
            request.setRole(newRole);

            Role newRole2 = service.projects().roles().create(baseSource,request).execute();
            return newRole2;
        }


    }


