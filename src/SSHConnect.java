import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class SSHConnect {
    public static void main(String[] args) {
        // Параметры SSH
        String user = "root";
        String host = "91.245.224.164";
        String command = "wg show";

        // Полная команда SSH
        String sshCommand = String.format("ssh %s@%s %s", user, host, "\"" + command + "\"");

        try {
            // Запускаем процесс SSH
            ProcessBuilder processBuilder = new ProcessBuilder("ssh", user + "@" + host, command);
            Process process = processBuilder.start();

            // Читаем стандартный вывод процесса
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                // Обрабатываем каждую строку вывода
                System.out.println("OUTPUT: " + line);
            }

            // Ждём завершения процесса
            int exitCode = process.waitFor();
            System.out.println("SSH команда завершилась с кодом: " + exitCode);

            // Читаем стандартный вывод ошибок процесса
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );
            while ((line = errorReader.readLine()) != null) {
                // Обрабатываем каждую строку ошибок
                System.err.println("ERROR: " + line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
