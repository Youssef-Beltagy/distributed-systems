/**
 * A serializable object that stores information for every message
 * to use a vector clock for causal sorting.
 *
 * @author  Youssef Beltagy
 * @version 4/20/2020
 */

import java.io.Serializable;

public class MessageData implements Serializable {

    public static final long serialVersionUID = 42L;

    public int[] vClock = null; // vector clock
    public String message = null; // the message to be sent
    public int senderRank = -1; // the rank of the sender

    public MessageData(String newMessage, int[] newVClock, int newSenderRank){
        this.vClock = newVClock;
        this.message = newMessage;
        this.senderRank = newSenderRank;
    }

}