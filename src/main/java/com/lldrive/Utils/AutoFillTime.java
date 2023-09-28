package com.lldrive.Utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class AutoFillTime implements MetaObjectHandler {
     @Override
     public void insertFill(MetaObject metaObject){
         Timestamp nowTime=Timestamp.valueOf(LocalDateTime.now());
         strictInsertFill(metaObject,"createTime", Timestamp.class,Timestamp.valueOf(LocalDateTime.now()));
         strictInsertFill(metaObject,"updateTime",Timestamp.class,Timestamp.valueOf(LocalDateTime.now()));
     }
     @Override
    public void updateFill(MetaObject metaObject){
         strictInsertFill(metaObject,"updateTime",Timestamp.class,Timestamp.valueOf(LocalDateTime.now()));

     }
}
