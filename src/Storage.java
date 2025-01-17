import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Storage {

    public static String lastSnap;

    public static Map<String, ClientSnapValue> prevMap = new HashMap<>(256);
    public static Map<String, ClientSnapValue> lastMap = new HashMap<>(256);
    public static Map<String, ClientSnapValue> deltaMap = new HashMap<>(256);

    public static void fillLastMap(){
        String regex = "allowed ips: (\\S+).*?transfer: ([\\d.]+)\\s(\\w+) received, ([\\d.]+)\\s(\\w+) sent";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(lastSnap);
        while (matcher.find()) {
            String allowedIps = matcher.group(1);
            double receivedValue = Double.parseDouble(matcher.group(2));
            String receivedUnit = matcher.group(3);
            double sentValue = Double.parseDouble(matcher.group(4));
            String sentUnit = matcher.group(5);

            // Преобразование в MiB
            long receivedMiB = convertToMiB(receivedValue, receivedUnit);
            long sentMiB = convertToMiB(sentValue, sentUnit);

            // Заполнение мапы
            ClientSnapValue snapValue = new ClientSnapValue();
            snapValue.setReceived(receivedMiB);
            snapValue.setSent(sentMiB);

            lastMap.put(allowedIps, snapValue);
        }
       // lastMap.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
    }

    private static long convertToMiB(double value, String unit) {
        switch (unit) {
            case "GiB":
                return Math.round(value * 1024);
            case "MiB":
                return Math.round(value);
            case "KiB":
                return Math.round(value / 1024);
            default:
                throw new IllegalArgumentException("Неизвестная единица измерения: " + unit);
        }
    }

    public static void changeMap() {
        prevMap = new HashMap<>(lastMap);
        lastMap.clear();
    }


    public static void calculateToDeltaMap1() {
        deltaMap.clear();

        for (Map.Entry<String, ClientSnapValue> entryLast : lastMap.entrySet()) {
            String key = entryLast.getKey();
            ClientSnapValue valueLast = entryLast.getValue();

            if (prevMap.containsKey(key)) {
                ClientSnapValue valuePrev = prevMap.get(key);

                long deltaReceived = valueLast.getReceived() - valuePrev.getReceived();
                long deltaSent = valueLast.getSent() - valuePrev.getSent();

                ClientSnapValue deltaValue = new ClientSnapValue();
                deltaValue.setReceived(deltaReceived);
                deltaValue.setSent(deltaSent);

                deltaMap.put(key, deltaValue);
            }
        }
    }

    public static void sendMetricsToInflux() {
        for (Map.Entry<String, ClientSnapValue> entry : deltaMap.entrySet()) {
            String peerIp = entry.getKey();
            ClientSnapValue snapValue = entry.getValue();

            // Создание и запуск потока для отправки "received"
            InfluxAsyncSender receivedSender = new InfluxAsyncSender("receivedPer10s", snapValue.getReceived(), peerIp);
            receivedSender.start();

            // Создание и запуск потока для отправки "sent"
            InfluxAsyncSender sentSender = new InfluxAsyncSender("sentPer10s", snapValue.getSent(), peerIp);
            sentSender.start();
        }
    }

}
