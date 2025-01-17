public class ClientSnapValue {
    private long received = 0;
    private long sent = 0;

    public long getReceived() {
        return received;
    }

    public long getSent() {
        return sent;
    }

    public void setReceived(long received) {
        this.received = received;
    }

    public void setSent(long sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "ClientSnapValue{" +
                "received=" + received +
                ", sent=" + sent +
                '}';
    }
}
