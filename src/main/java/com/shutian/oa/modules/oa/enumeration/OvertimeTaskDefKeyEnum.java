package com.shutian.oa.modules.oa.enumeration;

public enum OvertimeTaskDefKeyEnum {

    DEPTMANAGER(1,"deptManager"), //部门审批环节key
    HR(2,"hr"),                   //人事审批环节key
    APPLY(3,"returnMe");          //返回给我

    private Integer sort;

    private String name;

    OvertimeTaskDefKeyEnum(Integer sort, String name) {
        this.sort = sort;
        this.name = name;
    }

    // 普通方法
    public static String getName(int sort) {
        for (OvertimeTaskDefKeyEnum c : OvertimeTaskDefKeyEnum.values()) {
            if (c.getSort() == sort) {
                return c.name;
            }
        }
        return null;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
