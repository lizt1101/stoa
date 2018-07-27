package com.shutian.oa.modules.oa.enumeration;

public enum LeaveTaskDefKeyEnum {

    DEPTMANAGER(1,"department","departUser"), //部门审批环节key
    HR(2,"hr","hrUser"),                   //人事审批环节key
    APPLY(3,"returnMe","apply");      //驳回给我

    private Integer link;
    private String name;
    private String user; //指点用户变量

    LeaveTaskDefKeyEnum(Integer link, String name, String user) {
        this.link = link;
        this.name = name;
        this.user = user;
    }

    public Integer getLink() {
        return link;
    }

    public void setLink(Integer link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
