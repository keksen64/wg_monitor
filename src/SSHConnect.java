import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SSHConnect {
    public static String getWgShow() {
        // Параметры SSH
        String user = "root";
        String host = "91.245.224.164";
        String command = "wg show";

        // Форматирование текущей даты и времени
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String filename = "CNAPSHOT_" + timestamp + ".txt";

        // Инициализация StringBuilder для сбора вывода
        StringBuilder outputBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        try {
            // Запуск процесса SSH
            ProcessBuilder processBuilder = new ProcessBuilder("ssh", user + "@" + host, command);
            Process process = processBuilder.start();

            // Чтение стандартного вывода процесса
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line).append(System.lineSeparator());
            }

            // Чтение стандартного вывода ошибок процесса
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );
            while ((line = errorReader.readLine()) != null) {
                errorBuilder.append(line).append(System.lineSeparator());
            }

            // Ожидание завершения процесса
            int exitCode = process.waitFor();
            System.out.println("SSH команда завершилась с кодом: " + exitCode);

            // Получение полного вывода
            String output = outputBuilder.toString();
            String errors = errorBuilder.toString();

            // Вывод стандартного вывода в консоль
            System.out.println("OUTPUT:\n" + output);

            // Вывод ошибок, если они есть
            if (!errors.isEmpty()) {
                System.err.println("ERRORS:\n" + errors);
            }

            // Сохранение вывода в файл
            Files.write(Paths.get(filename), output.getBytes());
            System.out.println("Вывод сохранён в файл: " + filename);
            return output;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return null;
        }
    }
}
