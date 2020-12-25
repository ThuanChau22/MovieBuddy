package moviebuddy.model;

public class Role {
    private int roleId;
    private String title;

    public Role(int roleId){
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
