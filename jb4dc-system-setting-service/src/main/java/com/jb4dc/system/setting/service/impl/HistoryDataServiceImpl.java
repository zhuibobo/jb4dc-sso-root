package com.jb4dc.system.setting.service.impl;

import com.jb4dc.base.service.ISQLBuilderService;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.UUIDUtility;
import com.jb4dc.system.setting.dao.HistoryDataMapper;
import com.jb4dc.system.setting.dbentities.HistoryDataEntity;
import com.jb4dc.system.setting.service.IHistoryDataService;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/2/25
 * To change this template use File | Settings | File Templates.
 */
public class HistoryDataServiceImpl implements IHistoryDataService
{
    HistoryDataMapper historyDataMapper;
    public HistoryDataServiceImpl(HistoryDataMapper _defaultBaseMapper){
        historyDataMapper=_defaultBaseMapper;
    }

    @Override
    public void keepRecordData(JB4DCSession JB4DCSession, Object record){
        HistoryDataEntity historyDataEntity=new HistoryDataEntity();
        historyDataEntity.setHistoryId("");
        historyDataEntity.setHistoryOrganId("");
        historyDataEntity.setHistoryOrganName("");
        historyDataEntity.setHistoryUserId("");
        historyDataEntity.setHistoryUserName("");
        historyDataEntity.setHistoryCreateTime(new Date());
        historyDataEntity.setHistoryTableName("");
        historyDataEntity.setHistoryRecordId("");
        historyDataEntity.setHistoryRecord("");

        historyDataMapper.insert(historyDataEntity);
    }

    @Override
    public void keepRecordData(JB4DCSession JB4DCSession, String tableName, String recordId, String recordData){
        HistoryDataEntity historyDataEntity=new HistoryDataEntity();
        historyDataEntity.setHistoryId(UUIDUtility.getUUID());
        historyDataEntity.setHistoryOrganId(JB4DCSession.getOrganId());
        historyDataEntity.setHistoryOrganName(JB4DCSession.getOrganName());
        historyDataEntity.setHistoryUserId(JB4DCSession.getUserId());
        historyDataEntity.setHistoryUserName(JB4DCSession.getUserName());
        historyDataEntity.setHistoryCreateTime(new Date());
        historyDataEntity.setHistoryTableName(tableName);
        historyDataEntity.setHistoryRecordId(recordId);
        historyDataEntity.setHistoryRecord(recordData);

        historyDataMapper.insert(historyDataEntity);
    }
}
