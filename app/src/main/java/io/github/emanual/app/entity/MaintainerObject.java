package io.github.emanual.app.entity;

/**
 * Author: jayin
 * Date: 2/10/16
 */
public class MaintainerObject extends BaseEntity {
    private String name;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "MaintainerObject{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
