package com.shutian.oa.modules.oa.entity;


/**
* @Description:    收藏实体
* @Author:         lizitao
* @CreateDate:     2018/7/23 17:47
* @UpdateUser:     lizitao
* @UpdateDate:     2018/7/23 17:47
* @Version:        1.0

*/
public class OaCollect {

    private String id;
    private String userId;  //用户id
    private String applyId;   //申请id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
}
