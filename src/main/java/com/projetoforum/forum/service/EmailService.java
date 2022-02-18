package com.projetoforum.forum.service;

import com.projetoforum.forum.controller.UsuarioController;
import com.projetoforum.forum.model.EmailsUsuarios;
import com.projetoforum.forum.model.Resposta;
import com.projetoforum.forum.model.Topico;
import com.projetoforum.forum.model.Usuario;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class EmailService {
    final Configuration configuration;
    final JavaMailSender javaMailSender;

    @Autowired
    RecomendacoesService recomendacoesService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EmailsUsuariosService emailsUsuariosService;

    boolean alreadyExecuted = false;

    List<String> emails;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Scheduled(cron = "0 10 8 * * ?", zone = "America/Sao_Paulo")
    public void emails() throws MessagingException, TemplateException, IOException {

        sendEmailRecomendacoes();
        log.info("Emails enviados");
        alreadyExecuted = false;
    }

    public EmailService(Configuration configuration, JavaMailSender javaMailSender) throws IOException {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(Usuario user) throws MessagingException, IOException, TemplateException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setSubject("Bem-vindo (a) ao Fórum!");
        helper.setTo(user.getEmail());
        String emailContent = getEmailContent(user);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendEmailResposta(Usuario autorResposta, Resposta resposta, Usuario autorTopico) throws MessagingException, IOException, TemplateException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setSubject("Seu tópico foi respondido!");
        helper.setTo(autorTopico.getEmail());
        String emailContent = getEmailContentResposta(autorResposta, resposta, autorTopico);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendEmailRecomendacoes() throws MessagingException, IOException, TemplateException {

        log.info("Iniciando envio de emails.");

        if (alreadyExecuted == false) {
            emails = emailsUsuariosService.findAll().stream().map(EmailsUsuarios::getEmail).collect(Collectors.toList());
            log.info("Lista possui " + emails.size() + " emails.");
            alreadyExecuted = true;
        }

        do {

            if (emails.size() != 0) {

                int i = emails.size() - 1;

                Usuario user = usuarioService.findUsuarioByEmail(emails.get(i));
                List<Topico> topicos = recomendacoesService.sugerirTopicos(user);

                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
                helper.setSubject("Achamos que você vai gostar desses tópicos");

                if (topicos.size() == 0) {
                    emails.remove(user.getEmail());
                    log.info("usuario " + user.getEmail() + " removido da lista pois nao ha topicos para recomendar para esse usuario ainda.");

                    if (emails.size() != 0) {
                        sendEmailRecomendacoes();
                    }
                    break;
                }

                helper.setTo(user.getEmail());
                String emailContentRecomendacoes = getEmailContentRecomendacoes(user, topicos);
                helper.setText(emailContentRecomendacoes, true);
                javaMailSender.send(mimeMessage);

                log.info("email enviado para " + user.getEmail());

                emails.remove(user.getEmail());

                if (emails.size() == 0) {
                    break;
                }

            }

        } while (emails.size() != 0);

    }

    String getEmailContent(Usuario user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        configuration.getTemplate("email2.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    String getEmailContentRecomendacoes(Usuario user, List<Topico> topicos) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("topicos", topicos);
        configuration.getTemplate("emailRecomendacoes.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    String getEmailContentResposta(Usuario autorResposta, Resposta resposta, Usuario autorTopico) throws IOException, TemplateException {

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("autorTopico", autorTopico);
        model.put("resposta", resposta);
        model.put("autorResposta", autorResposta);
        configuration.getTemplate("emailResposta.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
