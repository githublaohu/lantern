package com.lamp.lantern.plugins.core.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LanternServlet {
    HttpServletResponse getResponse();
    HttpServletRequest getRequest();

}
