package com.rog.EShop.dto.keycloak;

import java.util.List;

public class MappingsRepresentation {
    private List<RoleRepresentation> realmMappings;

    public List<RoleRepresentation> getRealmMappings() {
        return realmMappings;
    }

    public void setRealmMappings(List<RoleRepresentation> realmMappings) {
        this.realmMappings = realmMappings;
    }
}
