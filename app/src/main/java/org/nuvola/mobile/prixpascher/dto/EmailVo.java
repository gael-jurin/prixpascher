package org.nuvola.mobile.prixpascher.dto;

public class EmailVo {
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String message;
    private String attachment;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getCc() {
        return cc;
    }

    public String getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public static final class Builder {
        private EmailVo EmailVo;

        public Builder(String to) {
            this.EmailVo = new EmailVo();
            this.EmailVo.to = to;
        }

        public Builder from(String from) {
            this.EmailVo.from = from;
            return this;
        }

        public Builder subject(String subject) {
            EmailVo.subject = subject;
            return this;
        }

        public Builder message(String message) {
            EmailVo.message = message;
            return this;
        }

        public Builder cc(String cc) {
            EmailVo.cc = cc;
            return this;
        }

        public Builder bcc(String bcc) {
            EmailVo.bcc = bcc;
            return this;
        }

        public EmailVo build() {
            return this.EmailVo;
        }
    }
}
