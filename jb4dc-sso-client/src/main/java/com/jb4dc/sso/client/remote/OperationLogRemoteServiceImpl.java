package com.jb4dc.sso.client.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jb4dc.base.service.IOperationLogService;
import com.jb4dc.base.service.po.OperationLogPO;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/31
 * To change this template use File | Settings | File Templates.
 */
public class OperationLogRemoteServiceImpl implements IOperationLogService {
    @Autowired
    private OperationLogRemote operationLogRemote;

    @Override
    public void writeUserLoginLog(JB4DCSession JB4DCSession, Class targetClass, HttpServletRequest request) throws JsonProcessingException, JBuild4DCGenerallyException {

    }

    @Override
    public void writeUserExitLog(JB4DCSession JB4DCSession, Class targetClass, HttpServletRequest request) throws JsonProcessingException, JBuild4DCGenerallyException {

    }

    @Override
    public void writeOperationLog(JB4DCSession JB4DCSession, String subSystemName, String moduleName, String actionName, String type, String text, String data, Class targetClass, HttpServletRequest request) throws JsonProcessingException, JBuild4DCGenerallyException {
        this.writeOperationLog(JB4DCSession,subSystemName,moduleName,actionName,type,text,data,targetClass.getName(),request);
    }

    @Override
    public void writeOperationLog(JB4DCSession JB4DCSession, String subSystemName, String moduleName, String actionName, String type, String text, String data, String targetClass, HttpServletRequest request) throws JsonProcessingException, JBuild4DCGenerallyException {
        //operationLogRemote.writeOperationLog(subSystemName,moduleName,actionName,type,text,data,targetClass);
        OperationLogPO logPO=new OperationLogPO();
        logPO.setLogSystemName(subSystemName);
        logPO.setLogModuleName(moduleName);
        logPO.setLogActionName(actionName);
        logPO.setLogType(type);
        logPO.setLogText(text);
        logPO.setLogData(data);
        logPO.setLogClassName(targetClass);
        operationLogRemote.writeOperationLog(logPO);
    }

    @Override
    public void initSystemData(JB4DCSession jb4DSession) throws JsonProcessingException, JBuild4DCGenerallyException {

    }
}
