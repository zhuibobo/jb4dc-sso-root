package com.jb4dc.sso.client.filter;

import com.jb4dc.core.base.exception.JBuild4DCBaseException;
import com.jb4dc.core.base.exception.JBuild4DCGenerallyException;
import com.jb4dc.core.base.session.JB4DCSession;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface ISSoWebFilterLocationPreCheck {
    JB4DCSession preCheckSession(String absPath,ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException;
    boolean customSelfCheck();
}
