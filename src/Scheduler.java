import java.io.EOFException;

public class Scheduler extends Thread {
    @Override
    public void run() {
        while (true){
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

                try {
                    Thread.sleep(30000);
                }catch (Exception e){
                    System.err.println(e);
                }
        }
    }
}
