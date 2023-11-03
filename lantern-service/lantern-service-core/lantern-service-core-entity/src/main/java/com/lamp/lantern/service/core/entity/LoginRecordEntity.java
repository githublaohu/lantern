package com.lamp.lantern.service.core.entity;


import com.lamp.lantern.plugins.api.mode.LoginRecordInfo;
import com.lamp.lantern.service.core.entity.database.LoginRecord;
import com.lamp.lantern.service.core.entity.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class LoginRecordEntity extends LoginRecord implements Serializable {


}
