package com.lamp.lantern.plugins.api.mode;

import lombok.Data;

@Data
public class RoleType {
    private Long roleTypeId;

    private String roleTypeDescription;

    private String roleTypeName;

    private Integer isDelete;
}
