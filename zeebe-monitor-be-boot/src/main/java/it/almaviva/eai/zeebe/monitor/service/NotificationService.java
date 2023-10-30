package it.almaviva.eai.zeebe.monitor.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.INotificationPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "mail", name = "active", havingValue = "true")
public class NotificationService  implements INotificationPort {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration freemarkerConfig;

    @Value("${mail.to}")
    String[] to;

    @Value("${mail.from}")
    String from;

    @Value("${mail.system}")
    String system;

    @Override
    public void sendIncidentNotification(IncidentDomain incidentDomain)  {

        Map<String, Object> model = new HashMap<>();

        model.put("system", system);
        model.put("Key",incidentDomain.getKey());
        model.put("BpmnProcessId",incidentDomain.getBpmnProcessId());
        model.put("Created",incidentDomain.getCreated());
        model.put("ElementInstanceKey",incidentDomain.getElementInstanceKey());
        model.put("ErrorMessage",incidentDomain.getErrorMessage());
        model.put("JobKey",incidentDomain.getJobKey());
        model.put("WorkflowInstanceKey",incidentDomain.getWorkflowInstanceKey());
        model.put("WorkflowKey",incidentDomain.getWorkflowKey());


        String html;
		try {
			html = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate("incidentNotification.ftlh"), model);
			 MimeMessage msg = javaMailSender.createMimeMessage();
		     MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		     helper.setFrom(from);
		     helper.setTo(to);
		     helper.setSubject("Error Notification");
		     helper.setText(html, true);
		     javaMailSender.send(msg);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		} 
       
    }

}
