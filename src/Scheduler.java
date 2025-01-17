public class Scheduler extends Thread {
    @Override
    public void run() {
        while (true){
                // получаем текущий снапшот
                Storage.lastSnap = SSHConnect.getWgShow();
                // заполняем карту айпи-объем траффика
                Storage.fillLastMap();
                // вычисляем дельту трафика
                Storage.calculateToDeltaMap();
                // направляем результат в хранилище

                // меняем мапы местами
                Storage.changeMap();
        }
    }
}
