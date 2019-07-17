package com.jb4dc.sso.webserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/Controller")
public class LoginSSOController {
    @RequestMapping(value = "/LoginSSOView", method = RequestMethod.GET)
    public ModelAndView loginSSOView(HttpServletRequest request) {

        ModelAndView modelAndView=new ModelAndView("LoginSSO");

        return modelAndView;
    }
}
