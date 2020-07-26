package web.dto;

import web.model.Role;
import web.model.User;

import java.util.Arrays;

public class UserDto {
    private Long id;
    private String name;
    private String password;
    private Integer age;
    private String[] roles;

    public UserDto() {
    }

    public UserDto(User user) {
        id = user.getId();
        name = user.getName();
        password = user.getPassword();
        age = user.getAge();
        Object[] objectArr = user.getRoles().stream().map(Role::getNameRole).toArray();
        this.roles = Arrays.copyOf(objectArr, objectArr.length, String[].class);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getStringRole() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < roles.length; i++) {
            stringBuilder.append(roles[i]);
            if (roles.length > 1 && i < roles.length - 1){
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
