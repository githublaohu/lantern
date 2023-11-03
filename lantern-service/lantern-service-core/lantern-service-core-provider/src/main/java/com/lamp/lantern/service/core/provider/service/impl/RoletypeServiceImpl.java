package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.RoletypeEntity;
import com.lamp.lantern.service.core.entity.database.Roletype;
import com.lamp.lantern.service.core.provider.mapper.RoletypeMapper;
import com.lamp.lantern.service.core.service.RoletypeService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class RoletypeServiceImpl implements RoletypeService {
    @Autowired
    private RoletypeMapper roletypeMapper;
    @PostConstruct
    public void test(){
        RoletypeEntity roletypeEntity = new RoletypeEntity();
        roletypeEntity.setRoletypeName("admin");
        this.insertRoletype(roletypeEntity);
    }
    @Override
    public Integer insertRoletype(RoletypeEntity roletypeEntity) {
        return roletypeMapper.insertRoletype(roletypeEntity);
    }

    @Override
    public Integer insertRoletypes(List<RoletypeEntity> roletypeEntities) {
        roletypeEntities.forEach(this::insertRoletype);
        return 1;
    }

    @Override
    public Integer deleteRoletype(RoletypeEntity roletypeEntity) {
        return roletypeMapper.deleteRoletype(roletypeEntity);
    }

    @Override
    public Integer deleteRoletypes(List<RoletypeEntity> roletypeEntities) {
        return roletypeMapper.deleteRoletypes(roletypeEntities);
    }

}
