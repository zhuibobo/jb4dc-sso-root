package com.jb4dc.sso.service.organ.impl;

import com.jb4dc.base.dbaccess.exenum.EnableTypeEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.IUpdateBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.sso.dao.organ.OrganTypeMapper;
import com.jb4dc.sso.dbentities.organ.OrganTypeEntity;
import com.jb4dc.sso.service.organ.IOrganTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2018/7/27
 * To change this template use File | Settings | File Templates.
 */

@Service
public class OrganTypeServiceImpl extends BaseServiceImpl<OrganTypeEntity> implements IOrganTypeService
{
    public static String getValueExistErrorMsg(String value){
        return String.format("已经存在值为:%s的组织类型!", value);
    }

    OrganTypeMapper organTypeMapper;

    @Autowired
    public OrganTypeServiceImpl(OrganTypeMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        organTypeMapper=_defaultBaseMapper;
    }

    @Override
    public int saveSimple(JB4DCSession jb4DSession, String id, OrganTypeEntity record) throws JBuild4DCGenerallyException {
        //判断是否存在相同Value的记录
        String value = record.getOrganTypeValue();
        return super.save(jb4DSession, id, record, new IAddBefore<OrganTypeEntity>() {
            @Override
            public OrganTypeEntity run(JB4DCSession jb4DSession, OrganTypeEntity sourceEntity) throws JBuild4DCGenerallyException {

                if (organTypeMapper.selectByOrganValue(value) == null) {
                    sourceEntity.setOrganTypeStatus(EnableTypeEnum.enable.getDisplayName());
                    sourceEntity.setOrganTypeOrderNum(organTypeMapper.nextOrderNum());
                    sourceEntity.setOrganTypeCreateTime(new Date());
                    return sourceEntity;
                } else {
                    throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,getValueExistErrorMsg(value));
                }
            }
        }, new IUpdateBefore<OrganTypeEntity>() {
            @Override
            public OrganTypeEntity run(JB4DCSession jb4DSession, OrganTypeEntity sourceEntity) throws JBuild4DCGenerallyException {
                OrganTypeEntity oldEntity=organTypeMapper.selectByOrganValue(value);
                if (oldEntity!=null){
                    if(!oldEntity.getOrganTypeId().equals(sourceEntity.getOrganTypeId())){
                        throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,getValueExistErrorMsg(value));
                    }
                }
                return sourceEntity;
            }
        });
    }

    @Override
    public void createDefaultOrganType(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        OrganTypeEntity organTypeEntity=new OrganTypeEntity();
        organTypeEntity.setOrganTypeId("0");
        organTypeEntity.setOrganTypeValue("TYPE10001");
        organTypeEntity.setOrganTypeName("默认类型");
        organTypeEntity.setOrganTypeDesc("默认类型");
        organTypeEntity.setOrganTypeCreateTime(new Date());
        organTypeEntity.setOrganTypeStatus(EnableTypeEnum.enable.getDisplayName());
        this.saveSimple(jb4DSession,organTypeEntity.getOrganTypeId(),organTypeEntity);
    }

    @Override
    public void statusChange(JB4DCSession jb4DSession, String ids, String status) throws JBuild4DCGenerallyException {
        if(StringUtility.isNotEmpty(ids)) {
            String[] idArray = ids.split(";");
            for (int i = 0; i < idArray.length; i++) {
                OrganTypeEntity entity = getByPrimaryKey(jb4DSession, idArray[i]);
                entity.setOrganTypeStatus(status);
                organTypeMapper.updateByPrimaryKeySelective(entity);
            }
        }
    }

    @Override
    public void moveUp(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        OrganTypeEntity gtEntity=organTypeMapper.selectGreaterThanRecord(id);
        switchOrder(id, gtEntity);
    }

    private void switchOrder(String id, OrganTypeEntity toEntity) {
        if(toEntity !=null){
            OrganTypeEntity selfEntity=organTypeMapper.selectByPrimaryKey(id);
            int newNum= toEntity.getOrganTypeOrderNum();
            toEntity.setOrganTypeOrderNum(selfEntity.getOrganTypeOrderNum());
            selfEntity.setOrganTypeOrderNum(newNum);
            organTypeMapper.updateByPrimaryKeySelective(toEntity);
            organTypeMapper.updateByPrimaryKeySelective(selfEntity);
        }
    }

    @Override
    public void moveDown(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        OrganTypeEntity ltEntity=organTypeMapper.selectLessThanRecord(id);
        switchOrder(id, ltEntity);
    }
}
