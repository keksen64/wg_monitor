
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
        //traffic_receive_30_sec
        HttpSender.executePost("http://"+ "localhost" +":"+"8086"+"/write?db=wg_metric", type+",peer="+peer_ip+" value="+value);
    }

}
