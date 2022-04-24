//package com.lamp.lantern.serivce.action.logon.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.lamp.lantern.serivce.action.logon.incident.IncidentSerivce;
//
///**
// * 1.
// * @author laohu
// *
// */
//@RestController
//@RequestMapping("partyLogin")
//public class TripartiteLogonController {
//
//
//	@Autowired
//	private IncidentSerivce incidentSerivce;
//
//
//	public void acquireTripartiteLoginInfo() {
//		// 得到第三方信息
//		// 请求第三方
//		// 第三方返回一个url
//		// 要求用户重定向
//		incidentSerivce.createTripartiteIncident();
//	}
//
//	public void tripartiteLogin() {
//		incidentSerivce.getLogonIncidentBuilder();
//	}
//
//
//}
