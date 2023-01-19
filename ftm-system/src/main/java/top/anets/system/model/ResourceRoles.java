package top.anets.system.model;

import lombok.Data;

import java.util.List;

@Data
public class ResourceRoles {
    private String resource;
    private List<String> roles;
}
