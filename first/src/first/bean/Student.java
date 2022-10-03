package first.bean;

import java.util.Objects;

/**
 * @author Sepnine
 * @version 1.0
 * @description: TODO
 * @date 2022/9/25 11:20
 */
public class Student {
    private String id;
    private String name;
    private String gender;
    private String strClass;
    private String mobile;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(name, student.name) && Objects.equals(gender, student.gender) && Objects.equals(strClass, student.strClass) && Objects.equals(mobile, student.mobile) && Objects.equals(email, student.email);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", strClass='" + strClass + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, strClass, mobile, email);
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStrClass() {
        return strClass;
    }

    public void setStrClass(String strClass) {
        this.strClass = strClass;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Student() {
    }

    public Student(String id, String name, String gender, String strClass, String mobile, String email) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.strClass = strClass;
        this.mobile = mobile;
        this.email = email;
    }
}
