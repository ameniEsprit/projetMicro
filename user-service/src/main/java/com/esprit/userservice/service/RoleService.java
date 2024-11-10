package com.esprit.userservice.service;

import com.esprit.userservice.entity.Role;
import com.esprit.userservice.entity.RoleType;
import com.esprit.userservice.securityconfig.KeycloakConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService  {

    private final Keycloak keycloakInstance = KeycloakConfig.getInstance();
    private static final String REALM_NAME = "whereToGo";

    public List<Role> getRoles() {
        try {
            List<RoleRepresentation> roleRepresentations = keycloakInstance.realm(REALM_NAME).roles().list();
            return mapRoles(roleRepresentations);
        } catch (Exception e) {
            log.error("Error retrieving roles from Keycloak", e);
            return Collections.emptyList();
        }
    }

    public Role getRole(RoleType roleName) {
        try {
            String roleNameStr = roleName.toString();
            return mapRole(keycloakInstance.realm(REALM_NAME).roles().get(roleNameStr).toRepresentation());
        } catch (Exception e) {
            log.error("Error retrieving role '{}' from Keycloak", roleName, e);
            return null;
        }
    }

    public void createRole(Role role) {
        try {
            RoleRepresentation roleRepresentation = mapRoleRep(role);
            keycloakInstance.realm(REALM_NAME).roles().create(roleRepresentation);
            log.info("Role '{}' created successfully", role.getRoleType());
        } catch (Exception e) {
            log.error("Error creating role in Keycloak", e);
        }
    }

    public void updateRole(Role role, String roleName) {
        try {
            RoleRepresentation roleRepresentation = mapRoleRep(role);
            keycloakInstance.realm(REALM_NAME).roles().get(roleName).update(roleRepresentation);
            log.info("Role '{}' updated successfully", roleName);
        } catch (Exception e) {
            log.error("Error updating role '{}' in Keycloak", roleName, e);
        }
    }

    public void deleteRole(String roleName) {
        try {
            keycloakInstance.realm(REALM_NAME).roles().deleteRole(roleName);
            log.info("Role '{}' deleted successfully", roleName);
        } catch (Exception e) {
            log.error("Error deleting role '{}' from Keycloak", roleName, e);
        }
    }

    public void assignRole(String userId, RoleType roleName) {
        try {
            String roleNameStr = roleName.toString();

            UserResource userResource = keycloakInstance.realm(REALM_NAME).users().get(userId);
            RolesResource rolesResource = keycloakInstance.realm(REALM_NAME).roles();
            RoleRepresentation representation = rolesResource.get(roleNameStr).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(representation));
            log.info("Role '{}' assigned to user '{}'", roleName, userId);
        } catch (Exception e) {
            log.error("Error assigning role '{}' to user '{}'", roleName, userId, e);
        }
    }

    private RoleRepresentation mapRoleRep(Role role) {
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(role.getRoleType().toString());
        return roleRep;
    }

    private List<Role> mapRoles(List<RoleRepresentation> roleRepresentations) {
        List<Role> roles = new ArrayList<>();
        if (roleRepresentations != null && !roleRepresentations.isEmpty()) {
            roleRepresentations.forEach(roleRep -> roles.add(mapRole(roleRep)));
        }
        return roles;
    }

    private Role mapRole(RoleRepresentation roleRep) {
        Role role = new Role();
        role.setRoleType(RoleType.valueOf(roleRep.getName().toUpperCase()));
        return role;
    }
}
