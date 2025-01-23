
public class InfluxAsyncSender extends Thread {

    private String type;
    private long value;
    private String peer_ip;

//в отдельном потоке отправляет метрики в инфлюкс
    public InfluxAsyncSender(String type, long value, String  peer_ip) {
        this.type = type;
        this.value = value;
        this.peer_ip = peer_ip;
    }
    public void run(){
        HttpSender.executePost( ConfigLoader.getAddress()+"/write?db="+ConfigLoader.getDbName(), type+",peer="+peer_ip+" value="+value);
    }

}
