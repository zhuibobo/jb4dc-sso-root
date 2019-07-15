package com.jb4dc.sso.service.organ.impl;

import com.jb4dc.base.dbaccess.exenum.EnableTypeEnum;
import com.jb4dc.base.dbaccess.exenum.TrueFalseEnum;
import com.jb4dc.base.service.IAddBefore;
import com.jb4dc.base.service.impl.BaseServiceImpl;
import com.jb4dc.base.tools.BeanUtility;
import com.jb4dc.base.tools.FileUtility;
import com.jb4dc.base.ymls.JBuild4DCYaml;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.StringUtility;
import com.jb4dc.core.base.tools.XMLDocumentUtility;
import com.jb4dc.sso.dao.organ.OrganMapper;
import com.jb4dc.sso.dbentities.organ.OrganEntity;
import com.jb4dc.sso.service.organ.IOnOrganChangeAware;
import com.jb4dc.sso.service.organ.IOrganService;
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

    String configResource= "/config/OrganInitConfig.xml";
    Document xmlDocument=null;

    OrganMapper organMapper;

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
}
