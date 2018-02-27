package com.lohika.morning.ecs.domain.email;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

  @Autowired
  private ExchangeService exchangeService;

  @PostMapping("email")
  public void sendTestEmail(@RequestBody EmailRequest request) throws Exception {
    EmailMessage msg = new EmailMessage(exchangeService);
    msg.setSubject(request.getSubject());
    msg.setBody(MessageBody.getMessageBodyFromText(request.getContent()));
    msg.getToRecipients().add(request.getTo());
    msg.send();
  }

}
