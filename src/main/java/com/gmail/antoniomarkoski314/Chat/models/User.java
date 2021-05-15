package com.gmail.antoniomarkoski314.Chat.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "session_id")
    private String socketSessionId;

    private int active;

    private String roles = "";

    private String permissions = "";

    public User(String username, String password,String roles, String permissions) {
        this.username = username;
        this.password = password;
        this.socketSessionId = "socketSessionId";
        this.active = 1;
        this.roles = roles;
        this.permissions = permissions;
    }

    public User(){}

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSocketSessionId() {
        return socketSessionId;
    }

    public void setSocketSessionId(String socketSessionId) {
        this.socketSessionId = socketSessionId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionList(){
        if(this.permissions.length() > 0){
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", socketSessionId='" + socketSessionId + '\'' +
                ", active=" + active +
                ", roles='" + roles + '\'' +
                ", permissions='" + permissions + '\'' +
                '}';
    }
}
