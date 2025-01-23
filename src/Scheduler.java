import java.time.Clock;

public class Scheduler extends Thread {
    @Override
    public void run() {
        Clock clock = Clock.systemDefaultZone();
        while(true){
            long startMills = clock.millis();
            // получаем текущий снапшот
            Storage.lastSnap = SSHConnect.getWgShow();
            // заполняем карту айпи-объем траффика
            Storage.fillLastMap();
            // вычисляем дельту трафика
            Storage.calculateToDeltaMap1();
            // направляем результат в хранилище
            Storage.sendMetricsToInflux();
            // меняем мапы местами
            Storage.changeMap();

            // Удаление старых файлов
            FileCleaner.cleanOldFiles(".", "CNAPSHOT_", 30);

            long endMills = clock.millis();
            long diffMills = endMills - startMills;
            try {
                if(diffMills<60000){
                    Thread.sleep(60000-diffMills);
                }else {
                    System.out.println("Превышено время выполнения цикла");
                }
            }catch (Exception e){
                java.time.LocalDateTime currentDateTimeErr = java.time.LocalDateTime.now();
                System.err.println(currentDateTimeErr + " \nFatal error " + e);
                break;
            }
        }
    }
}
