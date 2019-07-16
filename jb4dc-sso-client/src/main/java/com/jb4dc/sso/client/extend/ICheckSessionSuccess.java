package com.jb4dc.sso.client.extend;


import com.jb4dc.core.base.session.JB4DCSession;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface ICheckSessionSuccess {
    public void run(ServletRequest request, ServletResponse response, FilterChain chain, JB4DCSession jb4DSession);
}
