public class MainClass {
    public static void main(String[] args) {

        if (args.length > 0) {
            ConfigLoader.loadConfig(args[0]);
        } else {
            System.out.println("Аргумент с конфигурационным файлом не передан. Используются значения по умолчанию.");
        }

        System.out.println("Текущая конфигурация:");
        System.out.println("Address: " + ConfigLoader.getAddress());
        System.out.println("DB Name: " + ConfigLoader.getDbName());
        System.out.println("Interface Name: " + ConfigLoader.getInterfaceName());

        Scheduler sch = new Scheduler();
        sch.run();
    }
}
