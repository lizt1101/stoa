package com.shutian.oa.modules.oa.enumeration;


/**
* @Description:    费用报销流程节点枚举类
* @Author:         lizitao
* @CreateDate:     2018/7/19 11:05
* @UpdateUser:     lizitao
* @UpdateDate:     2018/7/19 11:05
* @Version:        1.0
*/
public enum ReimTaskDefKeyEnum {

    DEPARTMENT(1,"department","departUser"),  //部门负责人、用户变量
    ACCOUNTING(2,"accounting","accountUser"),  //会计复核
    FINANCE(3,"finance","financeUser"),        //财务部负责人
    TOPMANAGER(4,"topManager","managerUser"),  //总经理意见
    RETURNME(5,"returnMe","apply");      //返回给提交人节点

    private Integer link;
    private String name;
    private String user; //指点用户变量

    // 根据link获取环节
    public static String getName(int link) {
        for (ReimTaskDefKeyEnum c : ReimTaskDefKeyEnum.values()) {
            if (c.getLink() == link) {
                return c.name;
            }
        }
        return null;
    }

    // 根据link获取指定用户变量
    public static String getUser(int link) {
        for (ReimTaskDefKeyEnum c : ReimTaskDefKeyEnum.values()) {
            if (c.getLink() == link) {
                return c.getUser();
            }
        }
        return null;
    }

    ReimTaskDefKeyEnum(Integer link, String name, String user) {
        this.link = link;
        this.name = name;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
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
}
