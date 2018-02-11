package org.nuvola.mobile.prixpascher.dto;

public class EmailVO {
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
        private EmailVO EmailVO;

        public Builder(String to) {
            this.EmailVO = new EmailVO();
            this.EmailVO.to = to;
        }

        public Builder from(String from) {
            this.EmailVO.from = from;
            return this;
        }

        public Builder subject(String subject) {
            EmailVO.subject = subject;
            return this;
        }

        public Builder message(String message) {
            EmailVO.message = message;
            return this;
        }

        public Builder cc(String cc) {
            EmailVO.cc = cc;
            return this;
        }

        public Builder bcc(String bcc) {
            EmailVO.bcc = bcc;
            return this;
        }

        public EmailVO build() {
            return this.EmailVO;
        }
    }
}
