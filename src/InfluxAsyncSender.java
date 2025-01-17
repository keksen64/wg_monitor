package influx;

import prepare.ConfigReader;

public class InfluxAsyncSender extends Thread {

    private String type;
    private long value;
    private int status;

//в отдельном потоке отправляет метрики в инфлюкс
    public InfluxAsyncSender(String type, long value, int status) {
        this.type = type;
        this.value = value;
        this.status = status;
    }
    public void run(){
        HttpSender.executePost("http://"+ ConfigReader.influxHost +":"+ConfigReader.influxPort+"/write?db="+ ConfigReader.influxDB, "LRTransaction40,Module=OW,TransactionName="+type+",Status_1="+status+" value=" + value);
    }

}
