package com.esprit.userservice.service;


import com.esprit.userservice.dto.AuthenticationRequest;
import com.esprit.userservice.dto.RegisterRequest;
import com.esprit.userservice.entity.Role;
import com.esprit.userservice.entity.RoleType;
import com.esprit.userservice.entity.User;
import com.esprit.userservice.exception.EmailExistsExecption;
import com.esprit.userservice.repository.RoleRepository;
import com.esprit.userservice.repository.UserRepository;
import com.esprit.userservice.securityconfig.KeycloakConfig;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${keycloak.client-secret}")
    private String clientSecret;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    public String login(AuthenticationRequest authenticationRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = "grant_type=password&username=" + authenticationRequest.getUsername() + "&password=" + authenticationRequest.getPassword() +
                "&client_id=login-app&client_secret="+clientSecret;
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:9090/realms/whereToGo/protocol/openid-connect/token",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        String responseBody = responseEntity.getBody();

        return responseBody;
    }

    public String register(RegisterRequest userDto) {
        UserRepresentation userRep= mapUserRep(userDto);
        Keycloak keycloak = KeycloakConfig.getInstance();
        List<UserRepresentation> usernameRepresentations = keycloak.realm("whereToGo").users().searchByUsername(userDto.getEmail(),true);
        List<UserRepresentation> emailRepresentations = keycloak.realm("whereToGo").users().searchByEmail(userDto.getEmail(),true);

        if(!(usernameRepresentations.isEmpty() && emailRepresentations.isEmpty())){
            throw new EmailExistsExecption("username or email already exists");
        }
        Response response = keycloak.realm("whereToGo").users().create(userRep);


        Role role=roleRepository.findByRoleType(RoleType.USER).get();
        User user=new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getMobileNumber());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        String photoName= fileService.uploadFile(userDto.getPhotoProfile());
        user.setPhotoprofile(photoName);
        userRepository.save(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user");
        }
        String userId = CreatedResponseUtil.getCreatedId(response);
        System.out.println("userID est de createUser"+userId);
        roleService.getRole(RoleType.USER);
        roleService.assignRole(userId,RoleType.USER);
        UserResource userResource = keycloak.realm("whereToGo").users().get(userId);
        //   userResource.sendVerifyEmail();

        return "User created";




    }
    public String createOwner(RegisterRequest userDto) {
        UserRepresentation userRep= mapUserRep(userDto);
        Keycloak keycloak = KeycloakConfig.getInstance();
        List<UserRepresentation> usernameRepresentations = keycloak.realm("whereToGo").users().searchByUsername(userDto.getEmail(),true);
        List<UserRepresentation> emailRepresentations = keycloak.realm("whereToGo").users().searchByEmail(userDto.getEmail(),true);

        if(!(usernameRepresentations.isEmpty() && emailRepresentations.isEmpty())){
            throw new EmailExistsExecption("username or email already exists");
        }
        Response response = keycloak.realm("whereToGo").users().create(userRep);


        Role role=roleRepository.findByRoleType(RoleType.USER).get();
        User user=new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getMobileNumber());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        String photoName= fileService.uploadFile(userDto.getPhotoProfile());
        user.setPhotoprofile(photoName);
        userRepository.save(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user");
        }
        String userId = CreatedResponseUtil.getCreatedId(response);
        System.out.println("userID est de createUser"+userId);
        roleService.getRole(RoleType.USER);
        roleService.assignRole(userId,RoleType.USER);
        UserResource userResource = keycloak.realm("whereToGo").users().get(userId);
        //   userResource.sendVerifyEmail();

        return "User created";




    }
    private UserRepresentation mapUserRep(RegisterRequest userDto) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setFirstName(userDto.getFirstName());
        userRep.setUsername(userDto.getEmail());
        userRep.setLastName(userDto.getLastName());
        userRep.setEmail(userDto.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(false);

        Map<String, List<String>> attributes = new HashMap<>();
        if (userDto.getMobileNumber() != null) {
            List<String> mobileNumber = new ArrayList<>();
            mobileNumber.add(userDto.getMobileNumber());
            attributes.put("mobileNumber", mobileNumber);
        }
        userRep.setAttributes(attributes);

        if (userDto.getPassword() != null) {
            List<CredentialRepresentation> creds = new ArrayList<>();
            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setTemporary(false);
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(userDto.getPassword());
            creds.add(cred);
            userRep.setCredentials(creds);
        }
        return userRep;
    }

    public void forgotPassword(String email) {
        Keycloak keycloak = KeycloakConfig.getInstance();
        List<UserRepresentation> emailRepresentations = keycloak.realm("whereToGo").users().searchByEmail(email,true);
        System.out.println("********test0******** "+emailRepresentations);
        if (!emailRepresentations.isEmpty()) {
            UserRepresentation userRepresentation = emailRepresentations.get(0); // Get the first user
            System.out.println("********test1******** "+userRepresentation);

            try {
                UserResource userResource = keycloak.realm("pfe").users().get(userRepresentation.getId());
                List<String> actions = new ArrayList<>();
                actions.add("UPDATE_PASSWORD");
                userResource.executeActionsEmail(actions);

                System.out.println("Password reset email sent successfully.");
            } catch (Exception e) {
                System.err.println("Error resetting password: " + e.getMessage());
                throw new RuntimeException("Failed to reset password.");
            }
        } else {
            System.err.println("User with username '" + email + "' not found.");
            throw new RuntimeException("User not found.");
        }
    }

}




