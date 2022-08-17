package com.lamp.lantern.service.core.entity;


import java.io.Serializable;
import java.util.Date;


public class AuthRoleRelationEntity implements Serializable {
  /**
   * id
   */
  private Integer id;
  /**
   * 权限id
   */
  private Integer authorityId;
  /**
   * 操作id
   */
  private Integer operatorId;
  /**
   * 角色id
   */
  private Integer roleId;
  /**
   * 类型
   */
  private String type;
  /**
   * 类型id
   */
  private Integer typeId;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 修改时间
   */
  private Date updateTime;
  /**
   * 结束时间
   */
  private String terminalTime;
  /**
   * 有效时间
   */
  private Date validTime;
  /**
   * 状态
   */
  private String status;


}
