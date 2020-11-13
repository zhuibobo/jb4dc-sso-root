package com.jb4dc.sso.service.organ.impl;

import com.jb4dc.base.service.exenum.EnableTypeEnum;
import com.jb4dc.base.service.exenum.TrueFalseEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.exenum.UserTypeEnum;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.base.tools.BeanUtility;
import com.jb4dc.base.ymls.JBuild4DCYaml;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.FileUtility;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.core.base.tools.XMLDocumentUtility;
import com.jb4dc.sso.dao.organ.OrganMapper;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.po.DepartmentUserPO;
import com.jb4dc.sso.service.department.IDepartmentService;
import com.jb4dc.sso.service.department.IDepartmentUserService;
import com.jb4dc.sso.service.organ.IOnOrganChangeAware;
import com.jb4dc.sso.service.organ.IOrganService;
import com.jb4dc.sso.service.organ.IOrganTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2018/7/27
 * To change this template use File | Settings | File Templates.
 */

@Service
public class OrganServiceImpl extends BaseServiceImpl<OrganEntity> implements IOrganService
{
    private String rootId="0";
    private String rootParentId="-1";

    String configResource= "/config/organ-init-config.xml";
    Document xmlDocument=null;

    OrganMapper organMapper;

    @Autowired
    IOrganTypeService organTypeService;

    @Autowired
    IDepartmentUserService departmentUserService;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    public OrganServiceImpl(OrganMapper _defaultBaseMapper){
        super(_defaultBaseMapper);
        organMapper=_defaultBaseMapper;
    }

    @Override
    @Transactional(rollbackFor= JBuild4DCGenerallyException.class)
    public int saveSimple(JB4DCSession jb4DSession, String id, OrganEntity record) throws JBuild4DCGenerallyException {
        boolean isNew = false;
        if (this.getByPrimaryKey(jb4DSession, id) == null) {
            isNew = true;
        }

        int result = super.save(jb4DSession, id, record, new IAddBefore<OrganEntity>() {
            @Override
            public OrganEntity run(JB4DCSession jb4DSession, OrganEntity sourceEntity) throws JBuild4DCGenerallyException {
                sourceEntity.setOrganChildCount(0);
                sourceEntity.setOrganOrderNum(organMapper.nextOrderNum());
                sourceEntity.setOrganCreateTime(new Date());
                String parentIdList;
                if (sourceEntity.getOrganId().equals(rootId)) {
                    parentIdList = rootParentId;
                    sourceEntity.setOrganParentId(rootParentId);
                } else {
                    OrganEntity parentEntity = organMapper.selectByPrimaryKey(sourceEntity.getOrganParentId());
                    parentIdList = parentEntity.getOrganParentIdList();
                    parentEntity.setOrganChildCount(parentEntity.getOrganChildCount() + 1);
                    organMapper.updateByPrimaryKeySelective(parentEntity);
                }
                sourceEntity.setOrganParentIdList(parentIdList + "*" + sourceEntity.getOrganId());
                return sourceEntity;
            }
        });

        try {
            if (isNew) {
                this.awareCreatedOrgan(jb4DSession, record);
            } else {
                this.awareUpdatedOrgan(jb4DSession, record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void awareCreatedOrgan(JB4DCSession jb4DSession, OrganEntity organEntity) throws JBuild4DCGenerallyException, IOException, SAXException, ParserConfigurationException {
        xmlDocument=getOrganInitConfigDoc();
        try {
            List<Node> nodeList= XMLDocumentUtility.parseForNodeList(xmlDocument,"//Bean");
            for (Node node : nodeList) {
                String beanName= XMLDocumentUtility.getAttribute(node,"Name");
                IOnOrganChangeAware createOrganAware= BeanUtility.getBean(beanName);
                if(createOrganAware==null){
                    throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"再容器中找不到名称为"+beanName+"的Bean");
                }
                else {
                    createOrganAware.organCreated(jb4DSession,organEntity);
                }
            }

        } catch (XPathExpressionException e) {
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,e);
        }
    }

    private void awareUpdatedOrgan(JB4DCSession jb4DSession, OrganEntity organEntity) throws JBuild4DCGenerallyException, IOException, SAXException, ParserConfigurationException {
        xmlDocument=getOrganInitConfigDoc();
        try {
            List<Node> nodeList= XMLDocumentUtility.parseForNodeList(xmlDocument,"//Bean");
            for (Node node : nodeList) {
                String beanName= XMLDocumentUtility.getAttribute(node,"Name");
                IOnOrganChangeAware createOrganAware= BeanUtility.getBean(beanName);
                if(createOrganAware==null){
                    throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,"再容器中找不到名称为"+beanName+"的Bean");
                }
                else {
                    createOrganAware.organUpdated(jb4DSession,organEntity);
                }
            }

        } catch (XPathExpressionException e) {
            throw new JBuild4DCGenerallyException(JBuild4DCGenerallyException.EXCEPTION_SSO_CODE,e);
        }
    }

    private Document getOrganInitConfigDoc() throws JBuild4DCGenerallyException, ParserConfigurationException, SAXException, IOException {
        //InputStream inputStream = this.getClass().getResourceAsStream(configResource);
        InputStream inputStream = FileUtility.getStreamByLevel(configResource);
        Document _xml = XMLDocumentUtility.parseForDoc(inputStream);
        return _xml;
    }

    @Override
    public OrganEntity createRootOrgan(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        OrganEntity organEntity=new OrganEntity();
        //treeTableEntity.setDdglParentId(rootId);
        organEntity.setOrganCreateTime(new Date());
        organEntity.setOrganCode("0000");
        organEntity.setOrganId(rootId);
        organEntity.setOrganName("组织机构管理");
        organEntity.setOrganNo("0000");
        organEntity.setOrganIsVirtual(TrueFalseEnum.False.getDisplayName());
        organEntity.setOrganParentId(rootParentId);
        organEntity.setOrganStatus(EnableTypeEnum.enable.getDisplayName());
        organEntity.setOrganShortName("组织管理");
        this.saveSimple(jb4DSession,organEntity.getOrganId(),organEntity);
        return organEntity;
    }

    @Override
    public List<OrganEntity> getALLEnableOrgan() {
        return organMapper.selectAllEnableOrgan();
    }

    @Override
    public List<OrganEntity> getALLEnableOrganMinProp() {
        return organMapper.selectAllEnableOrganMinProp();
    }

    @Override
    public void deleteByOrganName(JB4DCSession session, String organName, String warningOperationCode) {
        if(JBuild4DCYaml.getWarningOperationCode().equals(warningOperationCode)){
            organMapper.deleteByOrganName(organName);
        }
    }

    @Override
    public int deleteByKey(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        OrganEntity organEntity=getByPrimaryKey(jb4DSession,id);
        organEntity.setOrganStatus(EnableTypeEnum.delete.getDisplayName());
        return organMapper.updateByPrimaryKey(organEntity);
        //return super.deleteByKey(jb4DSession, id);
    }

    @Override
    public void statusChange(JB4DCSession jb4DSession, String ids, String status) throws JBuild4DCGenerallyException {
        if(StringUtility.isNotEmpty(ids)) {
            String[] idArray = ids.split(";");
            for (int i = 0; i < idArray.length; i++) {
                OrganEntity entity = getByPrimaryKey(jb4DSession, idArray[i]);
                entity.setOrganStatus(status);
                organMapper.updateByPrimaryKeySelective(entity);
            }
        }
    }

    @Override
    public void moveUp(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        OrganEntity selfEntity=organMapper.selectByPrimaryKey(id);
        OrganEntity ltEntity=organMapper.selectLessThanRecord(id,selfEntity.getOrganParentId());
        switchOrder(ltEntity,selfEntity);
    }

    @Override
    public void moveDown(JB4DCSession jb4DSession, String id) throws JBuild4DCGenerallyException {
        OrganEntity selfEntity=organMapper.selectByPrimaryKey(id);
        OrganEntity ltEntity=organMapper.selectGreaterThanRecord(id,selfEntity.getOrganParentId());
        switchOrder(ltEntity,selfEntity);
    }

    private void switchOrder(OrganEntity toEntity,OrganEntity selfEntity) {
        if(toEntity !=null){
            int newNum= toEntity.getOrganOrderNum();
            toEntity.setOrganOrderNum(selfEntity.getOrganOrderNum());
            selfEntity.setOrganOrderNum(newNum);
            organMapper.updateByPrimaryKeySelective(toEntity);
            organMapper.updateByPrimaryKeySelective(selfEntity);
        }
    }

    private OrganEntity getOrganEntity(String code,String id,String no,String name){
        OrganEntity organEntity=new OrganEntity();
        organEntity.setOrganCreateTime(new Date());
        organEntity.setOrganCode(code);
        organEntity.setOrganId(id);
        organEntity.setOrganName(name);
        organEntity.setOrganNo(no);
        organEntity.setOrganIsVirtual(TrueFalseEnum.False.getDisplayName());
        organEntity.setOrganParentId(rootId);
        organEntity.setOrganStatus(EnableTypeEnum.enable.getDisplayName());
        organEntity.setOrganShortName(name);
        return organEntity;
    }

    private DepartmentUserPO getDepartmentUserPO(JB4DCSession jb4DSession,String userId,String organId,String accountName,String userName,String Phone,UserTypeEnum userTypeEnum,String title) throws JBuild4DCGenerallyException {
        DepartmentUserPO newDepartmentUserVo=departmentUserService.getEmptyNewVo(null,departmentService.getRootDepartmentByOrganId(jb4DSession,organId).getDeptId());
        newDepartmentUserVo.getUserEntity().setUserId(userId);
        newDepartmentUserVo.getDepartmentUserEntity().setDuUserId(userId);
        newDepartmentUserVo.getDepartmentUserEntity().setDuId(userId);
        newDepartmentUserVo.getUserEntity().setUserType(userTypeEnum.getDisplayName());
        newDepartmentUserVo.getUserEntity().setUserAccount(accountName);
        newDepartmentUserVo.getUserEntity().setUserName(userName);
        newDepartmentUserVo.getUserEntity().setUserPhoneNumber(Phone);
        newDepartmentUserVo.getDepartmentUserEntity().setDuTitle(title);
        return newDepartmentUserVo;
    }

    @Override
    public void initSystemData(JB4DCSession jb4DSession) throws JBuild4DCGenerallyException {
        organTypeService.deleteByKeyNotValidate(jb4DSession,"0", JBuild4DCYaml.getWarningOperationCode());
        organTypeService.createDefaultOrganType(jb4DSession);

        deleteByKeyNotValidate(jb4DSession,"0", JBuild4DCYaml.getWarningOperationCode());
        this.createRootOrgan(jb4DSession);

        OrganEntity tlOrgan=getOrganEntity("0001","10001","0001","系统管理组");

        this.deleteByKeyNotValidate(jb4DSession,tlOrgan.getOrganId(),JBuild4DCYaml.getWarningOperationCode());
        this.saveSimple(jb4DSession,tlOrgan.getOrganId(),tlOrgan);

        String userId="Alex4D";
        departmentUserService.deleteDepartUserAndUser(jb4DSession,userId);

        DepartmentUserPO newDepartmentUserVo=getDepartmentUserPO(jb4DSession,userId,tlOrgan.getOrganId(),"Alex4D","Alex","13927425407",UserTypeEnum.manager,"管理员");
        departmentUserService.save(jb4DSession,userId,newDepartmentUserVo,"j4d123456");

        userId="Manager";
        departmentUserService.deleteDepartUserAndUser(jb4DSession,userId);

        newDepartmentUserVo=getDepartmentUserPO(jb4DSession,userId,tlOrgan.getOrganId(),"manager","总管理员","13927425407",UserTypeEnum.manager,"管理员");
        departmentUserService.save(jb4DSession,userId,newDepartmentUserVo,"j4d123456");

        OrganEntity zlOrgan=getOrganEntity("0002","10002","0002","卓联科技");

        this.deleteByKeyNotValidate(jb4DSession,zlOrgan.getOrganId(),JBuild4DCYaml.getWarningOperationCode());
        this.saveSimple(jb4DSession,zlOrgan.getOrganId(),zlOrgan);

        userId="Zhuang_Rui_Bo_UID";
        departmentUserService.deleteDepartUserAndUser(jb4DSession,userId);
        newDepartmentUserVo=getDepartmentUserPO(jb4DSession,userId,zlOrgan.getOrganId(),"zhuangrb","庄锐波","13927425407",UserTypeEnum.normalUser,"庄");
        departmentUserService.save(jb4DSession,userId,newDepartmentUserVo,"j4d123456");

        userId="Shi_Ming_Hua_UID";
        departmentUserService.deleteDepartUserAndUser(jb4DSession,userId);
        newDepartmentUserVo=getDepartmentUserPO(jb4DSession,userId,zlOrgan.getOrganId(),"shimh","石明华","13927425407",UserTypeEnum.normalUser,"石");
        departmentUserService.save(jb4DSession,userId,newDepartmentUserVo,"j4d123456");

        userId="Yuang_Hong_Ling_UID";
        departmentUserService.deleteDepartUserAndUser(jb4DSession,userId);
        newDepartmentUserVo=getDepartmentUserPO(jb4DSession,userId,zlOrgan.getOrganId(),"yuanghl","袁红林","13927425407",UserTypeEnum.normalUser,"袁");
        departmentUserService.save(jb4DSession,userId,newDepartmentUserVo,"j4d123456");

        userId="Li_Zheng_UID";
        departmentUserService.deleteDepartUserAndUser(jb4DSession,userId);
        newDepartmentUserVo=getDepartmentUserPO(jb4DSession,userId,zlOrgan.getOrganId(),"lizheng","李真","13927425407",UserTypeEnum.normalUser,"李");
        departmentUserService.save(jb4DSession,userId,newDepartmentUserVo,"j4d123456");
    }


}
