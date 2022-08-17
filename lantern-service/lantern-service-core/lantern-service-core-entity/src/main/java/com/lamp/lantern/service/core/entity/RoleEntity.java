package com.lamp.lantern.service.core.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "RoleEntity", description = "角色实体类")
public class RoleEntity implements Serializable {
  /**
   * id
   */
  private Integer id;
  /**
   * 角色名
   */
  private String roleName;
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
  private Date terminalTime;
  /**
   * 角色描述
   */
  private String roleDesc;
  /**
   * 有效时间
   */
  private Date validTime;
  /**
   * 状态
   */
  private String status;


}
