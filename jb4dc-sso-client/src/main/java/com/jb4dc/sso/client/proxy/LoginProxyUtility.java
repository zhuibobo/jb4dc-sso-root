package com.jb4dc.sso.client.proxy;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jb4dc.core.base.session.JB4DCSession;
import com.jb4dc.core.base.tools.CookieUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.sso.client.conf.Conf;
import com.jb4dc.sso.client.utils.HttpClientUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginProxyUtility {

    public static JB4DCSession loginCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JB4DCSession jb4DSession=null;

        String sessionCode = CookieUtility.getValue(request, Conf.JB4DC_SSO_CLIENT_COOKIE_STORE_KEY);

        //如果URL中带有SSSCode的参数,则使用该参数尝试获取用户信息
        if(sessionCode==null||sessionCode.equals("")){
            sessionCode=request.getParameter(Conf.SSO_TOKEN_URL_PARA_NAME);
        }

        if(sessionCode==null||sessionCode.equals("")){
            return null;
        }

        //通过sessionCode到服务端获取用户的信息
        String url=Conf.JB4DC_SSO_SERVER_ADDRESS+Conf.JB4DC_SSO_SERVER_CONTEXT_PATH+"/Rest/SSO/Session/GetSession";

        Map<String,String> sendData=new HashMap<String,String>();
        sendData.put(Conf.SSO_TOKEN_URL_PARA_NAME,sessionCode);
        String httpResult= HttpClientUtil.getHttpPostResult(url,sendData,true);

        ObjectMapper objectMapper = new ObjectMapper();
        JBuild4DCResponseVo jBuild4DResponseVo=objectMapper.readValue(httpResult, JBuild4DCResponseVo.class);
        if(jBuild4DResponseVo.isSuccess()){
            jb4DSession = objectMapper.convertValue(jBuild4DResponseVo.getData(), JB4DCSession.class);
            //jb4DSession=(JB4DSession)jBuild4DResponseVo.getData();
        }
        else{
            throw new Exception("SSO服务端错误:"+jBuild4DResponseVo.getMessage());
        }

        //将sessionCode写入Cookie中,用于本地Session超时时,可以使用该code重新拉取用户信息!
        CookieUtility.set(response,Conf.JB4DC_SSO_CLIENT_COOKIE_STORE_KEY,sessionCode,false);

        return jb4DSession;
    }
}
