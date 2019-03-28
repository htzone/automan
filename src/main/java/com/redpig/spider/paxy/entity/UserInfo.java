package com.redpig.spider.paxy.entity;

import java.io.Serializable;

/**
 * Created by hetao on 2018/6/20.
 */
public class UserInfo implements Serializable{

    private static final long serialVersionUID = 6073235851315665822L;

    private String loginState;
    private String userCode;
    private String userNum;
    private String userName;
    private String userRealName;
    private String protocolState;
    private String joinOrgState;
    private String orgCode;
    private String orgName;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String schoolName;
    private String roleCode;
    private String roleName;

    public String getLoginState() {
        return loginState;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getProtocolState() {
        return protocolState;
    }

    public void setProtocolState(String protocolState) {
        this.protocolState = protocolState;
    }

    public String getJoinOrgState() {
        return joinOrgState;
    }

    public void setJoinOrgState(String joinOrgState) {
        this.joinOrgState = joinOrgState;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
