import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class ConfigLoader {

    // Статичные переменные с дефолтными значениями
    private static String address = "http://localhost:8086";
    private static String dbName = "wg_metric";
    private static String interfaceName = "wg0";

    /**
     * Метод для загрузки конфигурации из JSON-файла
     *
     * @param configFilePath путь к конфигурационному файлу (JSON)
     */
    public static void loadConfig(String configFilePath) {
        if (configFilePath == null || configFilePath.isEmpty()) {
            System.out.println("Конфигурационный файл не передан. Используются значения по умолчанию.");
            return;
        }

        File configFile = new File(configFilePath);
        if (!configFile.exists() || !configFile.isFile()) {
            System.out.println("Файл конфигурации не найден. Используются значения по умолчанию.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"address\"")) {
                    address = extractValue(line);
                } else if (line.startsWith("\"dbName\"")) {
                    dbName = extractValue(line);
                } else if (line.startsWith("\"interfaceName\"")) {
                    interfaceName = extractValue(line);
                }
            }
            System.out.println("Конфигурация успешно загружена.");
        } catch (IOException e) {
            System.err.println("Ошибка при чтении конфигурационного файла: " + e.getMessage());
        }
        String query = "CREATE DATABASE " + dbName;
        String url = address + "/query";
        try {
            HttpSender.executePost(url, "q=" + query);
            System.out.println("База данных \"" + dbName + "\" успешно создана (или уже существует).\n");
        } catch (Exception e) {
            System.err.println("Ошибка при создании базы данных: " + e.getMessage());
        }
    }

    /**
     * Метод для извлечения значения из строки вида "ключ": "значение"
     *
     * @param line строка из файла
     * @return извлеченное значение
     */
    private static String extractValue(String line) {
        int colonIndex = line.indexOf(':');
        if (colonIndex != -1) {
            String value = line.substring(colonIndex + 1).trim();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                return value.substring(1, value.length() - 1);
            }
        }
        return "";
    }

    // Геттеры для получения значений переменных
    public static String getAddress() {
        return address;
    }

    public static String getDbName() {
        return dbName;
    }

    public static String getInterfaceName() {
        return interfaceName;
    }

}
