package it.almaviva.eai.zeebe.monitor.port.outgoing;

import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;

import java.io.IOException;

public interface INotificationPort {
    void sendIncidentNotification(IncidentDomain incidentDomain);
}
