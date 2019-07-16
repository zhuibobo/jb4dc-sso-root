package com.jb4dc.sso.webserver.rest.sso.application;

import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.encryption.nsymmetric.RSAUtility;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.files.dbentities.FileInfoEntity;
import com.jb4dc.files.service.IFileInfoService;
import com.jb4dc.sso.bo.SSOAppVo;
import com.jb4dc.sso.service.application.ISsoAppInterfaceService;
import com.jb4dc.sso.service.application.ISsoAppService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/Rest/SSO/App/Application")
public class AppRestResource {

    @Autowired
    IFileInfoService fileInfoService;

    @Autowired
    ISsoAppService ssoAppService;

    @Autowired
    ISsoAppInterfaceService ssoAppInterfaceService;

    byte[] defaultImageByte=null;

    public AppRestResource(){
        if(defaultImageByte==null){
            try {
                InputStream is = this.getClass().getResourceAsStream("/static/Themes/Default/Css/Images/DefaultSSOAppLogo.png");
                defaultImageByte = IOUtils.toByteArray(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/UploadAppLogo", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo uploadOrganLogo(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        FileInfoEntity fileInfoEntity=fileInfoService.addSmallFileToDB(JB4DCSessionUtility.getSession(),file);
        return JBuild4DCResponseVo.success(JBuild4DCResponseVo.SUCCESSMSG,fileInfoEntity);
    }

    @RequestMapping(value = "/SaveMainApp", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo saveMainApp(@RequestBody SSOAppVo entity, HttpServletRequest request) throws JBuild4DCGenerallyException {
        ssoAppService.saveIntegratedMainApp(JB4DCSessionUtility.getSession(),entity);
        return JBuild4DCResponseVo.opSuccess();
    }

    @RequestMapping(value = "/SaveSubApp", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo saveSubApp(@RequestBody SSOAppVo entity, HttpServletRequest request) throws JBuild4DCGenerallyException {
        ssoAppService.saveIntegratedSubApp(JB4DCSessionUtility.getSession(),entity);
        return JBuild4DCResponseVo.opSuccess();
    }

    @RequestMapping(value = "/GetAppLogo", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getOrganLogo(String fileId) throws IOException, JBuild4DCGenerallyException {
        FileInfoEntity fileInfoEntity=fileInfoService.getByPrimaryKey(JB4DCSessionUtility.getSession(),fileId);
        if(fileInfoEntity==null) {
            return defaultImageByte;
        }
        else{
            return fileInfoService.getContent(fileId);
        }
    }

    @RequestMapping(value = "/GetNewKeys", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo getNewKeys() throws Exception {
        KeyPair keyPair= RSAUtility.getKeyPair();
        String publicKey= RSAUtility.getPublicKeyBase64(keyPair);
        String privateKey=RSAUtility.getPrivateKeyBase64(keyPair);
        Map<String,String> keys=new HashMap<>();
        keys.put("publicKey",publicKey);
        keys.put("privateKey",privateKey);
        return JBuild4DCResponseVo.getDataSuccess(keys);
    }

    @RequestMapping(value = "/GetAllMainSsoApp", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo getAllSsoApp(){
        return JBuild4DCResponseVo.getDataSuccess(ssoAppService.getALLMainApp(JB4DCSessionUtility.getSession()));
    }

    @RequestMapping(value = "/GetAllSubSsoApp", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo getAllSubSsoApp(String appId){
        return JBuild4DCResponseVo.getDataSuccess(ssoAppService.getALLSubApp(JB4DCSessionUtility.getSession(),appId));
    }

    @RequestMapping(value = "/GetAppVo", method = RequestMethod.POST, produces = "application/json")
    public JBuild4DCResponseVo getAppVo(String appId){
        SSOAppVo ssoAppVo=ssoAppService.getAppVo(JB4DCSessionUtility.getSession(),appId);
        return JBuild4DCResponseVo.getDataSuccess(ssoAppVo);
    }

    @RequestMapping(value = "/Delete", method = RequestMethod.DELETE, produces = "application/json")
    public JBuild4DCResponseVo delete(String appId) throws JBuild4DCGenerallyException {
        ssoAppService.deleteByKey(JB4DCSessionUtility.getSession(),appId);
        return JBuild4DCResponseVo.opSuccess();
    }

    @RequestMapping(value = "/DeleteInterface", method = RequestMethod.DELETE, produces = "application/json")
    public JBuild4DCResponseVo deleteInterface(String interfaceId) throws JBuild4DCGenerallyException {
        ssoAppInterfaceService.deleteByKey(JB4DCSessionUtility.getSession(),interfaceId);
        return JBuild4DCResponseVo.opSuccess();
    }
}
