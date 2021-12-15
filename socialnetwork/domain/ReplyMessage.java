package socialnetwork.domain;

import socialnetwork.domain.containers.GroupMessage;

import java.time.LocalDateTime;
import java.util.UUID;

import static socialnetwork.domain.constants.Constants.DATEFORMATTER;

public class ReplyMessage extends Message{

    private Message reply ;

    public ReplyMessage(Message reply_message, User from, GroupMessage to, String message, LocalDateTime date) {
        super(from, to, message, date);
        setId(UUID.randomUUID());
        this.reply = reply_message;
    }

    public ReplyMessage(User from, GroupMessage to, String message, LocalDateTime date) {
        super(from, to, message, date);
        setId(UUID.randomUUID());
        this.reply = null;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        String timestamp = getDate().format(DATEFORMATTER);
        return "The user " + this.getFrom().getFirstName() + " " + this.getFrom().getLastName() +": reply to: " +
                "'" + this.reply.getMessage() + "' with " + this.getMessage() + " timestamp : " + timestamp;
    }
}

