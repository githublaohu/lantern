package com.lamp.lantern.serivce.action.logon.incident;

import org.springframework.stereotype.Component;

import com.lamp.lantern.serivce.action.logon.incident.LogonIncident.LogonIncidentBuilder;
import com.lamp.lantern.serivce.action.logon.incident.TripartiteIncident.TripartiteIncidentBuilder;

@Component
public class IncidentSerivce {

	public LogonIncident createLogonIncidentcreateLogonIncident() {
		LogonIncidentBuilder builder = LogonIncident.builder();
		return builder.build();
	}
	
	public LogonIncidentBuilder createLogonIncidentBuilder() {
		LogonIncidentBuilder builder = LogonIncident.builder();
		return builder;
	}
	
	public TripartiteIncident createTripartiteIncident() {
		TripartiteIncidentBuilder builder = TripartiteIncident.builder();
		return builder.build();
	}
}
