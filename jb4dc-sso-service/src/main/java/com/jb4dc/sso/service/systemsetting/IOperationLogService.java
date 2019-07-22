package com.jb4dc.sso.service.systemsetting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.service.IBaseService;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.sso.dbentities.systemsetting.OperationLogEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2018/7/24
 * To change this template use File | Settings | File Templates.
 */
public interface IOperationLogService extends IBaseService<OperationLogEntity> {

    void writeUserLoginLog(JB4DCSession JB4DCSession, Class targetClass, HttpServletRequest request) throws JsonProcessingException, JBuild4DCGenerallyException;

    void writeUserExitLog(JB4DCSession JB4DCSession, Class targetClass, HttpServletRequest request) throws JsonProcessingException, JBuild4DCGenerallyException;

    void writeOperationLog(JB4DCSession JB4DCSession, String subSystemName, String moduleName, String actionName, String type, String text, String data, Class targetClass, HttpServletRequest request)  throws JsonProcessingException, JBuild4DCGenerallyException;

    void initSystemData(JB4DCSession jb4DSession) throws JsonProcessingException, JBuild4DCGenerallyException;

}
